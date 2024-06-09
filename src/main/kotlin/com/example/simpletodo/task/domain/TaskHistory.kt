package com.example.simpletodo.task.domain

import com.example.simpletodo.settings.commonVal.BaseTime
import jakarta.persistence.*

@Entity
class TaskHistory(
    @Column(nullable = false)
    val taskId: Long,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val oldStatus: Task.TaskStatus,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val newStatus: Task.TaskStatus,

    @Column(nullable = true)
    var memo: String? = null

): BaseTime() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L
}