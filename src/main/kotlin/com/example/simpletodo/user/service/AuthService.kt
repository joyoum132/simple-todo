package com.example.simpletodo.user.service

import com.example.simpletodo.settings.exception.AuthException
import com.example.simpletodo.settings.exception.BadRequestException
import com.example.simpletodo.user.controller.dto.SignInReq
import com.example.simpletodo.user.controller.dto.SignUpReq
import com.example.simpletodo.user.domain.User
import com.example.simpletodo.user.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository,
) {

    fun signUp(signUpReq: SignUpReq): User {
        val isDulpLoginId = userRepository.existsByLoginIdAndIsDeletedFalse(signUpReq.loginId)
        if (isDulpLoginId) {
            throw BadRequestException("중복된 아이디입니다.")
        }

        return userRepository.save(
            User(
                signUpReq.loginId,
                passwordEncoder.encode(signUpReq.password),
                signUpReq.nickname,
            )
        )
    }

    @Transactional
    fun deleteUser(loginId: String): User {
        return userRepository.findUserByLoginIdAndIsDeletedFalseAndIsLoginTrue(loginId)
            ?.apply { isDeleted = true }
            ?: throw BadRequestException("회원을 찾을 수 없습니다.")
    }

    @Transactional
    fun signIn(signInReq: SignInReq): User {
        return userRepository.findUserByLoginIdAndIsDeletedFalseAndIsLoginTrue(signInReq.loginId)
            ?.apply {
                if (!passwordEncoder.matches(signInReq.password, password)) {
                    throw AuthException("계정 정보가 일치하지 않습니다.")
                }
                isLogin = true
            }
            ?: throw BadRequestException("회원을 찾을 수 없습니다.")
    }
}