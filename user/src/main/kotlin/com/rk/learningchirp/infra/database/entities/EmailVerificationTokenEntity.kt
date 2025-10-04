package com.rk.learningchirp.infra.database.entities

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.Instant

@Entity
@Table(
    name = "email_verification_token",
    schema = "user_service"
)

class EmailVerificationTokenEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    @Column(nullable = false, unique = true)
    var token: String,
    @Column(nullable = false)
    var expiresAt: Instant,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: UserEntity,
    @Column(nullable = false)
    var usedAt: Instant?,
    @CreationTimestamp
    var createdAt: Instant = Instant.now()
)