package rahul.lohra.networkmonitor.presentation.ui

import android.content.Context
import rahul.lohra.networkmonitor.data.local.db.DatabaseProvider
import rahul.lohra.networkmonitor.data.local.db.NetworkDatabase
import rahul.lohra.networkmonitor.data.repository.NetworkRepositoryImpl
import rahul.lohra.networkmonitor.domain.usecas.GetPagedNetworkLogsUseCase
import rahul.lohra.networkmonitor.domain.usecas.ShareUseCase

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