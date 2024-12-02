package com.salus.user

data class UserRequest(
    val username: String,
    val password: String,
    val roles: Set<String>
)