package rahul.lohra.networkmonitor.domain.usecas

import androidx.paging.PagingData
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
    operator fun invoke(): Flow<PagingData<NetworkListItem>> {
        return repository.getPagedNetworkLogs().flow.map { it.map { if (it.networkType == "rest") it.toRestApiListItem() else it.toWebsocketListItem() } }
    }
}
