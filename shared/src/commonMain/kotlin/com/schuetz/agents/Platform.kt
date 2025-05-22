package com.schuetz.agents

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform