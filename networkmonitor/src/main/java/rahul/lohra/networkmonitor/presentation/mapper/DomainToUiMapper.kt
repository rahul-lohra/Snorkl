package rahul.lohra.networkmonitor.presentation.mapper

import kotlinx.serialization.json.Json
import rahul.lohra.networkmonitor.RestApiListItem
import rahul.lohra.networkmonitor.WebsocketListItem
import rahul.lohra.networkmonitor.data.WebSocketLogEntry
import rahul.lohra.networkmonitor.data.local.entities.NetworkEntity
import rahul.lohra.networkmonitor.data.local.entities.NetworkType

fun NetworkEntity.toRestApiListItem() = RestApiListItem(
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

fun WebSocketLogEntry.toWebsocketListItem() = WebsocketListItem(
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