package com.example.simpletodo.task.repository

import com.example.simpletodo.task.domain.Task
import com.example.simpletodo.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface TaskRepository: JpaRepository<Task, Long> {
    fun findFirstByUserAndIsDeletedFalseOrderByCreatedDesc(user: User): Task?

    fun findByUserAndDueDateBetweenOrderByCreated(user: User, start: LocalDate, end: LocalDate): List<Task>

    fun findByIdAndIsDeletedFalse(id: Long): Task?

}