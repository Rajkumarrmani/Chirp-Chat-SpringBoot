package com.rk.learningchirp.api.dto

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Email

data class EmailRequest(
    @JsonProperty("email")
    @field:Email
    val email: String
)
