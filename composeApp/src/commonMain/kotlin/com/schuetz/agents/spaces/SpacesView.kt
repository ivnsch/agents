package com.schuetz.agents.spaces

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.schuetz.agents.agents.AddAgentDialog
import com.schuetz.agents.domain.SpaceData

@Composable
fun Spaces(viewModel: SpacesViewModel, onSpaceSelected: (SpaceData) -> Unit) {
    val agents by viewModel.spaces
        .collectAsState(initial = emptyList())

    val showAddAgentDialog = remember { mutableStateOf(false) }

    val avatarUrl by viewModel.newAgentavatarUrl.collectAsState()

    Scaffold(
        floatingActionButton = {
            AddButton(onClick = {
                showAddAgentDialog.value = true
            })
        }
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(agents) { item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = item.agent.avatarUrl,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp)
                    )
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 40.dp)
                            .clickable { onSpaceSelected(item) },
                        text = item.name
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }

    if (showAddAgentDialog.value) {
        AddAgentDialog(
            avatarUrl = avatarUrl,
            llmModels = viewModel.llmModels(),
            onAddAgent = {
                viewModel.addSpaceWithAgent(it)
                showAddAgentDialog.value = false
            },
            onDismiss = {
                showAddAgentDialog.value = false
            },
            regenerateAvatar = {
                viewModel.regenerateAvatarUrl()
            }
        )
    }
}

@Composable
fun AddButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = { onClick() },
    ) {
        Icon(Icons.Filled.Add, "Add")
    }
}
