package rahul.lohra.networkinspector

import android.content.Context
import android.content.res.AssetManager
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.http.content.default
import io.ktor.server.http.content.files
import io.ktor.server.http.content.resources
import io.ktor.server.http.content.static
import io.ktor.server.http.content.staticFiles
import io.ktor.server.http.content.staticResources
import io.ktor.server.netty.Netty
import io.ktor.server.response.respond
import io.ktor.server.response.respondBytes
import io.ktor.server.response.respondOutputStream
import io.ktor.server.response.respondRedirect
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.websocket.DefaultWebSocketServerSession
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.pingPeriod
import io.ktor.server.websocket.timeout
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.io.IOException
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import kotlin.time.Duration.Companion.seconds
import java.util.Collections
import kotlin.Result

object WebSocketManager {
    private val clients = Collections.synchronizedSet(mutableSetOf<DefaultWebSocketServerSession>())

    private val wsMutex = Mutex()

    fun register(session: DefaultWebSocketServerSession) {
        clients.add(session)
    }

    fun unregister(session: DefaultWebSocketServerSession) {
        clients.remove(session)
    }

    suspend fun broadcast(log: NetworkLogData) {
        val json = kotlinx.serialization.json.Json.encodeToString(NetworkLogData.serializer(), log)
        clients.forEach {
            try {
                it.send(Frame.Text(json))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun send(log: InspectorLog.Network) {
        // Launching in global scope — you can improve this by passing a coroutine scope
        GlobalScope.launch {
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

    fun startServer(context: Context) {
        GlobalScope.launch {
            embeddedServer(Netty, port = 9394) {
                install(WebSockets) {
                    pingPeriod = 15.seconds
                    timeout = 30.seconds
                    maxFrameSize = Long.MAX_VALUE
                    masking = false
                }

                routing {
                    webSocket("/logs") {
                        WebSocketManager.register(this)
                        try {
//                            send(Frame.Text("SERVER: Connection established"))
                            for (frame in incoming) {
                                // Optionally respond to client
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        } finally {
                            WebSocketManager.unregister(this)
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