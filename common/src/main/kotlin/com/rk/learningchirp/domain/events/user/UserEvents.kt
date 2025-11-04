package com.rk.learningchirp.domain.events.user

import com.rk.learningchirp.domain.events.ChirpEvent
import com.rk.learningchirp.domain.type.UserId
import java.time.Instant
import java.util.UUID

// common are comes in UserEvents parameter and key i mean run time are comes in data class
sealed class UserEvents(
    override val eventId: String = UUID.randomUUID().toString(),
    override val exchange: String = UserEventConstants.USER_EXCHANGE,
    override val occurredAt: Instant = Instant.now(),
) : ChirpEvent {

    data class Created(
        val userId: UserId,
        val email: String,
        val userName: String,
        val verificationToken: String,
        override val eventKey: String = UserEventConstants.USER_CREATED_KEY
    ) : UserEvents(), ChirpEvent


    data class Verified(
        val userId: UserId,
        val email: String,
        val userName: String,
        override val eventKey: String = UserEventConstants.USER_VERIFIED
    ) : UserEvents(), ChirpEvent

    data class RequestResendVerification(
        val userId: UserId,
        val email: String,
        val userName: String,
        val verificationToken: String,
        override val eventKey: String = UserEventConstants.USER_REQUEST_RESEND_VERIFICATION

    ) : UserEvents(), ChirpEvent

    data class RequestResetPassword(
        val userId: UserId,
        val email: String,
        val userName: String,
        val expiresInMinutes: Long,
        override val eventKey: String = UserEventConstants.USER_REQUEST_RESET_PASSWORD

    ) : UserEvents(), ChirpEvent
}