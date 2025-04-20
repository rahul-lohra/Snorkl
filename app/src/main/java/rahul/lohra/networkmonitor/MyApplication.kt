package rahul.lohra.networkmonitor

import android.app.Application
import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import rahul.lohra.networkinspector.InspectingWebSocketListener
import rahul.lohra.networkinspector.InspectorLog
import rahul.lohra.networkinspector.NetworkLoggerInterceptor
import rahul.lohra.networkinspector.Util
import rahul.lohra.networkinspector.WebSocketServerManager
import rahul.lohra.networkmonitor.RestClient.request
import java.io.IOException

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        WebSocketServerManager.startServer(this)
        Log.d("Inspector", "Phone IP: ${Util.getLocalIpAddress(this)}")
//        WebsocketManager.observeWebsocket()
    }
}

object RestClient {
    val client = OkHttpClient.Builder()
        .addInterceptor(NetworkLoggerInterceptor())
        .build()

    val request = Request.Builder()
        .url("https://httpbin.org/get")
        .build()

    fun makeRequest() {
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("DemoCall", "Request failed: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                Log.i("DemoCall", "Request successful: ${response.code}")
            }
        })
    }
}

fun OkHttpClient.newWebSocketWithInspector(
    request: Request,
    listener: WebSocketListener
): WebSocket {
    val inspectingListener = InspectingWebSocketListener(listener, request.url.toString())
    return this.newWebSocket(request, inspectingListener)
}

object WebsocketClient {
    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null
    private val requestUrl = "wss://echo.websocket.events"

    fun connectAndTest() {
        val request = Request.Builder()
            .url(requestUrl)
            .build()

        val listener = object : WebSocketListener() {
            override fun onOpen(ws: WebSocket, response: Response) {
                Log.d("WS", "✅ Connected to WebSocket")
                sendLog("WebSocket OPEN", response.body?.string().orEmpty())
                webSocket = ws
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                sendLog("WebSocket ↓ (binary)", bytes.hex())
            }

            override fun onMessage(ws: WebSocket, text: String) {
                Log.d("WS", "📥 Received: $text")
                sendLog("WebSocket ↓", text)
            }

            override fun onFailure(ws: WebSocket, t: Throwable, response: Response?) {
                Log.e("WS", "❌ WebSocket error: ${t.message}", t)
                sendLog("WebSocket ERROR", buildString {
                    append(t.message)
                    append("\n\nStack Trace:\n")
                    append(t.stackTraceToString())
                    response?.let {
                        append("\n\nResponse: ${it.code} ${it.message}")
                    }
                })
            }

            override fun onClosed(ws: WebSocket, code: Int, reason: String) {
                Log.d("WS", "✅ Closed: $reason")
                sendLog("WebSocket CLOSED", "code=$code, reason=$reason")
                webSocket = null
            }
        }

        client.newWebSocket(request, listener)
    }

    fun sendMessage(message: String) {
        webSocket?.send(message) ?: Log.w("WS", "⚠️ WebSocket not connected")
    }

    fun close() {
        webSocket?.close(1000, "Closing from user")
        webSocket = null
    }

    fun observeWebsocket() {

        val ws = client.newWebSocketWithInspector(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("App", "WebSocket connected!")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("App", "Received message: $text")
            }

            // other events...
        })
    }

    private fun sendLog(direction: String, body: String = "") {

        Log.d("InspectingWebSocketListener", "sendLog: direction:$direction, body:$body")

        val log = InspectorLog.Network(
            requestUrl = requestUrl,
            direction = direction,
            body = body,
            timestamp = System.currentTimeMillis()
        )

        GlobalScope.launch {
            WebSocketServerManager.send(log)
        }
    }
}