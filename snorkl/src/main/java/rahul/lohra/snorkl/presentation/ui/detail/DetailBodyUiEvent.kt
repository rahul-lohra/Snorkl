package rahul.lohra.snorkl.presentation.ui.detail

sealed class DetailBodyUiEvent {
    data class OnGetDetail(val id:String) : DetailBodyUiEvent()
}
