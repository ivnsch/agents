package com.schuetz.agents.agents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil3.compose.AsyncImage
import com.schuetz.agents.domain.ConnectableProvider
import com.schuetz.agents.spaces.AddAgentInputs

@Composable
fun AddAgentDialog(
    avatarUrl: String,
    onAddAgent: (AddAgentInputs) -> Unit,
    onDismiss: () -> Unit,
    regenerateAvatar: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var authToken by remember { mutableStateOf("") }
    var llm by remember { mutableStateOf(ConnectableProvider.HUGGING_FACE) }

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
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "Huggingface LLM", fontWeight = Bold)
                AvatarImageBox(avatarUrl, regenerateAvatar)
                Text(text = "Agent name:")
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Text(text = "LLM:")
                LLMSelectionDropdown(llm, onLlmChange = {
                    llm = it
                })
                if (hasApiKey(llm)) {
                    Text(text = "API key:")
                    TextField(
                        value = authToken,
                        onValueChange = { authToken = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
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
                            onAddAgent(AddAgentInputs(name, llm, authToken, avatarUrl))
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
    ConnectableProvider.DUMMY -> false
}

private fun toTextLabel(llm: ConnectableProvider): String = when (llm) {
    ConnectableProvider.HUGGING_FACE -> "Hugging Face"
    ConnectableProvider.DUMMY -> "Dummy"
}

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
            modifier = Modifier.size(64.dp)
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
