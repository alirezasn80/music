package ir.flyap.madahi_rasooli_golchin.ui.common

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import ir.flyap.madahi_rasooli_golchin.R
import ir.flyap.madahi_rasooli_golchin.ui.theme.LargeSpacer
import ir.flyap.madahi_rasooli_golchin.ui.theme.MediumSpacer
import ir.flyap.madahi_rasooli_golchin.ui.theme.dimension
import ir.flyap.madahi_rasooli_golchin.utill.ContentWithMessageBar
import ir.flyap.madahi_rasooli_golchin.utill.MessageBarState
import ir.flyap.madahi_rasooli_golchin.utill.MessageState
import ir.flyap.madahi_rasooli_golchin.utill.Progress
import ir.flyap.madahi_rasooli_golchin.utill.UiComponent
import ir.flyap.madahi_rasooli_golchin.utill.WidgetType
import ir.flyap.madahi_rasooli_golchin.utill.isOnline
import ir.flyap.madahi_rasooli_golchin.utill.rememberMessageBarState
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun UI(
    progress: Progress? = Progress.Idle,
    uiComponent: SharedFlow<UiComponent>? = null,
    isNoData: Boolean = false,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val messageBarState = rememberMessageBarState()
    var isConnected by remember { mutableStateOf(isOnline(context)) }

    ContentWithMessageBar(messageBarState = messageBarState) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                content()

                if (progress is Progress.Loading) BlurLayout()

                if (uiComponent != null) HandleUiComponents(uiComponent, messageBarState)


                if (isNoData && progress is Progress.Idle) DefaultNoDataLayout()


            }

    }


}


@Composable
private fun DefaultNoDataLayout() {

    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(id = R.string.data_is_not_exist),
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 100.dp)
        )
    }


}


@Composable
private fun OfflineLayout(onRefresh: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(100.dp)
            )
            Text(text = "شما آفلاین هستید", style = MaterialTheme.typography.titleLarge)
            MediumSpacer()
            Text(
                text = "لطفا از اتصال خود به اینترنت مطمعن شوید و بعد از آن روی تلاش مجدد کلیک کنید",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = dimension.medium)
            )
            LargeSpacer()
            //todo()
        }
    }
}


@Composable
private fun HandleUiComponents(
    uiComponent: SharedFlow<UiComponent>?,
    state: MessageBarState,
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = uiComponent) {

        uiComponent?.collect { uiComponent ->

            val message = when (uiComponent.message) {

                is String -> uiComponent.message

                is Int -> context.getString(uiComponent.message)

                else -> return@collect
            }

            when (uiComponent.widgetType) {

                WidgetType.Snackbar -> {
                    when (uiComponent.messageState) {

                        MessageState.Error -> {
                            state.addError(exception = Exception(message))
                        }

                        MessageState.Success, MessageState.Info -> {
                            state.addSuccess(message)
                        }
                    }
                }

                WidgetType.Toast -> Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
        }

    }
}

@Composable
fun BaseTextButton(
    modifier: Modifier = Modifier,
    text: Any,
    contentColor: Color = MaterialTheme.colorScheme.primary,
    fontSize: TextUnit = TextUnit.Unspecified,
    onclick: () -> Unit,
) {
    val textButton = when (text) {
        is Int -> stringResource(id = text)
        is String -> text
        else -> ""
    }

    TextButton(onClick = onclick, modifier = modifier) {
        Text(
            text = textButton,
            style = MaterialTheme.typography.titleSmall.copy(color = contentColor),
            fontSize = fontSize
        )

    }
}