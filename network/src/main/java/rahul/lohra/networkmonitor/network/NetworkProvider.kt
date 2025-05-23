package rahul.lohra.networkmonitor.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkProvider {

    private const val BASE_URL = "https://hipsum.co/api/" //"https://hipsum.co/api/?type=hipster-centric&paras=2"

    private lateinit var apiService: ApiService

    private fun createApiService() {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(NetworkLoggerInterceptor())
            .build()

        apiService = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    fun getApiService(): ApiService {
        if (!NetworkProvider::apiService.isInitialized)
            createApiService()
        return apiService
    }
}