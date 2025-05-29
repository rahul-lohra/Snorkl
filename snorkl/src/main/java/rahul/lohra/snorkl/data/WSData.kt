package rahul.lohra.snorkl.data

import kotlinx.serialization.Serializable

@Serializable
data class WebsocketData(
    val requestUrl: String,
    val direction: String, // e.g., "Request", "Response", "WebSocket OPEN", etc.
    val body: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val responseCode: Int = 0,
    val networkType: String = "ws"
): NetworkData()

@Serializable
data class WebSocketLogEntry(
    val id: String,
    val requestUrl: String,
    val timestamp: Long,
    val eventType: WebSocketEventType,             // Clearly identifies the type of event
    val direction: WebSocketMessageDirection?,     // Nullable for non-message events
    val messageType: WebSocketMessageType?,        // Nullable for non-message events
    val connectionId: String,                      // Unique session ID
    val messageSize: Int?,                         // Size in bytes, optional
    val content: String?,                          // Message content or event details
    val error: String?,                            // For failure cases
    val networkType: String = "ws",
    val metadata: Map<String, String>?             // Flexible extra data
): NetworkData()

enum class WebSocketEventType {
    MESSAGE,            // Regular data message (text or binary)
    CONNECTION_OPEN,    // onOpen event
    CONNECTION_CLOSING, // onClosing event
    CONNECTION_CLOSED,  // onClosed event
    CONNECTION_FAILURE  // onFailure event
}

enum class WebSocketMessageDirection {
    INCOMING,
    OUTGOING
}

enum class WebSocketMessageType {
    TEXT,
    BINARY,
    PING,
    PONG,
    NONE  // For non-message events (like connection open/close)
}

