package rahul.lohra.snorkl.domain.usecas

import kotlinx.coroutines.flow.Flow
import rahul.lohra.snorkl.data.local.entities.NetworkEntity
import rahul.lohra.snorkl.data.repository.NetworkRepository

class RequestResponseRetrieverUseCase(private val repository: NetworkRepository) {

    suspend fun getRecord(id: String): Flow<NetworkEntity> {
        return repository.getLog(id)
    }
}