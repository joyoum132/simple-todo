package com.example.simpletodo.task.controller.dto

import com.example.simpletodo.task.domain.Task

class ChangeStatusReq(
    val to: Task.TaskStatus,
    val memo: String?
)