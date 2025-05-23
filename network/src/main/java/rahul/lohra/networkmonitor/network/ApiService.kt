package rahul.lohra.networkmonitor.network

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api/")
    suspend fun getHipsterIpsum(
        @Query("type") type: String = "hipster-centric",
        @Query("paras") paras: Int = 2
    ): HipsterResponse
}

data class HipsterResponse(val text: String)
