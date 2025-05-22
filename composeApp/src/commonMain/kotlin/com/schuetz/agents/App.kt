package com.schuetz.agents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            MessageList(
                modifier = Modifier.weight(1f),
                listOf(
                    Message("hello"),
                    Message("hello back"),
                    Message("How are you"),
                    Message("I'm fine")
                )
            )
            UserInput()
        }
    }
}

@Composable
private fun UserInput() {
    var textState by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }
    Box(
        Modifier
            .background(Color.Blue)
    ) {
        BasicTextField(
            value = textState,
            onValueChange = { textState = it },
            modifier = Modifier
                .padding(start = 32.dp)
                .align(Alignment.CenterStart)
                .height(48.dp)
                .fillMaxWidth(),
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
    }
}

@Composable
fun MessageList(
    modifier: Modifier = Modifier,
    messages: List<Message>,
) {
    LazyColumn(modifier = modifier) {
        items(items = messages, key = { it.text }) { item ->
            MessageView(
                message = item,
            )
        }
    }
}

@Composable
private fun MessageView(
    message: Message,
) {
    Text(
        text = message.text,
    )
}

data class Message(val text: String)
