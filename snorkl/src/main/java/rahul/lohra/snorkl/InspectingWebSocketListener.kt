package rahul.lohra.snorkl

//import android.util.Log
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import okhttp3.Response
//import okhttp3.WebSocket
//import okhttp3.WebSocketListener
//import okio.ByteString
//import rahul.lohra.snorkl.data.WebsocketData
//import rahul.lohra.snorkl.network.WebSocketServerManager
//
//class InspectingWebSocketListener() : WebSocketListener() {
//
//    private val scope = CoroutineScope(Dispatchers.IO)
//
//    override fun onOpen(webSocket: WebSocket, response: Response) {
//
//        sendLog("WebSocket OPEN", response.body?.string().orEmpty())
//    }
//
//    override fun onMessage(webSocket: WebSocket, text: String) {
//        sendLog("WebSocket ↓", text)
//    }
//
//    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
//        sendLog("WebSocket ↓ (binary)", bytes.hex())
//    }
//
//    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
//        sendLog("WebSocket CLOSING", "code=$code, reason=$reason")
//    }
//
//    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
//        sendLog("WebSocket CLOSED", "code=$code, reason=$reason")
//    }
//
//    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
//        sendLog("WebSocket ERROR", buildString {
//            append(t.message)
//            append("\n\nStack Trace:\n")
//            append(t.stackTraceToString())
//            response?.let {
//                append("\n\nResponse: ${it.code} ${it.message}")
//            }
//        })
//    }
//
//    private fun sendLog(direction: String, body: String = "") {
//
//        Log.d("InspectingWebSocketListener", "sendLog: direction:$direction, body:$body")
//
//        val log = WebsocketData(
//            requestUrl = "requestUrl",
//            direction = direction,
//            body = body,
//            timestamp = System.currentTimeMillis(),
//            networkType = "ws"
//        )
//
//        scope.launch {
//            WebSocketServerManager.send(log)
//        }
//    }
//}

