package com.rk.learningchirp.api.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.rk.learningchirp.api.util.Password
import jakarta.validation.constraints.NotBlank

data class ChangePasswordRequest(
    @JsonProperty("oldPassword")
    @field:NotBlank
    val oldPassword: String,
    @JsonProperty("newPassword")
    @field:Password
    val newPassword: String
)
