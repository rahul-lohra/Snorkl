package rahul.lohra.networkinspector.data.mappers

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import rahul.lohra.networkinspector.RestApiData
import rahul.lohra.networkinspector.WebsocketData
import rahul.lohra.networkinspector.data.local.entities.NetworkEntity
import java.util.UUID

fun RestApiData.toEntity(): NetworkEntity = NetworkEntity(
    id = id,
    timestamp = timestamp,
    requestUrl = requestUrl,
    method = method,
    requestHeaders = Json.encodeToString(requestHeaders),
    responseCode = responseCode,
    responseHeaders = Json.encodeToString(responseHeaders),
    body = body,
    durationMs = durationMs,
    requestBody = requestBody,
    direction = null,
    networkType = "rest"
)

fun WebsocketData.toEntity(): NetworkEntity = NetworkEntity(
    id = UUID.randomUUID().toString(),
    timestamp = timestamp,
    requestUrl = requestUrl,
    method = null,
    requestHeaders = null,
    responseCode = responseCode,
    responseHeaders = null,
    body = body,
    durationMs = null,
    requestBody = null,
    direction = direction,
    networkType = "ws"
)

fun NetworkEntity.toRestApiData(): RestApiData? =
    if (networkType == "rest") {
        RestApiData(
            id = id,
            timestamp = timestamp,
            requestUrl = requestUrl,
            method = method ?: "",
            requestHeaders = Json.decodeFromString(requestHeaders ?: "{}"),
            responseCode = responseCode,
            responseHeaders = Json.decodeFromString(responseHeaders ?: "{}"),
            body = body ?: "",
            durationMs = durationMs ?: 0L,
            requestBody = requestBody ?: "",
            networkType = "rest"
        )
    } else null

fun NetworkEntity.toWebsocketData(): WebsocketData? =
    if (networkType == "ws") {
        WebsocketData(
            requestUrl = requestUrl,
            direction = direction ?: "",
            body = body,
            timestamp = timestamp,
            responseCode = responseCode,
            networkType = "ws"
        )
    } else null

