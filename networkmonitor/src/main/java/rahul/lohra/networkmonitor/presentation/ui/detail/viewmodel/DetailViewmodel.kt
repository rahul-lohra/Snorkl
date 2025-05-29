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
import rahul.lohra.networkmonitor.domain.usecas.ClearDataUseCase
import rahul.lohra.networkmonitor.domain.usecas.ExportData
import rahul.lohra.networkmonitor.domain.usecas.RequestResponseRetrieverUseCase
import rahul.lohra.networkmonitor.domain.usecas.ShareUseCase
import rahul.lohra.networkmonitor.network.NetworkProvider
import rahul.lohra.networkmonitor.presentation.data.UiInitial
import rahul.lohra.networkmonitor.presentation.data.UiLoading
import rahul.lohra.networkmonitor.presentation.data.UiState
import rahul.lohra.networkmonitor.presentation.data.UiSuccess
import rahul.lohra.networkmonitor.presentation.ui.SharableDeletable
import rahul.lohra.networkmonitor.presentation.ui.detail.DetailBodyUiEvent
import rahul.lohra.networkmonitor.presentation.ui.detail.DetailScreenUiModel
import rahul.lohra.networkmonitor.presentation.ui.detail.ToolbarUiEvent
import rahul.lohra.networkmonitor.presentation.ui.detail.toRestApiDetails
import rahul.lohra.networkmonitor.presentation.ui.detail.toWsDetails

class DetailViewmodel(
    private val useCase: RequestResponseRetrieverUseCase,
    private val clearDataUseCase: ClearDataUseCase,
    private val shareUseCase: ShareUseCase,
    private val id:String,
    private val ioDispatcher: CoroutineDispatcher,
    private val defaultDispatcher: CoroutineDispatcher,
) : ViewModel(), SharableDeletable {

    companion object {
        const val KEY = "DetailViewmodel"
    }

    private val _networkUiList =
        MutableStateFlow<UiState<List<NetworkListItem>>>(UiInitial())
    val networkUiList: StateFlow<UiState<List<NetworkListItem>>> = _networkUiList

    private val _detailScreenData = MutableStateFlow<UiState<DetailScreenUiModel>>(UiInitial())
    val detailScreenData: SharedFlow<UiState<DetailScreenUiModel>> = _detailScreenData

    private val _shareIntentFlow = MutableSharedFlow<ExportData>()
    override val shareIntentFlow: Flow<ExportData> = _shareIntentFlow

    private val _exportFromDeviceFlow = MutableSharedFlow<Pair<ExportData, ExportData>>()
    override val exportFromDeviceFlow: Flow<Pair<ExportData, ExportData>> = _exportFromDeviceFlow


    fun onToolbarUiEvent(event: ToolbarUiEvent) {
        when (event) {
            is ToolbarUiEvent.OnSearchClick -> {
                // Handle search click
            }

            is ToolbarUiEvent.OnDeleteClick -> {
                viewModelScope.launch(ioDispatcher) {
                    clearDataUseCase.delete(event.id)
                }
            }

            is ToolbarUiEvent.OnShareJsonClick -> {
                viewModelScope.launch(ioDispatcher) {
                    _shareIntentFlow.emit(shareUseCase.shareNetworkLog(event.id,true))
                }
            }

            is ToolbarUiEvent.OnShareTextClick -> {
                viewModelScope.launch(ioDispatcher) {
                    val intent = shareUseCase.shareNetworkLog(event.id,false)
                    _shareIntentFlow.emit(intent)
                }
            }

            is ToolbarUiEvent.OnExportFromDeviceClick -> {
                viewModelScope.launch(ioDispatcher) {
                    val exportData1 = shareUseCase.shareNetworkLog(event.id, true)
                    val exportData2 = shareUseCase.shareNetworkLog(event.id, false)
                    _exportFromDeviceFlow.emit(Pair(exportData1, exportData2))
                }
            }
        }
    }

    fun onDetailBodyUiEvent(event: DetailBodyUiEvent) {
        when (event) {
            is DetailBodyUiEvent.OnGetDetail -> {
                if (_detailScreenData.value is UiInitial) {
                    viewModelScope.launch(ioDispatcher) {
                        _detailScreenData.emit(UiLoading())
                        val recordFlow = useCase.getRecord(event.id)
                        recordFlow.collectLatest {
                            val networkType = NetworkType.fromTitle(it.networkType)
                            val result = when (networkType) {
                                NetworkType.WEBSOCKET -> it.toWebSocketLogEntry().toWsDetails()
                                NetworkType.REST -> it.toRestApiDetails()
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

