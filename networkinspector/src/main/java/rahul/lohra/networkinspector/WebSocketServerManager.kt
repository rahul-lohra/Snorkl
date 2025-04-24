package rahul.lohra.networkinspector

import android.content.Context
import android.content.res.AssetManager
import android.os.Build
import android.util.Log
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.websocket.DefaultWebSocketServerSession
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.pingPeriod
import io.ktor.server.websocket.timeout
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.encodeToString
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.Collections

object WebSocketServerManager {
    private val clients = Collections.synchronizedSet(mutableSetOf<DefaultWebSocketServerSession>())

    private val wsMutex = Mutex()
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun register(session: DefaultWebSocketServerSession) {
        clients.add(session)
    }

    fun unregister(session: DefaultWebSocketServerSession) {
        clients.remove(session)
    }

    suspend fun broadcast(log: RestApiData) {
        val json = kotlinx.serialization.json.Json.encodeToString(RestApiData.serializer(), log)
        clients.forEach {
            try {
                it.send(Frame.Text(json))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun send(log: WebsocketData) {
        // Launching in global scope — you can improve this by passing a coroutine scope
        coroutineScope.launch {
            val json = kotlinx.serialization.json.Json.encodeToString(log)
            wsMutex.withLock {
                clients.forEach { session ->
                    try {
                        session.send(Frame.Text(json))
                    } catch (e: Exception) {
                        println("❌ Failed to send log: ${e.message}")
                    }
                }
            }
        }
    }

    fun startServer(context: Context, port: Int = 9394) {
        coroutineScope.launch {
            embeddedServer(Netty, port) {
                install(WebSockets) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        pingPeriod = java.time.Duration.ofSeconds(15)
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        timeout = java.time.Duration.ofSeconds(30)
                    }
                    maxFrameSize = Long.MAX_VALUE
                    masking = false
                }

                routing {
                    webSocket("/logs") {
                        WebSocketServerManager.register(this)
                        try {
                            Log.d("Noob", "inside /logs")
                            send(Frame.Text("SERVER: Connection established"))
                            for (frame in incoming) {
                                // Optionally respond to client
                                Log.d("Noob", "inside for loop of incoming")
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        } finally {
                            WebSocketServerManager.unregister(this)
                        }
                    }

                    get("/") {
                        call.respond("Hello world")
                    }
                    get("/inspector") {
                        val content = getTextFromAsset(context.assets,"web/index.html")
                        content.onSuccess {
                            call.respondText(it, io.ktor.http.ContentType.Text.Html, HttpStatusCode.OK)
                        }
                        content.onFailure {
                            call.respondText(
                                it.message ?: "",
                                io.ktor.http.ContentType.Text.Plain,
                                io.ktor.http.HttpStatusCode.InternalServerError
                            )
                        }
                    }

                    get("/inspector1") {
                        val content = getTextFromAsset(context.assets,"web/index1.html")
                        content.onSuccess {
                            call.respondText(it, io.ktor.http.ContentType.Text.Html, HttpStatusCode.OK)
                        }
                        content.onFailure {
                            call.respondText(
                                it.message ?: "",
                                io.ktor.http.ContentType.Text.Plain,
                                io.ktor.http.HttpStatusCode.InternalServerError
                            )
                        }
                    }
                }
            }.start(wait = false)
        }
    }
}

fun getTextFromAsset(assetManager: AssetManager, filePath: String): Result<String> {
    return try {
        val inputStream = assetManager.open(filePath)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val content = StringBuilder()
        var line: String? = reader.readLine()
        while (line != null) {
            content.append(line).append("\n")
            line = reader.readLine()
        }
        reader.close()
        inputStream.close()
        content.toString()
        Result.success(content.toString())
    } catch (e: Exception) {
        Result.failure<String>(Exception("Error loading index.html: ${e.localizedMessage}"))
    }
}