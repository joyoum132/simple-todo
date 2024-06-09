package com.example.simpletodo.task.domain

import com.example.simpletodo.settings.commonVal.BaseTime
import com.example.simpletodo.user.domain.User
import jakarta.persistence.*
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

@Entity
class Task(
    @Column(nullable = false)
    var title: String,

    @Column(nullable = true)
    var content: String?,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: TaskStatus,

    @Column
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    var dueDate: LocalDate?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User
): BaseTime() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @Column(nullable = false)
    val isDeleted: Boolean = false

    enum class TaskStatus {
        TODO, PENDING, IN_PROGRESS, DONE
    }
}