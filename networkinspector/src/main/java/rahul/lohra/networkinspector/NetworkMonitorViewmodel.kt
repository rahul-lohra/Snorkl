package rahul.lohra.networkinspector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import rahul.lohra.networkinspector.ui.UiInitial
import rahul.lohra.networkinspector.ui.UiState
import rahul.lohra.networkinspector.ui.UiSuccess

class NetworkMonitorViewmodel: ViewModel() {
    companion object {
        const val KEY = "NetworkMonitorViewmodel"
    }
    private val _networkUiList =
        MutableStateFlow<UiState<List<NetworkListItem>>>(UiInitial())
    val networkUiList: StateFlow<UiState<List<NetworkListItem>>> = _networkUiList

    private val _detailScreenData = MutableStateFlow<UiState<NetworkData>>(UiInitial())
    val detailScreenData: SharedFlow<UiState<NetworkData>> = _detailScreenData

    fun setDetailScreenData(networkData: NetworkData) {
        viewModelScope.launch {
            _detailScreenData.emit(UiSuccess(networkData))
        }
    }
}