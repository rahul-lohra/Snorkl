package rahul.lohra.networkmonitor


interface NetworkListItem

class RestApiListItem(
    val id: String,
    val timestamp: Long,
    val requestUrl: String,
    val method: String,
    val requestHeaders: Map<String, List<String>>,
    val responseCode: Int,
    val responseHeaders: Map<String, List<String>>,
    val body: String,
    val durationMs: Long,
    val requestBody: String,
    val networkType: String = "rest"
) : NetworkListItem

class WebsocketListItem(
    val id: String,
    val timestamp: Long,
    val requestUrl: String,
    val responseCode: Int,
    val body: String,
    val durationMs: Long,
    val direction: String
): NetworkListItem
