package com.schuetz.agents.domain

data class Message(val text: String, val author: Author)

sealed interface Author {
    data object Me : Author
    data object Agent : Author
}
