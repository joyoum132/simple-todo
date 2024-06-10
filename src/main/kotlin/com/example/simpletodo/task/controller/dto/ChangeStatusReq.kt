package com.example.simpletodo.task.controller.dto

import com.example.simpletodo.task.domain.Task
import jakarta.validation.constraints.NotNull

class ChangeStatusReq(
    @field:NotNull
    val to: Task.TaskStatus,
    val memo: String?
)