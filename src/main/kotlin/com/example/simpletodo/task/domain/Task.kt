package com.example.simpletodo.task.domain

import com.example.simpletodo.settings.commonVal.BaseTime
import com.example.simpletodo.user.domain.User
import jakarta.persistence.*

@Entity
class Task(
    @Column(nullable = false)
    var title: String,

    @Column(nullable = true)
    var content: String?,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: TaskStatus,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User
): BaseTime() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    enum class TaskStatus {
        TODO, PENDING, IN_PROGRESS, DONE
    }
}