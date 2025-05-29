package rahul.lohra.snorkl.presentation.viewmodel

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
import rahul.lohra.snorkl.NetworkListItem
import rahul.lohra.snorkl.data.NetworkData
import rahul.lohra.snorkl.domain.usecas.ClearDataUseCase
import rahul.lohra.snorkl.domain.usecas.ExportData
import rahul.lohra.snorkl.domain.usecas.GetPagedNetworkLogsUseCase
import rahul.lohra.snorkl.domain.usecas.ShareUseCase
import rahul.lohra.snorkl.network.NetworkProvider
import rahul.lohra.snorkl.presentation.data.UiInitial
import rahul.lohra.snorkl.presentation.data.UiState
import rahul.lohra.snorkl.presentation.data.UiSuccess
import rahul.lohra.snorkl.presentation.ui.SharableDeletable
import rahul.lohra.snorkl.presentation.ui.home.HomeToolbarUiEvent

class NetworkMonitorViewmodel (
    private val useCase: GetPagedNetworkLogsUseCase,
    private val clearDataUseCase: ClearDataUseCase,
    private val shareUseCase: ShareUseCase,
    private val ioDispatcher: CoroutineDispatcher,
    private val defaultDispatcher: CoroutineDispatcher,
): ViewModel(), SharableDeletable {
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
    override val shareIntentFlow : Flow<ExportData> = _shareIntentFlow

    private val _exportFromDeviceFlow = MutableSharedFlow<Pair<ExportData, ExportData>>()
    override val exportFromDeviceFlow : Flow<Pair<ExportData, ExportData>> = _exportFromDeviceFlow


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
                    clearDataUseCase.clearAll()
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
            }is HomeToolbarUiEvent.OnExportFromDeviceClick -> {
                viewModelScope.launch(ioDispatcher) {
                    val exportData1 = shareUseCase.shareAllNetworkLogs(true)
                    val exportData2 =shareUseCase.shareAllNetworkLogs(false)
                    _exportFromDeviceFlow.emit(Pair(exportData1, exportData2))
                }
            }
        }
    }
}

