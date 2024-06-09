package com.example.simpletodo.task.controller

import com.example.simpletodo.settings.commonVal.ApiResponse
import com.example.simpletodo.task.controller.dto.AddTaskReq
import com.example.simpletodo.task.controller.dto.TaskDetail
import com.example.simpletodo.task.controller.dto.TaskInfo
import com.example.simpletodo.task.domain.Task
import com.example.simpletodo.task.service.TaskService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/task")
class TaskController(
    private val taskService: TaskService
) {

    @PostMapping(name = "할 일 등록")
    fun addTask(@RequestBody @Valid addTaskReq: AddTaskReq): ApiResponse<Task> {
        return ApiResponse(
            true,
            taskService.makeTask(addTaskReq)
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
    fun getTaskBetweenDueDate(@PathVariable loginId: String,@PathVariable start: LocalDate, @PathVariable end: LocalDate): ApiResponse<Map<Task.TaskStatus, List<TaskInfo>>> {
        return ApiResponse(
            true,
            taskService.getAllTaskBetweenDueDate(loginId, start, end)
        )
    }

}