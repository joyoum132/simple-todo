package com.example.simpletodo.task.domain

import com.example.simpletodo.settings.commonVal.BaseTime
import jakarta.persistence.*

@Entity
class TaskHistory(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    val task: Task,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val oldStatus: Task.TaskStatus,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val newStatus: Task.TaskStatus

): BaseTime() {
    @Id
    var id: Long = 0L

    @Column(nullable = true)
    var memo: String? = null
}