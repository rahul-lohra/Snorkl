package rahul.lohra.networkmonitor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import rahul.lohra.networkmonitor.domain.usecas.GetPagedNetworkLogsUseCase

class NetworkMonitorViewmodelFactory (private val useCase: GetPagedNetworkLogsUseCase
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NetworkMonitorViewmodel(useCase) as T
    }
}