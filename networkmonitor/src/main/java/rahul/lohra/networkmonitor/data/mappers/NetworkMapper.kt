package rahul.lohra.networkmonitor.data.mappers

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import rahul.lohra.networkmonitor.data.RestApiData
import rahul.lohra.networkmonitor.data.WebSocketEventType
import rahul.lohra.networkmonitor.data.WebSocketLogEntry
import rahul.lohra.networkmonitor.data.WebSocketMessageDirection
import rahul.lohra.networkmonitor.data.WebSocketMessageType
import rahul.lohra.networkmonitor.data.WebsocketData
import rahul.lohra.networkmonitor.data.local.entities.NetworkEntity
import rahul.lohra.networkmonitor.data.local.entities.NetworkType
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
    networkType = "rest",
    responseSize = body.length,
    error = null,
    eventType = null,
    messageType = null,
    connectionId = null,
    metaData = null
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
    eventType = null,
    networkType = "ws",
    responseSize = body?.length,
    error = null,
    messageType = null,
    connectionId = null,
    metaData = null
)

fun WebSocketLogEntry.toEntity(): NetworkEntity = NetworkEntity(
    id = id,
    timestamp = timestamp,
    requestUrl = requestUrl,
    method = null,
    requestHeaders = null,
    responseCode = 0,
    responseHeaders = null,
    body = content,
    durationMs = null,
    requestBody = null,
    direction = direction?.name,
    eventType = eventType.name,
    messageType = messageType?.name,
    connectionId = connectionId,
    responseSize = messageSize,
    error = error,
    metaData = Gson().toJson(metadata),
    networkType = networkType
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
            networkType = networkType
        )
    } else null

fun NetworkEntity.toWebSocketLogEntry(): WebSocketLogEntry =
        WebSocketLogEntry(
            id = id,
            requestUrl = requestUrl,
            timestamp = timestamp,
            eventType =  WebSocketEventType.valueOf(this.eventType!!),
            direction = this.direction?.let { WebSocketMessageDirection.valueOf(it) },  // Nullable conversion
            messageType = this.messageType?.let { WebSocketMessageType.valueOf(it) },  // Nullable conversion
            connectionId = this.connectionId!!,
            messageSize = this.responseSize,
            content = this.body,
            error = this.error,
            networkType = this.networkType,
            metadata = Gson().fromJson(this.metaData, object : TypeToken<Map<String, String>>() {}.type)
        )

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

