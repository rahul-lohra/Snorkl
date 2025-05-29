package rahul.lohra.snorkl.domain.usecas

import rahul.lohra.snorkl.data.repository.NetworkRepository

class ClearDataUseCase(private val repository: NetworkRepository) {

    suspend fun clearAll() {
        repository.clearAll()
    }

    suspend fun delete(id: String) {
        repository.delete(id)
    }
}