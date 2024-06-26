package com.example.simpletodo.task.controller

import com.example.simpletodo.settings.commonVal.ApiResponse
import com.example.simpletodo.task.controller.dto.AddTaskReq
import com.example.simpletodo.task.controller.dto.ChangeStatusReq
import com.example.simpletodo.task.controller.dto.TaskDetail
import com.example.simpletodo.task.controller.dto.TaskInfo
import com.example.simpletodo.task.domain.Task
import com.example.simpletodo.task.service.TaskService
import jakarta.validation.Valid
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/task")
class TaskController(
    private val taskService: TaskService
) {

    @PostMapping(name = "할 일 등록")
    fun addTask(@RequestBody @Valid addTaskReq: AddTaskReq): ApiResponse<Long> {
        val newTask = taskService.makeTask(addTaskReq)
        return ApiResponse(
            true,
            newTask.id
        )
    }

    @GetMapping("/{loginId}/last-added", name = "최근에 등록한 할 일 조횐")
    fun getLastAddedTask(@PathVariable loginId: String): ApiResponse<TaskDetail> {
        return ApiResponse(
            true,
            taskService.getLastAddedTask(loginId)
        )
    }

    @GetMapping("/{loginId}/due-date/{start}/{end}", name = "due-date 기준으로 할 일 목록 조회")
    fun getTaskBetweenDueDate(
        @PathVariable loginId: String,
        @PathVariable start: LocalDate,
        @PathVariable end: LocalDate
    ): ApiResponse<Map<Task.TaskStatus, List<TaskInfo>>> {
        return ApiResponse(
            true,
            taskService.getAllTaskBetweenDueDate(loginId, start, end)
        )
    }

    @PutMapping("/{loginId}/{taskId}", name = "todo 상태 변경")
    fun changeTaskStatus(@PathVariable loginId: String, @PathVariable taskId: Long, @RequestBody @Valid changeStatusReq: ChangeStatusReq) {
        taskService.updateTaskStatus(loginId, taskId, changeStatusReq)
        ApiResponse(
            true,
            null
        )
    }
}