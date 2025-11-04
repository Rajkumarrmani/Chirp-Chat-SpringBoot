package com.rk.learningchirp.api.util

import com.rk.learningchirp.domain.exception.UnauthorizedException
import com.rk.learningchirp.domain.model.UserId
import org.springframework.security.core.context.SecurityContextHolder

val requestUserId : UserId get() =
    SecurityContextHolder.getContext().authentication?.principal as? UserId ?: throw UnauthorizedException()