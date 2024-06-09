package com.example.simpletodo.user.controller.dto

import jakarta.validation.constraints.NotBlank

class SignUpReq (
    @field:NotBlank
    val loginId: String,

    @field:NotBlank
    val nickname: String,

    @field:NotBlank
    val password: String
)