package rahul.lohra.snorkl.domain.usecas

import androidx.paging.PagingData
import androidx.paging.filter
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import rahul.lohra.snorkl.NetworkListItem
import rahul.lohra.snorkl.data.local.entities.NetworkType
import rahul.lohra.snorkl.data.mappers.toWebSocketLogEntry
import rahul.lohra.snorkl.data.repository.NetworkRepository
import rahul.lohra.snorkl.presentation.mapper.toRestApiListItem
import rahul.lohra.snorkl.presentation.mapper.toWebsocketListItem

class GetPagedNetworkLogsUseCase(
    private val repository: NetworkRepository
) {
    fun getPagedLogs(): Flow<PagingData<NetworkListItem>> {
        return repository.getPagedNetworkLogs().flow.map {
            it.map {
                if (it.networkType == NetworkType.REST.title)
                    it.toRestApiListItem()
                else
                    it.toWebSocketLogEntry()?.toWebsocketListItem()!!
            }
        }
    }

    fun getFilteredNetworkLogs(filterPredicate: (NetworkListItem) -> Boolean): Flow<PagingData<NetworkListItem>> {
        return repository.getPagedNetworkLogs()
            .flow
            .map { pagingData ->
                pagingData
                    .map {
                        if (it.networkType == NetworkType.REST.title) it.toRestApiListItem() else it.toWebSocketLogEntry()
                            ?.toWebsocketListItem()!!
                    }
                    .filter { filterPredicate(it) }
            }
    }
}
