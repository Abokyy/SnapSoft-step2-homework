package com.snapsoft.homework.model

data class UserValidator(
    val username: String,
    val password: String,
    val request_token: String
)