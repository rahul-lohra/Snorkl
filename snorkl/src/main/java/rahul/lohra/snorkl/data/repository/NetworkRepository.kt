package rahul.lohra.snorkl.data.repository

import androidx.paging.Pager
import kotlinx.coroutines.flow.Flow
import rahul.lohra.snorkl.data.local.entities.NetworkEntity

interface NetworkRepository {
    fun getPagedNetworkLogs(): Pager<Int, NetworkEntity>
    suspend fun clearAll()
    suspend fun delete(id: String)
    suspend fun getAllLogs(): List<NetworkEntity>
    suspend fun getLog(id: String): Flow<NetworkEntity>
}
