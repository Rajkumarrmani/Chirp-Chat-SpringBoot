package com.rk.learningchirp.domain.model

data class AuthenticatedUser(
    val user: String,
    val accessToken: String,
    val refreshToken: String
)
