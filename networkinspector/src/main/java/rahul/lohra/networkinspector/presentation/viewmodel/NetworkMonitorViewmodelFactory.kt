package rahul.lohra.networkinspector.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.StateFlow
import rahul.lohra.networkinspector.domain.usecas.GetPagedNetworkLogsUseCase

class NetworkMonitorViewmodelFactory (private val useCase: GetPagedNetworkLogsUseCase
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NetworkMonitorViewmodel(useCase) as T
    }
}