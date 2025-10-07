package com.rk.learningchirp.api.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.rk.learningchirp.api.util.Password
import jakarta.validation.constraints.Email
import org.hibernate.validator.constraints.Length

data class RegisterRequest(
    @field: Email(message = "Must be a valid email address")
    @JsonProperty("email")
    val email: String,


    @field:Length(min = 4, max = 6, message = "Username must be at least 4 characters and 6 characters long")
    @JsonProperty("username")
    val username: String,


    @field:Password
    val password: String
)
