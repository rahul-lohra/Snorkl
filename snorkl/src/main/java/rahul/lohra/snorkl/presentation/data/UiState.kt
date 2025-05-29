package rahul.lohra.snorkl.presentation.data

sealed class UiState<T>
class UiInitial<T> : UiState<T>()
class UiSuccess<T>(val data: T) : UiState<T>()
class UiLoading<T> : UiState<T>()
class UiFail<T> : UiState<T>()