package rahul.lohra.snorkl.presentation.ui.detail


sealed class ToolbarUiEvent(val id: String) {
    class OnSearchClick(id: String) : ToolbarUiEvent(id)
    class OnDeleteClick(id: String) : ToolbarUiEvent(id)
    class OnShareJsonClick(id: String) : ToolbarUiEvent(id)
    class OnShareTextClick(id: String) : ToolbarUiEvent(id)
    class OnExportFromDeviceClick(id: String) : ToolbarUiEvent(id)
}
