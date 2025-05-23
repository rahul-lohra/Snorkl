package rahul.lohra.networkmonitor.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import rahul.lohra.networkmonitor.data.local.dao.NetworkLogDao
import rahul.lohra.networkmonitor.data.local.entities.NetworkEntity
import rahul.lohra.networkmonitor.data.local.paging.NetworkLogPagingSource

class NetworkRepositoryImpl(
    private val dao: NetworkLogDao
) : NetworkRepository {

    override fun getPagedNetworkLogs(): Pager<Int, NetworkEntity> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { NetworkLogPagingSource(dao) }
        )
    }
}
