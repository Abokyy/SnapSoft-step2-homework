package com.snapsoft.homework.model.DTOs

data class RequestTokenDTO(
    val success: Boolean,
    val expires_at: String,
    val request_token: String
)