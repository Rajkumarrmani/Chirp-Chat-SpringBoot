package com.rk.learningchirp.api.dto

import com.rk.learningchirp.domain.model.UserId

data class UserDto(
    val id: UserId,
    val email: String,
    val username: String,
    val hasVerifiedEmail: Boolean
)
