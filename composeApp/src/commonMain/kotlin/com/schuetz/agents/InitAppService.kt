package com.schuetz.agents

import com.schuetz.agents.huggingface.HuggingFaceTokenStore

interface InitAppService {
    suspend fun init()
}

class InitAppServiceImpl(
    private val huggingFaceTokenStore: HuggingFaceTokenStore
) : InitAppService {
    override suspend fun init() {
        huggingFaceTokenStore.initialize()
    }
}
