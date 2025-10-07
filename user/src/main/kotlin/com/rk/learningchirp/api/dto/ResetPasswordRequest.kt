package com.rk.learningchirp.api.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.rk.learningchirp.api.util.Password
import jakarta.validation.constraints.NotBlank

data class ResetPasswordRequest(
    @JsonProperty("token")
    @field:NotBlank
    val token: String,

    @JsonProperty("newPassword")
    @field:Password
    val newPassword: String
)
