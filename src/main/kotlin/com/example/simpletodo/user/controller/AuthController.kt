package com.example.simpletodo.user.controller

import com.example.simpletodo.settings.commonVal.ApiResponse
import com.example.simpletodo.user.controller.dto.SignInReq
import com.example.simpletodo.user.controller.dto.SignUpReq
import com.example.simpletodo.user.service.AuthService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/sign-up", name = "회원 가입")
    fun signUp(@RequestBody @Valid signUpReq: SignUpReq): ApiResponse<Long> {
        val newUser = authService.signUp(signUpReq)
        return ApiResponse(
            true,
            newUser.id
        )
    }

    @PostMapping("/sign-in", name = "로그인")
    fun signIn(@RequestBody @Valid signInReq: SignInReq): ApiResponse<Nothing> {
        authService.signIn(signInReq)
        return ApiResponse(true, null)
    }

    @DeleteMapping("/withdrawal/{loginId}", name = "회원 탈퇴")
    fun withdrawal(@PathVariable loginId: String): ApiResponse<Nothing> {
        authService.deleteUser(loginId)
        return ApiResponse(true, null)
    }
}