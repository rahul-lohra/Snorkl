package rahul.lohra.networkinspector

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response

class NetworkLoggerInterceptor : Interceptor {
    @OptIn(DelicateCoroutinesApi::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val startNs = System.nanoTime()

        val response = chain.proceed(request)
        val durationMs = (System.nanoTime() - startNs) / 1_000_000

        val body = response.peekBody(1024 * 1024).string()

        val logData = NetworkLogData(
            url = request.url.toString(),
            method = request.method,
            requestHeaders = request.headers.toMultimap(),
            responseCode = response.code,
            responseHeaders = response.headers.toMultimap(),
            body = body,
            durationMs = durationMs
        )

        GlobalScope.launch {
            WebSocketManager.broadcast(logData)
        }
        return response
    }
}
