package com.rk.learningchirp.service.auth

import com.rk.learningchirp.domain.exception.UserAlreadyExistsException
import com.rk.learningchirp.domain.model.User
import com.rk.learningchirp.infra.database.entities.UserEntity
import com.rk.learningchirp.infra.database.mappers.toUser
import com.rk.learningchirp.infra.database.repositories.UserRepository
import com.rk.learningchirp.infra.security.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun register(email: String, username: String, password: String): User {
        val user = userRepository.findByEmailOrUsername(
            email = email.trim(),
            username = password.trim()
        )

        if(user != null) throw UserAlreadyExistsException()

        val savedUser = userRepository.save(
            UserEntity(
                email = email.trim(),
                username = username.trim(),
                hashedPassword = passwordEncoder.enCode(password)!!
            )
        ).toUser()

        return savedUser
    }
}