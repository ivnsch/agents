package com.schuetz.agents.chat

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@Composable
fun Chat(viewModel: ChatViewModel) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val messages by viewModel.messages
        .map { it.reversed() }
        .collectAsState(initial = emptyList())

    Column(modifier = Modifier.fillMaxSize()) {
        MessageList(
            modifier = Modifier.weight(1f),
            messages = messages,
            listState = listState
        )
        UserInput(sendMessage = { message ->
            scope.launch {
                viewModel.addMessage(message)
                println("sent!")
            }
        })
    }
}

@Composable
private fun UserInput(sendMessage: (Message) -> Unit) {
    var textState by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }

    val sendMessageAndClear = {
        sendMessage(Message(textState.text, Author.Me))
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
) {
    LazyColumn(
        modifier = modifier,
        state = listState,
        reverseLayout = true
    ) {

        items(items = messages) { item ->
            when (item.author) {
                Author.Agent -> MessageView(
                    message = item,
                    modifier = Modifier.padding(16.dp, 0.dp, 0.dp, 0.dp)
                )

                Author.Me -> MessageBubble(message = item)
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun MessageBubble(message: Message) {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            color = MaterialTheme.colorScheme.primary,
            shape = MessageBubbleShape,
            modifier = Modifier.padding(16.dp)
        ) {
            MessageView(
                message = message,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

private val MessageBubbleShape = RoundedCornerShape(20.dp, 4.dp, 20.dp, 20.dp)

@Composable
private fun MessageView(modifier: Modifier, message: Message) {
    Text(text = message.text, modifier = modifier)
}

