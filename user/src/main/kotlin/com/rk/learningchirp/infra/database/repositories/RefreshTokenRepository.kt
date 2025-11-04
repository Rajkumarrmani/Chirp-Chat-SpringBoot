package com.rk.learningchirp.infra.database.repositories

import com.rk.learningchirp.domain.type.UserId
import com.rk.learningchirp.infra.database.entities.RefreshTokenEntity
import org.springframework.data.jpa.repository.JpaRepository

interface RefreshTokenRepository : JpaRepository<RefreshTokenEntity, Long> {

    fun findByUserIdAndHashedToken(userId: UserId, hashedToken: String): RefreshTokenEntity?
    fun deleteByUserIdAndHashedToken(userId: UserId, hashedToken: String)
    fun deleteByUserId(userId: UserId)
}