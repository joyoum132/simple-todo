package com.example.simpletodo.task.repository

import com.example.simpletodo.task.domain.Task
import org.springframework.data.jpa.repository.JpaRepository

interface TaskRepository: JpaRepository<Task, Long> {
}