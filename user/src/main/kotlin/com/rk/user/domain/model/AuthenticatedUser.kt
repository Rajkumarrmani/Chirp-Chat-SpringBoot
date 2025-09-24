package com.rk.user.domain.model

data class AuthenticatedUser(
    val user: String,
    val accessToken: String,
    val refreshToken: String
)
