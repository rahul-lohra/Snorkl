package rahul.lohra.networkmonitor.presentation.ui.detail

sealed class DetailBodyUiEvent {
    data class OnGetDetail(val id:String) : DetailBodyUiEvent()
}
