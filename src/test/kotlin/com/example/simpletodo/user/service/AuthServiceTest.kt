package com.example.simpletodo.user.service

import com.example.simpletodo.settings.exception.BadRequestException
import com.example.simpletodo.user.controller.dto.SignInReq
import com.example.simpletodo.user.controller.dto.SignUpReq
import com.example.simpletodo.user.domain.User
import com.example.simpletodo.user.repository.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.security.crypto.password.PasswordEncoder

class AuthServiceTest {
    private lateinit var authService: AuthService
    private lateinit var passwordEncoder: PasswordEncoder
    private lateinit var userRepository: UserRepository

    private lateinit var loginId: String
    private lateinit var nickName: String
    private lateinit var inputPwd: String
    private lateinit var encryptPwd: String
    private lateinit var savedUser: User


    @BeforeEach
    fun before() {
        userRepository = mock(UserRepository::class.java)
        passwordEncoder = mock(PasswordEncoder::class.java)
        authService = AuthService(passwordEncoder, userRepository)

        loginId = "tester"
        nickName = "nickname"
        inputPwd = "password"
        encryptPwd = "encryptPwd"
        savedUser = User(loginId, nickName, inputPwd)
        savedUser.password = encryptPwd
    }

    @Test
    fun 회원가입_성공() {
        `when`(userRepository.existsByLoginIdAndIsDeletedFalse(loginId)).thenReturn(false)
        `when`(userRepository.save(any(User::class.java))).thenReturn(savedUser)
        `when`(passwordEncoder.encode(inputPwd)).thenReturn(encryptPwd)

        val result = authService.signUp(
            SignUpReq(loginId, nickName, inputPwd)
        )

        assertEquals(savedUser, result)
    }

    @Test
    fun 회원가입_실패_로그인_아이디_중복() {
        `when`(userRepository.existsByLoginIdAndIsDeletedFalse(loginId)).thenReturn(true)

        assertThrows(BadRequestException::class.java) {
            authService.signUp(
                SignUpReq(loginId, nickName, inputPwd)
            )
        }
    }

    @Test
    fun 회원탈퇴_성공() {
        `when`(userRepository.findUserByLoginIdAndIsDeletedFalse(loginId)).thenReturn(savedUser)

        val deletedUser = authService.deleteUser(loginId)
        assertTrue(deletedUser.isDeleted)
    }


    @Test
    fun 회원탈퇴_실패_회원조회안됨() {
        `when`(userRepository.findUserByLoginIdAndIsDeletedFalse(loginId)).thenReturn(null)

        assertThrows(BadRequestException::class.java) {
            authService.deleteUser(loginId)
        }

    }

    @Test
    fun 로그인_성공() {
        `when`(userRepository.findUserByLoginIdAndIsDeletedFalse(loginId)).thenReturn(savedUser)
        `when`(passwordEncoder.matches(inputPwd, encryptPwd)).thenReturn(true)

        val loginUser = authService.signIn(
            SignInReq(loginId, inputPwd)
        )
        assertTrue(loginUser.isLogin)
    }
}