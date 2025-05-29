package rahul.lohra.networkmonitor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import rahul.lohra.networkmonitor.domain.usecas.ClearDataUseCase
import rahul.lohra.networkmonitor.domain.usecas.GetPagedNetworkLogsUseCase
import rahul.lohra.networkmonitor.domain.usecas.ShareUseCase

class NetworkMonitorViewmodelFactory(
    private val useCase: GetPagedNetworkLogsUseCase,
    private val clearDataUseCase: ClearDataUseCase,
    private val shareUseCase: ShareUseCase,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NetworkMonitorViewmodel(
            useCase,
            clearDataUseCase,
            shareUseCase,
            Dispatchers.IO,
            Dispatchers.Default
        ) as T
    }
}