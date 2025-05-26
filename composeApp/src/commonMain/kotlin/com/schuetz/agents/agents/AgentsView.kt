package com.schuetz.agents.agents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.schuetz.agents.domain.AgentData

@Composable
fun Agents(viewModel: AgentsViewModel, onAgentSelected: (AgentData) -> Unit) {
    val agents by viewModel.otherAgents
        .collectAsState(initial = emptyList())

    val showAddAgentDialog = remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            AddButton(onClick = {
                showAddAgentDialog.value = true
            })
        }
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(agents) { item ->
                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 40.dp)
                        .clickable { onAgentSelected(item) },
                    text = item.name
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }

    if (showAddAgentDialog.value) {
        AddAgentDialog(onAddAgent = {
            viewModel.addAgent(it)
            showAddAgentDialog.value = false
        }, onDismiss = {
            showAddAgentDialog.value = false
        })
    }
}

@Composable
fun AddAgentDialog(
    onAddAgent: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }

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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "Agent name:")
                TextField(
                    value = name,
                    onValueChange = { name = it },
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
                            onAddAgent(name)
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
fun AddButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = { onClick() },
    ) {
        Icon(Icons.Filled.Add, "Add")
    }
}
