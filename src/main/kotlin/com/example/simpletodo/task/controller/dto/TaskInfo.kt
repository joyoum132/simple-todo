package com.example.simpletodo.task.controller.dto

import com.example.simpletodo.task.domain.Task
import java.time.LocalDate
import java.time.LocalDateTime

class TaskInfo (
    val id: Long,
    val title: String,
    val content: String?,
    val dueDate: LocalDate?,
    val status: Task.TaskStatus,
    val created: LocalDateTime,
    val updated: LocalDateTime,
)