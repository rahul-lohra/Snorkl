package rahul.lohra.networkmonitor.network

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import rahul.lohra.networkmonitor.data.WebSocketEventType
import rahul.lohra.networkmonitor.data.WebSocketLogEntry
import rahul.lohra.networkmonitor.data.WebSocketMessageDirection
import rahul.lohra.networkmonitor.data.WebSocketMessageType
import rahul.lohra.networkmonitor.data.WebsocketData
import rahul.lohra.networkmonitor.data.local.db.DatabaseProvider
import rahul.lohra.networkmonitor.data.mappers.toEntity
import java.util.UUID

class NetworkWebSocketListener() : WebSocketListener() {

    private val database = DatabaseProvider.getDatabase()
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onOpen(webSocket: WebSocket, response: Response) {
        sendLog(
            webSocket.request().url.toString(),
            "WebSocket OPEN",
            response.body?.string().orEmpty()
        )
        logWebSocketEvent(
            webSocket, WebSocketEventType.CONNECTION_OPEN,
            content = response.body?.string().orEmpty()
        )
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        sendLog(webSocket.request().url.toString(), "WebSocket ↓", text)
        logWebSocketEvent(
            webSocket, WebSocketEventType.MESSAGE,
            direction = WebSocketMessageDirection.INCOMING,
            messageType = WebSocketMessageType.TEXT,
            content = text
        )
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        sendLog(webSocket.request().url.toString(), "WebSocket ↓ (binary)", bytes.hex())
        logWebSocketEvent(
            webSocket, WebSocketEventType.MESSAGE,
            direction = WebSocketMessageDirection.INCOMING,
            messageType = WebSocketMessageType.BINARY,
            content = bytes.base64()
        )
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        val content = "code=$code, reason=$reason"
        sendLog(
            webSocket.request().url.toString(),
            "WebSocket CLOSING",
            content
        )
        logWebSocketEvent(
            webSocket, WebSocketEventType.CONNECTION_CLOSING,
            content = content
        )
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        val content = "code=$code, reason=$reason"
        sendLog(webSocket.request().url.toString(), "WebSocket CLOSED", content)
        logWebSocketEvent(webSocket, WebSocketEventType.CONNECTION_CLOSED, content = content)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        val stackTrack = buildString {
            append(t.message)
            append("\n\nStack Trace:\n")
            append(t.stackTraceToString())
        }
        val content = response?.let { ("\n\nResponse: ${it.code} ${it.message}") }

        sendLog(webSocket.request().url.toString(), "WebSocket ERROR", content + stackTrack)

        logWebSocketEvent(
            webSocket, WebSocketEventType.CONNECTION_FAILURE,
            content = content,
            error = stackTrack
        )
    }

    private fun sendLog(url: String, direction: String, body: String = "") {

//        Log.d("InspectingWebSocketListener", "sendLog: direction:$direction, body:$body")

        val log = WebsocketData(
            requestUrl = url,
            direction = direction,
            body = body,
            timestamp = System.currentTimeMillis(),
            networkType = "ws"
        )

        scope.launch {
//            database.networkLogDao().insert(log.toEntity())
        }
    }

    fun logWebSocketEvent(
        webSocket: WebSocket,
        eventType: WebSocketEventType,
        direction: WebSocketMessageDirection? = null,
        messageType: WebSocketMessageType? = null,
        content: String? = null,
        error: String? = null,
    ) {
        val logEntry = WebSocketLogEntry(
            id = UUID.randomUUID().toString(),
            timestamp = System.currentTimeMillis(),
            eventType = eventType,
            direction = direction,
            messageType = messageType,
            connectionId = webSocket.hashCode().toString(),  // You might want to assign a real session ID
            messageSize = content?.length,
            content = content,
            error = error,
            requestUrl = webSocket.request().url.toString(),
            metadata = mapOf()
        )
        scope.launch {
            database.networkLogDao().insert(logEntry.toEntity())
        }

    }

}
