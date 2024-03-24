package com.example.dotametrics.domain.repository.response

data class BasicResponse<T>(
    val data: T?,
    val error: String = "null"
)
