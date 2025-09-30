package com.rk.learningchirp.infra.database.mappers

import com.rk.learningchirp.domain.model.User
import com.rk.learningchirp.infra.database.entities.UserEntity

fun UserEntity.toUser(): User {
    return User(
        id = id!!,
        username = username,
        email = email,
        hasEmailVerified = hasVerifiedEmail
    )
}