package com.rk.learningchirp.api.mappers

import com.rk.learningchirp.api.dto.AuthenticatedUserDto
import com.rk.learningchirp.api.dto.UserDto
import com.rk.learningchirp.domain.model.AuthenticatedUser
import com.rk.learningchirp.domain.model.User


fun AuthenticatedUser.toAuthenticatedUserDto(): AuthenticatedUserDto =
    AuthenticatedUserDto(
        user = user.toUserDto(),
        accessToken = accessToken,
        refreshToken = refreshToken
    )

fun User.toUserDto(): UserDto {
    return UserDto(
        id = id,
        email = email,
        username = username,
        hasVerifiedEmail = hasEmailVerified
    )
}