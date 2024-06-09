package com.example.simpletodo.task.state

import com.example.simpletodo.task.domain.Task
import com.example.simpletodo.task.repository.TaskHistoryRepository
import org.springframework.stereotype.Component

@Component
class StateFactory(
    private val taskHistoryRepository: TaskHistoryRepository
) {
    fun create(task: Task) : TaskState {
        return when(task.status) {
            Task.TaskStatus.TODO -> StateTodo(taskHistoryRepository)
            Task.TaskStatus.PENDING -> StatePending(taskHistoryRepository)
            Task.TaskStatus.IN_PROGRESS -> StateInProgress(taskHistoryRepository)
            Task.TaskStatus.DONE -> StateDone(taskHistoryRepository)
        }

    }
}