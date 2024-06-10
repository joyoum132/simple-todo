package com.example.simpletodo.task.state

import com.example.simpletodo.task.domain.Task
import com.example.simpletodo.task.domain.TaskHistory
import com.example.simpletodo.task.repository.TaskHistoryRepository

interface TaskState {
    fun canChangeTo(to: Task.TaskStatus): Boolean
    fun changeState(task: Task, to: Task.TaskStatus, memo: String?): Task

    fun writeChangeHistory(
        taskHistoryRepository: TaskHistoryRepository,
        task: Task,
        to: Task.TaskStatus,
        memo: String?
    ) {
        taskHistoryRepository.save(
            TaskHistory(
                task.id,
                task.status,
                to,
                memo
            )
        )
    }
}