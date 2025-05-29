package rahul.lohra.networkmonitor.presentation.ui.home

sealed class HomeToolbarUiEvent {
    object OnSearchClick : HomeToolbarUiEvent()
    object OnDeleteClick : HomeToolbarUiEvent()
    object OnShareJsonClick : HomeToolbarUiEvent()
    object OnShareTextClick : HomeToolbarUiEvent()
    object OnExportFromDeviceClick : HomeToolbarUiEvent()
}
