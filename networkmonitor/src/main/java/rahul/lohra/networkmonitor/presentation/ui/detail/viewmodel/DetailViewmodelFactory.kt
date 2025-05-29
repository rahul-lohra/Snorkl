package rahul.lohra.networkmonitor.presentation.ui.detail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.Dispatchers
import rahul.lohra.networkmonitor.domain.usecas.GetPagedNetworkLogsUseCase
import rahul.lohra.networkmonitor.domain.usecas.RequestResponseRetrieverUseCase
import rahul.lohra.networkmonitor.domain.usecas.ShareUseCase

class DetailViewmodelFactory (
    private val useCase: RequestResponseRetrieverUseCase,
    private val shareUseCase: ShareUseCase,
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailViewmodel(useCase, shareUseCase, Dispatchers.IO, Dispatchers.Default) as T
    }
}