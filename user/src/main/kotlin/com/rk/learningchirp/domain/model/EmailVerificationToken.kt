package com.rk.learningchirp.domain.model

data class EmailVerificationToken(
    val id: Long,
    val token: String,
    val user: User
)
