package rahul.lohra.networkmonitor.data

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
    val requestBody: String,
    val networkType: String = "rest"
): NetworkData()

sealed class NetworkData


