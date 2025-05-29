package rahul.lohra.networkmonitor.domain.usecas

import rahul.lohra.networkmonitor.data.repository.NetworkRepository

class ClearDataUseCase(private val repository: NetworkRepository) {

    suspend fun clearAll() {
        repository.clearAll()
    }

    suspend fun delete(id: String) {
        repository.delete(id)
    }
}