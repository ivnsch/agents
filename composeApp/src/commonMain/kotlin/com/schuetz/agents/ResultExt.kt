package com.schuetz.agents

fun <T, U> Result<T>.flatMap(transform: (T) -> Result<U>): Result<U> =
    fold(onSuccess = transform, onFailure = { Result.failure(it) })
