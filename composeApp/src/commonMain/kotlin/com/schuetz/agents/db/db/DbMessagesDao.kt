package com.schuetz.agents.db.db

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.schuetz.agents.db.MessagesDao
import com.schuetz.agents.domain.AgentConnectionData.None.toConnectionData
import com.schuetz.agents.domain.AgentData
import com.schuetz.agents.domain.Message
import com.schuetz.agents.domain.MessageInput
import com.schuetz.agents.domain.SpaceData
import com.schuetz.agents.domain.StructuredMessage
import com.schuetz.agents.domain.StructuredMessageInput
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class DbMessagesDao(
    private val database: MyDatabase,
    private val dispatcher: CoroutineDispatcher
) : MessagesDao {

    override suspend fun messages(spaceId: Long): Flow<List<Message>> =
        database.messageQueries
            .selectWithSpace(spaceId)
            .asFlow()
            .mapToList(dispatcher)
            .map { messages ->
                messages.map {
                    val connectionData =
                        toConnectionData(
                            it.author_provider,
                            it.author_model,
                            it.author_api_key
                        ).getOrElse { e -> throw e }
                    val agent = AgentData(
                        id = it.author_id,
                        name = it.author_name,
                        description = it.author_description,
                        isMe = it.author_is_me,
                        avatarUrl = it.author_avatar_url,
                        connectionData = connectionData
                    )
                    val strucutedContent = fromDbStr(it.text)
                    Message(
                        it.id,
                        content = strucutedContent,
                        agent,
                        space = SpaceData(it.space_id, it.space_name, agent)
                    )
                }
            }
            .flowOn(dispatcher)

    override suspend fun insert(message: MessageInput) {
        database.messageQueries.insert(message.text, message.author.id, message.space.id)
    }

    override suspend fun insert(message: StructuredMessageInput) {
        val jsonString = toDbStr(message.content)
        database.messageQueries.insert(jsonString, message.author.id, message.space.id)
    }

    private fun toDbStr(response: StructuredMessage): String =
        Json.encodeToString(response)

    private fun fromDbStr(jsonString: String): StructuredMessage =
        Json.decodeFromString<StructuredMessage>(jsonString)
}
