package rahul.lohra.networkmonitor.presentation.ui.detail

import kotlinx.serialization.json.Json
import rahul.lohra.networkmonitor.data.WebSocketLogEntry
import rahul.lohra.networkmonitor.data.local.entities.NetworkEntity
import rahul.lohra.networkmonitor.data.local.entities.NetworkType

class RestApiDetails(
    override val id: String,
    override val networkType: String = NetworkType.REST.title,
    val timestamp: Long,
    val requestUrl: String,
    val method: String,
    val requestHeaders: Map<String, List<String>>,
    val responseCode: Int,
    val responseHeaders: Map<String, List<String>>,
    val body: String,
    val durationMs: Long,
    val requestBody: String,
):DetailScreenUiModel

class WsDetails(
    override val id: String,
    override val networkType: String = NetworkType.WEBSOCKET.title,
    val timestamp: Long,
    val requestUrl: String,
    val body: String,
    val durationMs: Long,
    val direction: String,
    val eventType: String,
    val messageType: String,
    val messageSize: String,
):DetailScreenUiModel

interface DetailScreenUiModel {
    val id:String
    val networkType:String
}

fun NetworkEntity.toRestApiDetails()= RestApiDetails (
    id = id,
    timestamp = timestamp,
    requestUrl = requestUrl,
    method = method!!,
    requestHeaders = Json.decodeFromString(requestHeaders!!),
    responseCode = responseCode,
    responseHeaders = Json.decodeFromString(responseHeaders!!),
    body = body ?: "",
    durationMs = durationMs ?: 0L,
    requestBody = requestBody ?: "",
    networkType = networkType
)
fun WebSocketLogEntry.toWsDetails() = WsDetails (
    id = id,
    timestamp = timestamp,
    requestUrl = requestUrl,
    body = content ?: "",
    durationMs = 0L,
    direction = direction?.name ?: "",
    eventType = eventType.name,
    messageType = messageType?.name?:"",
    messageSize = messageSize?.toString()?:"",
    networkType = networkType,
)

