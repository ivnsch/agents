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
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
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
                Text(text = "API key:")
                TextField(
                    value = authToken,
                    onValueChange = { authToken = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    TextButton(
                        onClick = {
                            onAddAgent(AddAgentInputs(name, authToken, avatarUrl))
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
