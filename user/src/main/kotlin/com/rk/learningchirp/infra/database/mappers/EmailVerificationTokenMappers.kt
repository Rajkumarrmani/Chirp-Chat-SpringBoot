package com.rk.learningchirp.infra.database.mappers

import com.rk.learningchirp.domain.model.EmailVerificationToken
import com.rk.learningchirp.infra.database.entities.EmailVerificationTokenEntity

fun EmailVerificationTokenEntity.toEmailVerificationToken(): EmailVerificationToken {
    return EmailVerificationToken(
        id = id,
        token = token,
        user = user.toUser()
    )
}