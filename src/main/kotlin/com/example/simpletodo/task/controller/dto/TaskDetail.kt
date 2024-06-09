package com.example.simpletodo.task.controller.dto

import com.example.simpletodo.task.domain.Task
import java.time.LocalDate
import java.time.LocalDateTime

class TaskDetail (
    val taskInfo: TaskInfo? = null,
    val stateHistory: List<History> = emptyList()
) {
    class History(
        val id: Long,
        val oldStatus: Task.TaskStatus,
        val newStatus: Task.TaskStatus,
        val memo: String?,
        val created: LocalDateTime,
        val updated: LocalDateTime,
        )
}
