package com.snapsoft.homework.model.DTOs

data class GuestSessionDTO(
    val success: Boolean,
    val guest_session_id: String,
    val expires_at: String
)