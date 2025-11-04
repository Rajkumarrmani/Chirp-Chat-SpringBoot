package com.rk.learningchirp.service

import com.rk.learningchirp.domain.exception.InvalidCredentialsException
import com.rk.learningchirp.domain.exception.InvalidTokenException
import com.rk.learningchirp.domain.exception.SamePasswordException
import com.rk.learningchirp.domain.exception.UserNotFoundException
import com.rk.learningchirp.domain.type.UserId
import com.rk.learningchirp.infra.database.entities.PasswordResetTokenEntity
import com.rk.learningchirp.infra.database.repositories.PasswordResetTokenRepository
import com.rk.learningchirp.infra.database.repositories.RefreshTokenRepository
import com.rk.learningchirp.infra.database.repositories.UserRepository
import com.rk.learningchirp.infra.security.PasswordEncoder
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class PasswordResetService(
    private val userRepository: UserRepository,
    private val passwordResetTokenRepository: PasswordResetTokenRepository,
    private val passwordEncoder: PasswordEncoder,

    @param:Value("\${chirp.email.reset-password.expiry-minutes}")
    private val expiryMinutes: Long,

    private val refreshTokenRepository: RefreshTokenRepository
) {

    @Transactional
    fun requestPasswordReset(
        email: String
    ) {
        val user = userRepository.findByEmail(email) ?: return

        passwordResetTokenRepository.invalidateActiveTokensForUser(user)

        val token = PasswordResetTokenEntity(
            user = user,
            expiresAt = Instant.now().plus(expiryMinutes, ChronoUnit.MINUTES),
        )

        passwordResetTokenRepository.save(token)
    }

    @Transactional
    fun resetPassword(
        token: String,
        newPassword: String
    ) {
        val resetToken = passwordResetTokenRepository.findByToken(token)
            ?: throw InvalidTokenException("Invalid password reset token")

        if (resetToken.isUsed) {
            throw InvalidTokenException("Email verification token is already used.")
        }

        if (resetToken.isExpired) {
            throw InvalidTokenException("Email verification token has already expired.")
        }

        val user = resetToken.user

        if (passwordEncoder.matches(newPassword, user.hashedPassword)) {
            throw SamePasswordException()
        }

        val hashedNewPassword = passwordEncoder.enCode(newPassword)

        userRepository.save(
            user.apply {
                this.hashedPassword = hashedNewPassword!!
            }
        )

        passwordResetTokenRepository.save(
            resetToken.apply {
                this.usedAt = Instant.now()
            }
        )

        refreshTokenRepository.deleteByUserId(
            user.id!!
        )
    }

    @Transactional
    fun changePassword(
        userId: UserId,
        oldPassword: String,
        newPassword: String
    ) {

        val user = userRepository.findByIdOrNull(userId)
            ?: throw UserNotFoundException()

        if (!passwordEncoder.matches(oldPassword, user.hashedPassword)) {
            throw InvalidCredentialsException()
        }

        if (oldPassword == newPassword) {
            throw SamePasswordException()
        }

        refreshTokenRepository.deleteByUserId(user.id!!)

        val newHashedPassword = passwordEncoder.enCode(newPassword)

        userRepository.save(
            user.apply {
                this.hashedPassword = newHashedPassword!!
            }
        )

    }


    @Scheduled(cron = "0 0 3 * * *")
    fun cleanupExpiredTokens() {
        passwordResetTokenRepository.deleteByExpiresAtLessThan(
            now = Instant.now()
        )
    }


}