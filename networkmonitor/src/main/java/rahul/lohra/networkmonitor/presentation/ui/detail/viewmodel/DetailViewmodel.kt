package rahul.lohra.networkmonitor.presentation.ui.detail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import rahul.lohra.networkmonitor.NetworkListItem
import rahul.lohra.networkmonitor.data.local.entities.NetworkType
import rahul.lohra.networkmonitor.data.mappers.toWebSocketLogEntry
import rahul.lohra.networkmonitor.domain.usecas.ExportData
import rahul.lohra.networkmonitor.domain.usecas.RequestResponseRetrieverUseCase
import rahul.lohra.networkmonitor.domain.usecas.ShareUseCase
import rahul.lohra.networkmonitor.network.NetworkProvider
import rahul.lohra.networkmonitor.presentation.data.UiInitial
import rahul.lohra.networkmonitor.presentation.data.UiLoading
import rahul.lohra.networkmonitor.presentation.data.UiState
import rahul.lohra.networkmonitor.presentation.data.UiSuccess
import rahul.lohra.networkmonitor.presentation.ui.detail.DetailBodyUiEvent
import rahul.lohra.networkmonitor.presentation.ui.detail.DetailScreenUiModel
import rahul.lohra.networkmonitor.presentation.ui.detail.toRestApiDetails
import rahul.lohra.networkmonitor.presentation.ui.detail.toWsDetails

class DetailViewmodel (
    private val useCase: RequestResponseRetrieverUseCase,
    private val shareUseCase: ShareUseCase,
    private val ioDispatcher: CoroutineDispatcher,
    private val defaultDispatcher: CoroutineDispatcher,
): ViewModel() {
    companion object {
        const val KEY = "DetailViewmodel"
    }

    private val _networkUiList =
        MutableStateFlow<UiState<List<NetworkListItem>>>(UiInitial())
    val networkUiList: StateFlow<UiState<List<NetworkListItem>>> = _networkUiList

    private val _detailScreenData = MutableStateFlow<UiState<DetailScreenUiModel>>(UiInitial())
    val detailScreenData: SharedFlow<UiState<DetailScreenUiModel>> = _detailScreenData

//    val allUiNetworkLogs: Flow<PagingData<NetworkListItem>> = useCase.getPagedLogs()
//        .cachedIn(viewModelScope)
//
//    val httpNetworkLogs: Flow<PagingData<NetworkListItem>> = useCase.getFilteredNetworkLogs { it.networkType == "rest" }
//        .cachedIn(viewModelScope)
//
//    val wsNetworkLogs: Flow<PagingData<NetworkListItem>> = useCase.getFilteredNetworkLogs { it.networkType == "ws" }
//        .cachedIn(viewModelScope)

    private val _shareIntentFlow = MutableSharedFlow<ExportData>()
    val shareIntentFlow : Flow<ExportData> = _shareIntentFlow

    private val _exportFromDeviceFlow = MutableSharedFlow<Pair<ExportData, ExportData>>()
    val exportFromDeviceFlow : Flow<Pair<ExportData, ExportData>> = _exportFromDeviceFlow

//    fun setDetailScreenData(networkData: NetworkData) {
//        viewModelScope.launch {
//            _detailScreenData.emit(UiSuccess(networkData))
//        }
//    }

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

    fun onDetailBodyUiEvent(event: DetailBodyUiEvent){
        when (event) {
            is DetailBodyUiEvent.OnGetDetail -> {
                if(_detailScreenData.value is UiInitial) {
                    viewModelScope.launch(ioDispatcher) {
                        _detailScreenData.emit(UiLoading())
                        val recordFlow = useCase.getRecord(event.id)
                        recordFlow.collectLatest {
                            val networkType = NetworkType.fromTitle(it.networkType)
                            val result = when (networkType) {
                                NetworkType.WEBSOCKET->  it.toWebSocketLogEntry().toWsDetails()
                                NetworkType.REST->  it.toRestApiDetails()
                                null -> {
                                    throw RuntimeException("Only two types of network are registered")
                                }
                            }
                            _detailScreenData.emit(UiSuccess(result))
                        }
                    }
                }
            }
        }
    }
}

