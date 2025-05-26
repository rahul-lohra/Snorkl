package rahul.lohra.networkmonitor.presentation.viewmodel

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import rahul.lohra.networkmonitor.network.NetworkProvider
import rahul.lohra.networkmonitor.NetworkListItem
import rahul.lohra.networkmonitor.data.NetworkData
import rahul.lohra.networkmonitor.domain.usecas.ExportData
import rahul.lohra.networkmonitor.domain.usecas.GetPagedNetworkLogsUseCase
import rahul.lohra.networkmonitor.domain.usecas.ShareUseCase
import rahul.lohra.networkmonitor.presentation.data.UiInitial
import rahul.lohra.networkmonitor.presentation.data.UiState
import rahul.lohra.networkmonitor.presentation.data.UiSuccess
import rahul.lohra.networkmonitor.presentation.ui.home.HomeToolbarUiEvent

class NetworkMonitorViewmodel (
    private val useCase: GetPagedNetworkLogsUseCase,
    private val shareUseCase: ShareUseCase,
    private val ioDispatcher: CoroutineDispatcher,
    private val defaultDispatcher: CoroutineDispatcher,
): ViewModel() {
    companion object {
        const val KEY = "NetworkMonitorViewmodel"
    }

    private val _networkUiList =
        MutableStateFlow<UiState<List<NetworkListItem>>>(UiInitial())
    val networkUiList: StateFlow<UiState<List<NetworkListItem>>> = _networkUiList

    private val _detailScreenData = MutableStateFlow<UiState<NetworkData>>(UiInitial())
    val detailScreenData: SharedFlow<UiState<NetworkData>> = _detailScreenData

    val allUiNetworkLogs: Flow<PagingData<NetworkListItem>> = useCase.getPagedLogs()
        .cachedIn(viewModelScope)

    val httpNetworkLogs: Flow<PagingData<NetworkListItem>> = useCase.getFilteredNetworkLogs { it.networkType == "rest" }
        .cachedIn(viewModelScope)

    val wsNetworkLogs: Flow<PagingData<NetworkListItem>> = useCase.getFilteredNetworkLogs { it.networkType == "ws" }
        .cachedIn(viewModelScope)

    private val _shareIntentFlow = MutableSharedFlow<ExportData>()
    val shareIntentFlow : Flow<ExportData> = _shareIntentFlow

    private val _exportFromDeviceFlow = MutableSharedFlow<Pair<ExportData, ExportData>>()
    val exportFromDeviceFlow : Flow<Pair<ExportData, ExportData>> = _exportFromDeviceFlow


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

    fun onToolbarUiEvent(event: HomeToolbarUiEvent){
        when (event) {
            is HomeToolbarUiEvent.OnSearchClick -> {
                // Handle search click
            }
            is HomeToolbarUiEvent.OnDeleteClick -> {
                viewModelScope.launch(ioDispatcher) {
                    useCase.clearAll()
                }
            }
            is HomeToolbarUiEvent.OnShareJsonClick -> {
                viewModelScope.launch(ioDispatcher) {
                    _shareIntentFlow.emit(shareUseCase.shareAllNetworkLogs(true))
                }
            }
            is HomeToolbarUiEvent.OnShareTextClick -> {
                viewModelScope.launch(ioDispatcher) {
                    val intent = shareUseCase.shareAllNetworkLogs(false)
                    _shareIntentFlow.emit(intent)
                }
            }is HomeToolbarUiEvent.onExportFromDeviceClick -> {
                viewModelScope.launch(ioDispatcher) {
                    val exportData1 = shareUseCase.shareAllNetworkLogs(true)
                    val exportData2 =shareUseCase.shareAllNetworkLogs(false)
                    _exportFromDeviceFlow.emit(Pair(exportData1, exportData2))
                }
            }
        }
    }
}

