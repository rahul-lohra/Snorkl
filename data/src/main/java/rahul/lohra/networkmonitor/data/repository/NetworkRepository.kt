package rahul.lohra.snorkl.data.repository

import androidx.paging.Pager
import rahul.lohra.snorkl.data.local.entities.NetworkEntity

interface NetworkRepository {
    fun getPagedNetworkLogs(): Pager<Int, NetworkEntity>
    suspend fun clearAll()
    suspend fun getAllLogs(): List<NetworkEntity>
}
