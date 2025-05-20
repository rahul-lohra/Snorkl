package rahul.lohra.networkinspector.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import rahul.lohra.networkinspector.data.local.dao.NetworkLogDao
import rahul.lohra.networkinspector.data.local.entities.NetworkEntity
import rahul.lohra.networkinspector.data.local.paging.NetworkLogPagingSource
import rahul.lohra.networkinspector.domain.repository.NetworkRepository

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
