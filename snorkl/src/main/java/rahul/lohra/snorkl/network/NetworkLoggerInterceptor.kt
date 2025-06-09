package rahul.lohra.snorkl.network

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import rahul.lohra.snorkl.data.RestApiData
import rahul.lohra.snorkl.data.local.dao.NetworkLogDao
import rahul.lohra.snorkl.data.local.db.DatabaseProvider
import rahul.lohra.snorkl.data.mappers.toEntity

class NetworkLoggerInterceptor(private val reportToWebserver: Boolean = true) : Interceptor {

    private val scope = CoroutineScope(Dispatchers.IO)
    private val database = DatabaseProvider.getDatabase()
    private val networkLogDao: NetworkLogDao = database.networkLogDao()

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
            val entity = logData.toEntity()
            val recordId = networkLogDao.insert(entity)
            Log.d("Noob", "RecordId: $recordId")
            if(reportToWebserver){
                WebSocketServerManager.send(entity) //TODO Rahul, do it later
            }

        }
        return response
    }
}
