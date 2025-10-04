package com.rk.learningchirp.infra.database.repositories

import com.rk.learningchirp.domain.model.EmailVerificationToken
import com.rk.learningchirp.infra.database.entities.EmailVerificationTokenEntity
import com.rk.learningchirp.infra.database.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant

interface EmailVerificationTokenRepository : JpaRepository<EmailVerificationTokenEntity, Long> {
    fun findByToken(token: String): EmailVerificationTokenEntity?
    fun deleteByExpiresAtLessThan(now: Instant)
    fun findByUserAndUsedAtIsNull(user: UserEntity): List<EmailVerificationTokenEntity>
}