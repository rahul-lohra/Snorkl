package rahul.lohra.snorkl

import android.app.Application
import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import rahul.lohra.snorkl.network.NetworkWebSocketListener
import java.io.IOException

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        WebSocketServerManager.startServer(this, 9396)
        Log.d("Inspector", "Phone IP: ${Util.getLocalIpAddress(this)}")
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
                webSocket = ws
            }

            override fun onMessage(ws: WebSocket, text: String) {
                Log.d("WS", "📥 Received: $text")
            }

            override fun onFailure(ws: WebSocket, t: Throwable, response: Response?) {
                Log.e("WS", "❌ WebSocket error: ${t.message}", t)
            }

            override fun onClosed(ws: WebSocket, code: Int, reason: String) {
                Log.d("WS", "✅ Closed: $reason")
                webSocket = null
            }
        }
        val wrappedListener = MySocketListenerRegistry.wrap(listener)
        MySocketListenerRegistry.register(NetworkWebSocketListener())
        MySocketListenerRegistry.register(InspectingWebSocketListener())
        client.newWebSocket(request, wrappedListener)
    }

    fun sendMessage(message: String) {
        webSocket?.send(message) ?: Log.w("WS", "⚠️ WebSocket not connected")
    }

    fun close() {
        webSocket?.close(1000, "Closing from user")
        webSocket = null
    }
}

object MySocketListenerRegistry {
    private val listeners = mutableListOf<WebSocketListener>()

    fun register(listener: WebSocketListener) {
        listeners += listener
    }

    internal fun wrap(baseListener: WebSocketListener): WebSocketListener {
        return object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                listeners.forEach { it.onOpen(webSocket, response) }
                baseListener.onOpen(webSocket, response)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                listeners.forEach { it.onMessage(webSocket, text) }
                baseListener.onMessage(webSocket, text)
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                listeners.forEach { it.onMessage(webSocket, bytes) }
                baseListener.onMessage(webSocket, bytes)
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                listeners.forEach { it.onClosing(webSocket, code, reason) }
                baseListener.onClosing(webSocket, code, reason)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                listeners.forEach { it.onClosed(webSocket, code, reason) }
                baseListener.onClosed(webSocket, code, reason)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                listeners.forEach { it.onFailure(webSocket, t, response) }
                baseListener.onFailure(webSocket, t, response)
            }
        }
    }
}