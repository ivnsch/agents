package com.schuetz.agents.agents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.schuetz.agents.domain.AgentData

@Composable
fun Agents(viewModel: AgentsViewModel, onAgentSelected: (AgentData) -> Unit) {
    val agents by viewModel.otherAgents
        .collectAsState(initial = emptyList())

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
