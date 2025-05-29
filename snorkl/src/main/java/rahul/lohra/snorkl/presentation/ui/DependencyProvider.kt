package rahul.lohra.snorkl.presentation.ui

import android.content.Context
import rahul.lohra.snorkl.data.local.db.DatabaseProvider
import rahul.lohra.snorkl.data.local.db.NetworkDatabase
import rahul.lohra.snorkl.data.repository.NetworkRepositoryImpl
import rahul.lohra.snorkl.domain.usecas.GetPagedNetworkLogsUseCase
import rahul.lohra.snorkl.domain.usecas.ShareUseCase

object DependencyProvider {
    lateinit var applicationContext: Context
    var db: NetworkDatabase = DatabaseProvider.getDatabase(applicationContext)
//    var networkRepository = NetworkRepositoryImpl(db.networkLogDao())
//    var networkRepository = GetPagedNetworkLogsUseCase(networkRepository)



    fun init(applicationContext: Context) {
        val db = DatabaseProvider.getDatabase(applicationContext)
        val networkRepository = NetworkRepositoryImpl(db.networkLogDao())
        val useCase = GetPagedNetworkLogsUseCase(networkRepository)
        val shareUseCase = ShareUseCase(networkRepository)
    }
}