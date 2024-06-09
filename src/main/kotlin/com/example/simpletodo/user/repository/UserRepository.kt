package com.example.simpletodo.user.repository

import com.example.simpletodo.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Long> {
    fun existsByLoginIdAndIsDeletedFalse(loginId: String): Boolean

    fun findUserByLoginIdAndIsDeletedFalse(loginId: String): User?
}