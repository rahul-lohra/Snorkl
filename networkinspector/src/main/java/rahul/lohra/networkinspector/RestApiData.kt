package rahul.lohra.networkinspector

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class RestApiData(
    val id: String = UUID.randomUUID().toString(),
    val timestamp: Long = System.currentTimeMillis(),
    val requestUrl: String,
    val method: String,
    val requestHeaders: Map<String, List<String>>,
    val responseCode: Int,
    val responseHeaders: Map<String, List<String>>,
    val body: String,
    val durationMs: Long,
    val networkType: String = "rest"
)

@Serializable
data class WebsocketData(
    val requestUrl: String,
    val direction: String, // e.g., "Request", "Response", "WebSocket OPEN", etc.
    val body: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val responseCode: Int = 0,
    val networkType: String = "ws"
)

