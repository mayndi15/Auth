package com.salus.user

data class UserResponse(
    val id: Long,
    val username: String,
    val roles: Set<String>
)