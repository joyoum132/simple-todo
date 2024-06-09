package com.example.simpletodo.task.state

import com.example.simpletodo.settings.exception.BadRequestException
import com.example.simpletodo.task.domain.Task
import com.example.simpletodo.task.repository.TaskHistoryRepository

class StateDone(
    private val taskHistoryRepository: TaskHistoryRepository
) : TaskState {

    override fun canChangeTo(to: Task.TaskStatus): Boolean {
        return to != Task.TaskStatus.PENDING
    }

    override fun changeState(task: Task, to: Task.TaskStatus, memo: String?): Task {
        if(!canChangeTo(to)) {
            throw BadRequestException("상태를 변경할 수 없습니다.")
        }
        writeChangeHistory(taskHistoryRepository, task, to, memo)
        task.status = to
        return task
    }
}