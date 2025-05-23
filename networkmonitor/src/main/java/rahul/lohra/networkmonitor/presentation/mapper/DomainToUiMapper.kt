package rahul.lohra.networkmonitor.presentation.mapper

import kotlinx.serialization.json.Json
import rahul.lohra.networkmonitor.RestApiListItem
import rahul.lohra.networkmonitor.WebsocketListItem
import rahul.lohra.networkmonitor.data.local.entities.NetworkEntity

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
    networkType = "rest"
)

fun NetworkEntity.toWebsocketListItem() = WebsocketListItem(
    id = id,
    timestamp = timestamp,
    requestUrl = requestUrl,
    responseCode = responseCode,
    body = body ?: "",
    durationMs = durationMs ?: 0L,
    direction = direction ?: "",
)