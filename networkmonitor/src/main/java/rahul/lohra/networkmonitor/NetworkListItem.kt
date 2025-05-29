package rahul.lohra.networkmonitor


interface NetworkListItem {
    val id:String
    val networkType:String
}

class RestApiListItem(
    override val id: String,
    val timestamp: Long,
    val requestUrl: String,
    val method: String,
    val requestHeaders: Map<String, List<String>>,
    val responseCode: Int,
    val responseHeaders: Map<String, List<String>>,
    val body: String,
    val durationMs: Long,
    val requestBody: String,
    override val networkType: String = "rest"
) : NetworkListItem

class WebsocketListItem(
    override val id: String,
    val timestamp: Long,
    val requestUrl: String,
    val body: String,
    val durationMs: Long,
    val direction: String,
    val eventType: String,
    val messageType: String,
    val messageSize: String,
    override val networkType: String = "ws"
): NetworkListItem
