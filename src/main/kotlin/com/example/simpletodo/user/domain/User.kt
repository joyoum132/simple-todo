package com.example.simpletodo.user.domain

import com.example.simpletodo.settings.commonVal.BaseTime
import jakarta.persistence.*

@Entity
class User(
    @Column(nullable = false)
    val loginId: String,

    @Column(nullable = false)
    var password: String,

    @Column(nullable = false)
    var nickname: String
) : BaseTime() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @Column(nullable = false)
    var isDeleted: Boolean = false

    @Column(nullable = false)
    var isLogin: Boolean = false
}