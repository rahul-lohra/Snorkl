package rahul.lohra.networkinspector

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class InspectingWebSocketListener(
    private val original: WebSocketListener,
    private val requestUrl: String
) : WebSocketListener() {

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onOpen(webSocket: WebSocket, response: Response) {

        sendLog("WebSocket OPEN", response.body?.string().orEmpty())
        original.onOpen(webSocket, response)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        sendLog("WebSocket ↓", text)
        original.onMessage(webSocket, text)
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        sendLog("WebSocket ↓ (binary)", bytes.hex())
        original.onMessage(webSocket, bytes)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        sendLog("WebSocket CLOSING", "code=$code, reason=$reason")
        original.onClosing(webSocket, code, reason)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        sendLog("WebSocket CLOSED", "code=$code, reason=$reason")
        original.onClosed(webSocket, code, reason)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        sendLog("WebSocket ERROR", buildString {
            append(t.message)
            append("\n\nStack Trace:\n")
            append(t.stackTraceToString())
            response?.let {
                append("\n\nResponse: ${it.code} ${it.message}")
            }
        })
        original.onFailure(webSocket, t, response)
    }

    private fun sendLog(direction: String, body: String = "") {

        Log.d("InspectingWebSocketListener", "sendLog: direction:$direction, body:$body")

        val log = WebsocketData(
            requestUrl = requestUrl,
            direction = direction,
            body = body,
            timestamp = System.currentTimeMillis(),
            networkType = "ws"
        )

        scope.launch {
            WebSocketServerManager.send(log)
        }
    }
}

