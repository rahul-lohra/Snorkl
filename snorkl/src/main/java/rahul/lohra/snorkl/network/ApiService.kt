package rahul.lohra.snorkl.network

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api/")
    suspend fun getHipsterIpsum(
        @Query("type") type: String = "hipster-centric",
        @Query("paras") paras: Int = 2
    ): List<String>
}