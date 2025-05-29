package rahul.lohra.snorkl.presentation.ui.detail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.Dispatchers
import rahul.lohra.snorkl.domain.usecas.ClearDataUseCase
import rahul.lohra.snorkl.domain.usecas.GetPagedNetworkLogsUseCase
import rahul.lohra.snorkl.domain.usecas.RequestResponseRetrieverUseCase
import rahul.lohra.snorkl.domain.usecas.ShareUseCase

class DetailViewmodelFactory (
    private val useCase: RequestResponseRetrieverUseCase,
    private val clearDataUseCase: ClearDataUseCase,
    private val shareUseCase: ShareUseCase,
    private val id: String,
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailViewmodel(
            useCase,
            clearDataUseCase,
            shareUseCase,
            id,
            Dispatchers.IO,
            Dispatchers.Default
        ) as T
    }
}