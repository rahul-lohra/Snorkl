package rahul.lohra.networkmonitor.domain.usecas

import kotlinx.coroutines.flow.Flow
import rahul.lohra.networkmonitor.data.local.entities.NetworkEntity
import rahul.lohra.networkmonitor.data.repository.NetworkRepository

class RequestResponseRetrieverUseCase(private val repository: NetworkRepository) {

    suspend fun getRecord(id: String): Flow<NetworkEntity> {
        return repository.getLog(id)
    }
}