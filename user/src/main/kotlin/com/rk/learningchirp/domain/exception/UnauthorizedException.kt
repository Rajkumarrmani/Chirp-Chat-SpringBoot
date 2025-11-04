package com.rk.learningchirp.domain.exception

class UnauthorizedException : RuntimeException(
    "Missing auth details"
)