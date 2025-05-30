package com.schuetz.agents.chat

import agents.composeapp.generated.resources.Res
import agents.composeapp.generated.resources.sun
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.schuetz.agents.WeatherClientReport
import com.schuetz.agents.common.ErrorDialog
import com.schuetz.agents.domain.Message
import com.schuetz.agents.domain.StructuredMessage
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import org.jetbrains.compose.resources.painterResource
import kotlin.math.pow
import kotlin.math.roundToInt

@Composable
fun Chat(viewModel: ChatViewModel) {
    val listState = rememberLazyListState()
    val focusManager = LocalFocusManager.current

    val messages by viewModel.messages
        .map { it.reversed() }
        .collectAsState(initial = emptyList())
    val isWaitingForReply by viewModel.isWaitingForReply
        .collectAsState(initial = false)
    val errorMessage = viewModel.errorMessage
        .collectAsState(initial = null)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }) {
        MessageList(
            modifier = Modifier.weight(1f),
            messages = messages,
            listState = listState,
            isWaitingForReply = isWaitingForReply
        )
        UserInput(sendMessage = { message ->
            viewModel.sendMessage(message)
        })
    }

    errorMessage.value?.let { error ->
        ErrorDialog(
            message = error,
            onDismiss = { viewModel.clearError() }
        )
    }
}

@Composable
private fun UserInput(sendMessage: (String) -> Unit) {
    var textState by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }

    val sendMessageAndClear = {
        sendMessage(textState.text)
        textState = TextFieldValue("")
    }

    Surface(tonalElevation = 1.dp) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BasicTextField(
                value = textState,
                onValueChange = { textState = it },
                modifier = Modifier
                    .padding(16.dp)
                    .height(80.dp)
                    .weight(1f),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Send,
                ),
                keyboardActions = KeyboardActions {
                    sendMessageAndClear()
                },
                maxLines = 1,
                cursorBrush = SolidColor(LocalContentColor.current),
                textStyle = LocalTextStyle.current.copy(color = LocalContentColor.current),
            )

            SendButton(textState.text.isNotBlank(), onClick = {
                sendMessageAndClear()
            })
        }
    }
}

@Composable
private fun SendButton(isEnabled: Boolean, onClick: () -> Unit) {
    val border = if (!isEnabled) {
        BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
        )
    } else {
        null
    }

    Button(
        modifier = Modifier.height(36.dp).width(100.dp),
        onClick = {
            onClick()
        },
        enabled = isEnabled,
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            disabledContainerColor = Color.Transparent,
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
        ),
        border = border,
    ) {
        Text(
            "Send",
            modifier = Modifier.padding(horizontal = 16.dp),
        )
    }
}

@Composable
private fun MessageList(
    modifier: Modifier = Modifier,
    messages: List<Message>,
    listState: LazyListState,
    isWaitingForReply: Boolean = false
) {
    LazyColumn(
        modifier = modifier,
        state = listState,
        reverseLayout = true
    ) {
        if (isWaitingForReply) {
            item {
                ThinkingBubble()
                MessageSpacer()
            }
        }
        items(items = messages) { item ->
            if (item.author.isMe) {
                MyMessageBubble(content = { BubbleContent(item.content) })
            } else {
                Row {
                    AsyncImage(
                        model = item.author.avatarUrl,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp)
                    )
                    OtherMessageBubble(content = { BubbleContent(item.content) })
                }
            }
            MessageSpacer()
        }
    }
}

@Composable
private fun BubbleContent(message: StructuredMessage) {
    val padding = Modifier.padding(16.dp)
    when (message) {
        is StructuredMessage.Message -> MessageView(padding, message.text)
        is StructuredMessage.WeatherReport -> Box(padding) {
            WeatherWidget(message.report)
        }
    }
}

@Composable
fun WeatherWidget(report: WeatherClientReport) {
    val temperature = "${report.temperature.roundTo(1)}°"
    val humidity = "${report.humidity.roundTo(0)}%"

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Image(
            painter = painterResource(Res.drawable.sun),
            contentDescription = null,
            modifier = Modifier.size(64.dp)
        )
        Text(
            text = temperature,
            fontSize = 48.sp,
            fontWeight = FontWeight.Light,
            color = Color.White
        )
        Text(
            text = humidity,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
    }
}

private fun Double.roundTo(decimals: Int): String {
    val factor = 10.0.pow(decimals)
    return ((this * factor).roundToInt() / factor).toString()
}

@Composable
private fun ThinkingBubble() {
    var dots by remember { mutableStateOf(".") }
    LaunchedEffect(Unit) {
        while (currentCoroutineContext().isActive) {
            delay(500)
            dots = when (dots.length) {
                1 -> ".."
                2 -> "..."
                else -> "."
            }
        }
    }
    OtherMessageView(dots, null)
}

@Composable
private fun MessageSpacer() = Spacer(modifier = Modifier.height(4.dp))

@Composable
private fun OtherMessageView(message: String, avatarUrl: String?) = MessageView(
    message = message,
    modifier = Modifier.padding(16.dp, 0.dp, 0.dp, 0.dp),
)

@Composable
private fun MyMessageBubble(content: @Composable () -> Unit) =
    MessageBubble(
        content = content,
        shape = MyMessageBubbleShape,
        arrangement = Arrangement.End,
        color = MaterialTheme.colorScheme.primary
    )

@Composable
private fun OtherMessageBubble(content: @Composable () -> Unit) =
    MessageBubble(
        content = content,
        shape = OtherMessageBubbleShape,
        arrangement = Arrangement.Start,
        color = MaterialTheme.colorScheme.secondary
    )

@Composable
private fun MessageBubble(
    content: @Composable () -> Unit,
    shape: Shape,
    arrangement: Arrangement.Horizontal,
    color: Color,
) {
    Row(
        horizontalArrangement = arrangement,
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            color = color,
            shape = shape,
            modifier = Modifier.padding(16.dp)
        ) {
            content()
        }
    }
}

private val MyMessageBubbleShape = RoundedCornerShape(20.dp, 4.dp, 20.dp, 20.dp)
private val OtherMessageBubbleShape = RoundedCornerShape(4.dp, 20.dp, 20.dp, 20.dp)

@Composable
private fun MessageView(modifier: Modifier, message: String) {
    val selectionColors = TextSelectionColors(
        handleColor = Color.Blue,
        backgroundColor = Color.Blue
    )

    CompositionLocalProvider(LocalTextSelectionColors provides selectionColors) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            SelectionContainer {
                Text(text = message, modifier = modifier)
            }
        }
    }
}

