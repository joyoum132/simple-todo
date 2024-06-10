package com.example.simpletodo.user.controller.dto

import jakarta.validation.constraints.NotBlank

class SignInReq(
    @field:NotBlank
    val loginId: String,

    @field:NotBlank
    val password: String
)