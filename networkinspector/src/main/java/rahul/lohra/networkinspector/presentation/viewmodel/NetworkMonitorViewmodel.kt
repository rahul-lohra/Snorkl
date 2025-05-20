package rahul.lohra.networkinspector.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import rahul.lohra.networkinspector.NetworkData
import rahul.lohra.networkinspector.NetworkListItem
import rahul.lohra.networkinspector.data.local.entities.NetworkEntity
import rahul.lohra.networkinspector.domain.usecas.GetPagedNetworkLogsUseCase
import rahul.lohra.networkinspector.presentation.data.UiInitial
import rahul.lohra.networkinspector.presentation.data.UiState
import rahul.lohra.networkinspector.presentation.data.UiSuccess

class NetworkMonitorViewmodel (private val getPagedLogs: GetPagedNetworkLogsUseCase): ViewModel() {
    companion object {
        const val KEY = "NetworkMonitorViewmodel"
    }
    private val _networkUiList =
        MutableStateFlow<UiState<List<NetworkListItem>>>(UiInitial())
    val networkUiList: StateFlow<UiState<List<NetworkListItem>>> = _networkUiList

    private val _detailScreenData = MutableStateFlow<UiState<NetworkData>>(UiInitial())
    val detailScreenData: SharedFlow<UiState<NetworkData>> = _detailScreenData

    val logs: Flow<PagingData<NetworkEntity>> = getPagedLogs()
        .cachedIn(viewModelScope)

    fun setDetailScreenData(networkData: NetworkData) {
        viewModelScope.launch {
            _detailScreenData.emit(UiSuccess(networkData))
        }
    }
}