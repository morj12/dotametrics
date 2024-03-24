package com.example.dotametrics.domain.entity.remote

data class BasicResponse<T>(
    val data: T?,
    val error: String = "null"
)
