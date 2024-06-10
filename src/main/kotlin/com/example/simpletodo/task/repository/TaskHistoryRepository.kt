package com.example.simpletodo.task.repository

import com.example.simpletodo.task.domain.TaskHistory
import org.springframework.data.jpa.repository.JpaRepository

interface TaskHistoryRepository : JpaRepository<TaskHistory, Long> {
    fun findByTaskIdOrderByCreatedAsc(taskId: Long): List<TaskHistory>
}