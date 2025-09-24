package com.rk.learningchirp.infra.database.repositories

import com.rk.learningchirp.domain.model.UserId
import com.rk.learningchirp.infra.database.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, UserId> {
    fun findByEmail(email: String): UserEntity?
    fun findByEmailOrUsername(email: String, username: String): UserEntity?
}