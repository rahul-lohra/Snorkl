package rahul.lohra.networkmonitor.domain.usecas

import androidx.paging.PagingData
import androidx.paging.filter
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import rahul.lohra.networkmonitor.NetworkListItem
import rahul.lohra.networkmonitor.data.repository.NetworkRepository
import rahul.lohra.networkmonitor.presentation.mapper.toRestApiListItem
import rahul.lohra.networkmonitor.presentation.mapper.toWebsocketListItem

class GetPagedNetworkLogsUseCase(
    private val repository: NetworkRepository
) {
    fun getPagedLogs(): Flow<PagingData<NetworkListItem>> {
        return repository.getPagedNetworkLogs().flow.map { it.map { if (it.networkType == "rest") it.toRestApiListItem() else it.toWebsocketListItem() } }
    }

    fun getFilteredNetworkLogs(filterPredicate: (NetworkListItem) -> Boolean): Flow<PagingData<NetworkListItem>> {
        return repository.getPagedNetworkLogs()
            .flow
            .map { pagingData ->
                pagingData
                    .map { if (it.networkType == "rest") it.toRestApiListItem() else it.toWebsocketListItem() }
                    .filter { filterPredicate(it) }
            }
    }

    suspend fun clearAll() {
        repository.clearAll()
    }
}
