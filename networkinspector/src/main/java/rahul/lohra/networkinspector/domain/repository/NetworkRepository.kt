package rahul.lohra.networkinspector.domain.repository

import androidx.paging.Pager
import rahul.lohra.networkinspector.data.local.entities.NetworkEntity

interface NetworkRepository {
    fun getPagedNetworkLogs(): Pager<Int, NetworkEntity>
}
