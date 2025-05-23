package rahul.lohra.networkmonitor.domain.usecas

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import rahul.lohra.networkmonitor.data.local.entities.NetworkEntity
import rahul.lohra.networkmonitor.data.repository.NetworkRepository

class GetPagedNetworkLogsUseCase(
    private val repository: NetworkRepository
) {
    operator fun invoke(): Flow<PagingData<NetworkEntity>> {
        return repository.getPagedNetworkLogs().flow
    }
}
