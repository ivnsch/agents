package com.schuetz.agents.agents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil3.compose.AsyncImage
import com.schuetz.agents.domain.ConnectableProvider
import com.schuetz.agents.spaces.AddAgentInputs

@Composable
fun AddAgentDialog(
    avatarUrl: String,
    llmModels: (ConnectableProvider) -> List<String>,
    onAddAgent: (AddAgentInputs) -> Unit,
    onDismiss: () -> Unit,
    regenerateAvatar: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf<String?>(null) }
    var authToken by remember { mutableStateOf<String?>(null) }
    var llm by remember { mutableStateOf(ConnectableProvider.HUGGING_FACE) }
    var model by remember { mutableStateOf("") }

    LaunchedEffect(llm) {
        model = llmModels(llm).firstOrNull() ?: ""
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .imePadding(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Add agent",
                    fontWeight = Bold,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                )
                AvatarImageBox(avatarUrl, regenerateAvatar)
                Text(text = "Name:")
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Text(text = "Tagline:")
                TextField(
                    value = description ?: "",
                    onValueChange = { description = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Text(text = "LLM:")
                LLMSelectionDropdown(llm, onLlmChange = {
                    llm = it
                })
                if (hasApiKey(llm)) {
                    Row {
                        Text(text = "Access token")
                        apiKeyLink(llm)?.let {
                            Link(it, " (how to get one)")
                        }
                        Text(text = ":")
                    }
                    TextField(
                        value = authToken ?: "",
                        onValueChange = { authToken = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
                if (hasModel(llm)) {
                    Text(text = "Model:")
                    LLMModelSelectionDropdown(
                        models = llmModels(llm),
                        selectedModel = model,
                        onModelChange = {
                            model = it
                        })
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    TextButton(
                        onClick = {
                            onAddAgent(
                                AddAgentInputs(
                                    name,
                                    description,
                                    llm,
                                    model,
                                    authToken,
                                    avatarUrl
                                )
                            )
                            onDismiss()
                        },
                        enabled = name.isNotBlank()
                    ) {
                        Text("OK")
                    }
                }
            }
        }
    }
}

@Composable
fun Link(url: String, text: String) = Text(
    buildAnnotatedString {
        withLink(
            LinkAnnotation.Url(
                url,
                TextLinkStyles(style = SpanStyle(color = Color.Blue))
            )
        ) {
            append(text)
        }
    }
)

@Composable
fun LLMSelectionDropdown(
    selectedLLM: ConnectableProvider,
    onLlmChange: (ConnectableProvider) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(toTextLabel(selectedLLM))
        IconButton(onClick = { expanded = !expanded }) {
            Icon(Icons.Default.MoreVert, contentDescription = "More options")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            ConnectableProvider.entries.forEach { provider ->
                LLMDropdownItem(provider, onClick = {
                    onLlmChange(it)
                    expanded = false
                })
            }
        }
    }
}

@Composable
private fun LLMDropdownItem(llm: ConnectableProvider, onClick: (ConnectableProvider) -> Unit) =
    DropdownMenuItem(
        text = { Text(toTextLabel(llm)) },
        onClick = { onClick(llm) }
    )

private fun hasApiKey(llm: ConnectableProvider): Boolean = when (llm) {
    ConnectableProvider.HUGGING_FACE -> true
    ConnectableProvider.OPEN_AI -> true
    ConnectableProvider.DUMMY -> false
}

private fun hasModel(llm: ConnectableProvider): Boolean = when (llm) {
    ConnectableProvider.HUGGING_FACE -> true
    ConnectableProvider.OPEN_AI -> true
    ConnectableProvider.DUMMY -> false
}

private fun toTextLabel(llm: ConnectableProvider): String = when (llm) {
    ConnectableProvider.HUGGING_FACE -> "Hugging Face"
    ConnectableProvider.OPEN_AI -> "OpenAI"
    ConnectableProvider.DUMMY -> "Dummy"
}

private fun apiKeyLink(llm: ConnectableProvider): String? = when (llm) {
    ConnectableProvider.HUGGING_FACE -> "https://huggingface.co/docs/hub/en/security-tokens"
    ConnectableProvider.OPEN_AI -> "https://help.openai.com/en/articles/4936850-where-do-i-find-my-openai-api-key"
    ConnectableProvider.DUMMY -> null
}

@Composable
fun LLMModelSelectionDropdown(
    models: List<String>,
    selectedModel: String,
    onModelChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(selectedModel)
        IconButton(onClick = { expanded = !expanded }) {
            Icon(Icons.Default.MoreVert, contentDescription = "More options")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            models.forEach { model ->
                LLMModelDropdownItem(model, onClick = {
                    onModelChange(it)
                    expanded = false
                })
            }
        }
    }
}

@Composable
private fun LLMModelDropdownItem(model: String, onClick: (String) -> Unit) =
    DropdownMenuItem(
        text = { Text(model) },
        onClick = { onClick(model) }
    )


@Composable
fun AvatarImageBox(avatarUrl: String, regenerateAvatar: () -> Unit) = Box(
    modifier = Modifier.fillMaxWidth(),
    contentAlignment = Alignment.Center
) {
    // this box is used to center the image and button horizontally
    Box {
        AsyncImage(
            model = avatarUrl,
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(12.dp))
        )
        // this box allows to offset the bottom outside of the image
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 6.dp, y = 6.dp)
        ) {
            IconButton(
                onClick = regenerateAvatar,
                modifier = Modifier
                    .size(30.dp)
                    .background(Color.Blue, CircleShape)
                    .clip(CircleShape)
            ) {
                Icon(
                    Icons.Default.Refresh,
                    contentDescription = "Regenerate",
                    tint = Color.White,
                )
            }
        }
    }
}
