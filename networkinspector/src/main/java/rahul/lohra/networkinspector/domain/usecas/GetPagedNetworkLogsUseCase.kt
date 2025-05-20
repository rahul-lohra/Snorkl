package rahul.lohra.networkinspector.domain.usecas

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import rahul.lohra.networkinspector.data.local.entities.NetworkEntity
import rahul.lohra.networkinspector.domain.repository.NetworkRepository

class GetPagedNetworkLogsUseCase(
    private val repository: NetworkRepository
) {
    operator fun invoke(): Flow<PagingData<NetworkEntity>> {
        return repository.getPagedNetworkLogs().flow
    }
}
