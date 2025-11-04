package com.rk.learningchirp.service

import com.rk.learningchirp.domain.exception.*
import com.rk.learningchirp.domain.model.AuthenticatedUser
import com.rk.learningchirp.domain.model.User
import com.rk.learningchirp.domain.type.UserId
import com.rk.learningchirp.infra.database.entities.RefreshTokenEntity
import com.rk.learningchirp.infra.database.entities.UserEntity
import com.rk.learningchirp.infra.database.mappers.toUser
import com.rk.learningchirp.infra.database.repositories.RefreshTokenRepository
import com.rk.learningchirp.infra.database.repositories.UserRepository
import com.rk.learningchirp.infra.security.PasswordEncoder
import com.rk.learningchirp.service.auth.EmailVerificationService
import com.rk.learningchirp.service.auth.JwtService
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import java.security.MessageDigest
import java.time.Instant
import java.util.*

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val emailVerificationService: EmailVerificationService,
) {
    @Transactional
    fun register(email: String, username: String, password: String): User {

        val trimmedEmail = email.trim()

        val user = userRepository.findByEmailOrUsername(
            email = trimmedEmail,
            username = password.trim()
        )

        if (user != null) throw UserAlreadyExistsException()


        val savedUser = userRepository.saveAndFlush(
            UserEntity(
                email = trimmedEmail,
                username = username.trim(),
                hashedPassword = passwordEncoder.enCode(password)!!
            )
        ).toUser()

        val token = emailVerificationService.createVerificationToken(trimmedEmail)

        return savedUser
    }

    fun login(
        email: String,
        password: String
    ): AuthenticatedUser {
        val user = userRepository.findByEmail(email.trim())
            ?: throw InvalidCredentialsException()

        if (!passwordEncoder.matches(password, user.hashedPassword)) {
            throw InvalidCredentialsException()
        }

        if(!user.hasVerifiedEmail) {
            throw EmailNotVerifiedException()
        }

        return user.id?.let { userId ->
            val accessToken = jwtService.generateAccessToken(userId)
            val refreshToken = jwtService.generateRefreshToken(userId)

            storeRefreshToken(userId, refreshToken)

            AuthenticatedUser(
                user = user.toUser(),
                accessToken = accessToken,
                refreshToken = refreshToken
            )
        } ?: throw UserNotFoundException()
    }

    @GetMapping("/verify")
    fun verifyEmail(
        @RequestParam token: String
    ) {
        emailVerificationService.verifyEmail(token)
    }

    @Transactional
    fun logout(refreshToken: String) {
        val userId = jwtService.getUserIdFromToken(refreshToken)
        val hashed = hashToken(refreshToken)
        refreshTokenRepository.deleteByUserIdAndHashedToken(userId, hashed)
    }

    @Transactional
    fun refresh(refreshToken: String): AuthenticatedUser {
        if (!jwtService.validateRefreshToken(refreshToken)) {
            throw InvalidTokenException(
                message = "Invalid refresh token"
            )
        }

        val userId = jwtService.getUserIdFromToken(refreshToken)

        val user = userRepository.findByIdOrNull(userId)
            ?: throw UserNotFoundException()

        val hashed = hashToken(refreshToken)

        return user.id?.let { userId ->

            refreshTokenRepository.findByUserIdAndHashedToken(
                userId = userId,
                hashedToken = hashed
            ) ?: throw InvalidTokenException("Invalid refresh token")

            refreshTokenRepository.deleteByUserIdAndHashedToken(
                userId = userId,
                hashedToken = hashed
            )

            val newAccessToken = jwtService.generateAccessToken(userId)
            val newRefreshToken = jwtService.generateRefreshToken(userId)

            storeRefreshToken(userId, newRefreshToken)

            AuthenticatedUser(
                user = user.toUser(),
                accessToken = newAccessToken,
                refreshToken = newRefreshToken
            )
        } ?: throw UserNotFoundException()
    }


    private fun storeRefreshToken(userId: UserId, token: String) {
        val hashed = hashToken(token)
        val expiryMs = jwtService.refreshTokenValidityMs
        val expiresAt = Instant.now().plusMillis(expiryMs)

        refreshTokenRepository.save(
            RefreshTokenEntity(
                userId = userId,
                expiresAt = expiresAt,
                hashedToken = hashed
            )
        )
    }

    private fun hashToken(token: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(token.encodeToByteArray())
        return Base64.getEncoder().encodeToString(hashBytes)
    }
}