package com.rk.learningchirp.infra.database.repositories

import com.rk.learningchirp.infra.database.entities.RefreshTokenEntity
import org.springframework.data.jpa.repository.JpaRepository

interface RefreshTokenRepository : JpaRepository<RefreshTokenEntity, Long> {

    fun findByUserIdAndHashedToken(userId: String, hashedToken: String): RefreshTokenEntity?
    fun deleteByUserIdAndHashedToken(userId: String, hashedToken: String)
    fun deleteByUserId(userId: String)
}