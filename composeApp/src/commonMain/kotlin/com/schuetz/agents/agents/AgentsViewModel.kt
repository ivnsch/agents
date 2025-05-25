package com.schuetz.agents.agents

import androidx.lifecycle.ViewModel
import com.schuetz.agents.domain.AgentData
import kotlinx.coroutines.flow.Flow

class AgentsViewModel(
    private val agentsRepo: AgentsRepo,
) : ViewModel() {
    val otherAgents: Flow<List<AgentData>> = agentsRepo.otherAgents
}
