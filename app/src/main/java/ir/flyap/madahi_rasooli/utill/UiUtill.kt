package ir.flyap.madahi_rasooli.utill

interface MessageType {
    fun setMessageByToast(message: Any, messageState: MessageState = MessageState.Error)
    fun setMessageBySnackbar(message: Any, messageState: MessageState = MessageState.Error)
}

sealed interface MessageState {
    object Error : MessageState
    object Success : MessageState
    object Info : MessageState
}


data class UiComponent(
    val message: Any?,
    val widgetType: WidgetType,
    val messageState: MessageState,
)

sealed interface WidgetType {
    object Toast : WidgetType
    object Snackbar : WidgetType
}

sealed class Progress {

    object Loading : Progress()

    object Idle : Progress()

}
object LoadingKey {
    const val DEFAULT = "Default"
    const val NEXT_PAGE = "Next Page"
}