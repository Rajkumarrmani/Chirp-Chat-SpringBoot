package com.rk.learningchirp.domain.model

import java.util.UUID




// Password no need to return to user so here no need to add it
data class User(
    val id: UserId,
    val username: String,
    val email: String,
    val hasEmailVerified: Boolean
)
