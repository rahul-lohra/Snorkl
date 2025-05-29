package rahul.lohra.snorkl

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import rahul.lohra.snorkl.data.RestApiData

class NetworkLoggerInterceptor : Interceptor {

    private val scope = CoroutineScope(Dispatchers.IO)

    @OptIn(DelicateCoroutinesApi::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val startNs = System.nanoTime()

        val response = chain.proceed(request)
        val durationMs = (System.nanoTime() - startNs) / 1_000_000

        val body = response.peekBody(1024 * 1024).string()

        // Log Request Body
        val requestBodyString = try {
            val requestBody = request.body
            if (requestBody != null) {
                val buffer = Buffer()
                requestBody.writeTo(buffer)
                buffer.readUtf8()
            } else {
                null
            }
        } catch (e: Exception) {
            "Error reading request body: ${e.message}"
        }

        val logData = RestApiData(
            requestUrl = request.url.toString(),
            method = request.method,
            requestHeaders = request.headers.toMultimap(),
            responseCode = response.code,
            responseHeaders = response.headers.toMultimap(),
            body = body,
            durationMs = durationMs,
            requestBody = requestBodyString ?: "",
            networkType = "rest"
        )

        scope.launch {
            WebSocketServerManager.broadcast(logData)
        }
        return response
    }
}
