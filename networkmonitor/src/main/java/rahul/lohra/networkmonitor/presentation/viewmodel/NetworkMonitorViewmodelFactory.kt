package rahul.lohra.networkmonitor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import rahul.lohra.networkmonitor.domain.usecas.GetPagedNetworkLogsUseCase
import rahul.lohra.networkmonitor.domain.usecas.ShareUseCase

class NetworkMonitorViewmodelFactory (
    private val useCase: GetPagedNetworkLogsUseCase,
    private val shareUseCase: ShareUseCase,
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NetworkMonitorViewmodel(useCase, shareUseCase, Dispatchers.IO, Dispatchers.Default) as T
    }
}