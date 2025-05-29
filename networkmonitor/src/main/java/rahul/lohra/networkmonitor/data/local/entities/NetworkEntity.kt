package rahul.lohra.networkmonitor.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "network_logs")
data class NetworkEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val timestamp: Long,
    val requestUrl: String,
    val method: String?,            // Only for REST
    val requestHeaders: String?,   // Stored as JSON string
    val responseCode: Int,
    val responseHeaders: String?,  // Stored as JSON string
    val body: String?,
    val responseSize: Int?,
    val error: String?,
    val durationMs: Long?,         // Only for REST
    val requestBody: String?,      // Only for REST
    val direction: String?,        // Only for WS
    val eventType: String?,        // Only for WS
    val messageType: String?,        // Only for WS
    val connectionId: String?,        // Only for WS
    val metaData: String?,          // Only for WS
    val networkType: String        // "rest" or "ws"
)

enum class NetworkType(val title: String) {
    REST("rest"),
    WEBSOCKET("ws")
}
