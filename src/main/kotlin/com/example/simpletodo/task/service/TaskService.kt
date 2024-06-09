package com.example.simpletodo.task.service

import com.example.simpletodo.settings.exception.BadRequestException
import com.example.simpletodo.task.controller.dto.AddTaskReq
import com.example.simpletodo.task.controller.dto.ChangeStatusReq
import com.example.simpletodo.task.controller.dto.TaskDetail
import com.example.simpletodo.task.controller.dto.TaskInfo
import com.example.simpletodo.task.domain.Task
import com.example.simpletodo.task.repository.TaskHistoryRepository
import com.example.simpletodo.task.repository.TaskRepository
import com.example.simpletodo.task.state.StateFactory
import com.example.simpletodo.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class TaskService(
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository,
    private val taskHistoryRepository: TaskHistoryRepository,
    private val stateFactory: StateFactory,
) {

    fun makeTask(req: AddTaskReq): Task {
        return userRepository.findUserByLoginIdAndIsDeletedFalse(req.loginId)
            ?.run {
                taskRepository.save(
                    Task(req.title, req.content, req.status, req.dueDate, this)
                )
            }
            ?: throw BadRequestException("회원을 찾을 수 없습니다.")
    }

    fun getAllTaskBetweenDueDate(
        loginId: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): Map<Task.TaskStatus, List<TaskInfo>> {
        userRepository.findUserByLoginIdAndIsDeletedFalse(loginId)
            ?.let { user ->
                return taskRepository.findByUserAndDueDateBetweenOrderByCreated(user, startDate, endDate.plusDays(1))
                    .groupBy(
                        { it.status },
                        { TaskInfo(
                            it.id,
                            it.title,
                            it.content,
                            it.dueDate,
                            it.status,
                            it.created,
                            it.updated
                        )
                        }
                    )
            }
            ?: throw BadRequestException("회원을 찾을 수 없습니다.")
    }

    fun getLastAddedTask(loginId: String): TaskDetail {
        return userRepository.findUserByLoginIdAndIsDeletedFalse(loginId)
            ?.let { user ->
                taskRepository.findFirstByUserAndIsDeletedFalseOrderByCreatedDesc(user)
                    ?.let { task ->
                        TaskDetail(
                            taskInfo = TaskInfo(
                                id = task.id,
                                title = task.title,
                                content = task.content,
                                dueDate = task.dueDate,
                                status = task.status,
                                created = task.created,
                                updated = task.updated,
                            ),
                            stateHistory = taskHistoryRepository.findByTaskIdOrderByCreatedAsc(task.id)
                                .map {
                                    TaskDetail.History(
                                        id = it.id,
                                        oldStatus = it.oldStatus,
                                        newStatus = it.newStatus,
                                        memo = it.memo,
                                        created = it.created,
                                        updated = it.updated
                                    )
                                }
                        )
                    }
                    ?: return TaskDetail()
            }
            ?: throw BadRequestException("회원을 찾을 수 없습니다.")
    }

    @Transactional
    fun updateTaskStatus(taskId: Long, req: ChangeStatusReq): Task {
        return taskRepository.findByIdOrNull(taskId)
            ?.apply {
                val state = stateFactory.create(this)
                state.changeState(this, req.to, req.memo)
            }
            ?: throw BadRequestException("할일을 찾을 수 없습니다.")

    }

}