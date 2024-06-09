package com.example.simpletodo.task.controller.dto

import com.example.simpletodo.task.domain.Task
import jakarta.validation.constraints.NotBlank
import java.time.LocalDate

class AddTaskReq (
    @field:NotBlank
    val loginId: String,

    @field:NotBlank
    val title: String,

    val content: String? = null,
    val dueDate: LocalDate? = null,
    val status: Task.TaskStatus = Task.TaskStatus.TODO,
)