package rahul.lohra.networkmonitor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import rahul.lohra.networkmonitor.network.NetworkProvider
import rahul.lohra.networkmonitor.NetworkListItem
import rahul.lohra.networkmonitor.data.NetworkData
import rahul.lohra.networkmonitor.domain.usecas.GetPagedNetworkLogsUseCase
import rahul.lohra.networkmonitor.presentation.data.UiInitial
import rahul.lohra.networkmonitor.presentation.data.UiState
import rahul.lohra.networkmonitor.presentation.data.UiSuccess

class NetworkMonitorViewmodel (getPagedLogs: GetPagedNetworkLogsUseCase): ViewModel() {
    companion object {
        const val KEY = "NetworkMonitorViewmodel"
    }
    private val _networkUiList =
        MutableStateFlow<UiState<List<NetworkListItem>>>(UiInitial())
    val networkUiList: StateFlow<UiState<List<NetworkListItem>>> = _networkUiList

    private val _detailScreenData = MutableStateFlow<UiState<NetworkData>>(UiInitial())
    val detailScreenData: SharedFlow<UiState<NetworkData>> = _detailScreenData

    val allUiNetworkLogs: Flow<PagingData<NetworkListItem>> = getPagedLogs()
        .cachedIn(viewModelScope)

    fun setDetailScreenData(networkData: NetworkData) {
        viewModelScope.launch {
            _detailScreenData.emit(UiSuccess(networkData))
        }
    }

    fun setDetailScreenData(networkListItem: NetworkListItem) {
        viewModelScope.launch {
//            _detailScreenData.emit(UiSuccess(networkData))
        }
    }

    fun makeGetRequest() {
        viewModelScope.launch {
            NetworkProvider.getApiService().getHipsterIpsum()
        }
    }
}