package rahul.lohra.networkmonitor.network

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import rahul.lohra.networkmonitor.data.WebsocketData
import rahul.lohra.networkmonitor.data.local.db.DatabaseProvider
import rahul.lohra.networkmonitor.data.mappers.toEntity

class NetworkWebSocketListener() : WebSocketListener() {

    private val database = DatabaseProvider.getDatabase()
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onOpen(webSocket: WebSocket, response: Response) {
        sendLog(
            webSocket.request().url.toString(),
            "WebSocket OPEN",
            response.body?.string().orEmpty()
        )
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        sendLog(webSocket.request().url.toString(), "WebSocket ↓", text)
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        sendLog(webSocket.request().url.toString(), "WebSocket ↓ (binary)", bytes.hex())
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        sendLog(
            webSocket.request().url.toString(),
            "WebSocket CLOSING",
            "code=$code, reason=$reason"
        )
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        sendLog(
            webSocket.request().url.toString(),
            "WebSocket CLOSED",
            "code=$code, reason=$reason"
        )
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {

        sendLog(webSocket.request().url.toString(),
            "WebSocket ERROR", buildString {
                append(t.message)
                append("\n\nStack Trace:\n")
                append(t.stackTraceToString())
                response?.let {
                    append("\n\nResponse: ${it.code} ${it.message}")
                }
            })
    }

    private fun sendLog(url: String, direction: String, body: String = "") {

        Log.d("InspectingWebSocketListener", "sendLog: direction:$direction, body:$body")

        val log = WebsocketData(
            requestUrl = url,
            direction = direction,
            body = body,
            timestamp = System.currentTimeMillis(),
            networkType = "ws"
        )

        scope.launch {
            database.networkLogDao().insert(log.toEntity())
        }
    }
}
