package com.schuetz.agents

import androidx.compose.foundation.background
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
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

val exampleState = ChatUiState(
    initialMessages = listOf(
        Message("Hello!", Author.Me),
        Message("hi!", Author.Agent),
        Message("how are you doing?", Author.Me),
        Message("I'm doing great, thanks!", Author.Agent),
        Message("I'm doing great, thanks!", Author.Me),
        Message("I'm doing great, thanks!", Author.Agent),
        Message("I'm doing great, thanks!", Author.Me),
        Message("I'm doing great, thanks!", Author.Agent),
        Message("I'm doing great, thanks!", Author.Me),
        Message("I'm doing great, thanks!", Author.Agent),
        Message("I'm doing great, thanks!", Author.Me),
        Message("I'm doing great, thanks!", Author.Agent),
        Message("I'm doing great, thanks!", Author.Me),
    ),
)

@Composable
@Preview
fun App() {
    MaterialTheme {
        Chat(state = exampleState)
    }
}

@Composable
private fun Chat(state: ChatUiState) {
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    Column(modifier = Modifier.fillMaxSize()) {
        MessageList(
            modifier = Modifier.weight(1f),
            messages = state.messages,
            listState = listState
        )
        UserInput(sendMessage = { message ->
            state.addMessage(message)
            println("sent!")

            scope.launch {
                listState.animateScrollToItem(state.messages.lastIndex)
            }
        })
    }
}

@Composable
private fun UserInput(sendMessage: (Message) -> Unit) {
    var textState by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }

    Row(
        Modifier
            .background(Color.Blue).fillMaxWidth()
    ) {
        BasicTextField(
            value = textState,
            onValueChange = { textState = it },
            modifier = Modifier
                .padding(start = 32.dp)
                .height(48.dp)
                .weight(1f),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Send,
            ),
            keyboardActions = KeyboardActions {
                println("in keyboard actions!")
            },
            maxLines = 1,
            cursorBrush = SolidColor(LocalContentColor.current),
            textStyle = LocalTextStyle.current.copy(color = LocalContentColor.current),
        )

        Button(
            modifier = Modifier.height(36.dp).width(100.dp),
            onClick = {
                sendMessage(Message(textState.text, Author.Me))
                textState = TextFieldValue("")
            },
            contentPadding = PaddingValues(0.dp),
        ) {
            Text(
                "Send",
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
    }
}

@Composable
fun MessageList(
    modifier: Modifier = Modifier,
    messages: List<Message>,
    listState: LazyListState,
) {
    LazyColumn(
        modifier = modifier, state = listState,
    ) {

        items(items = messages) { item ->
            when (item.author) {
                Author.Agent -> MessageView(message = item)
                Author.Me -> MessageBubble(message = item)
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun MessageBubble(message: Message) {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            color = MaterialTheme.colorScheme.primary,
            shape = MessageBubbleShape,
        ) {
            MessageView(message = message)
        }
    }
}

private val MessageBubbleShape = RoundedCornerShape(20.dp, 4.dp, 20.dp, 20.dp)

@Composable
private fun MessageView(message: Message) {
    Text(
        text = message.text,
        modifier = Modifier.padding(16.dp),
    )
}

data class Message(val text: String, val author: Author)

class ChatUiState(initialMessages: List<Message>) {
    private val _messages: MutableList<Message> = initialMessages.toMutableStateList()
    val messages: List<Message> = _messages

    fun addMessage(msg: Message) {
        _messages.add(msg)
    }
}

sealed interface Author {
    data object Me : Author
    data object Agent : Author
}
