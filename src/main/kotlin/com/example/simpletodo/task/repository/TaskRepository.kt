package com.example.simpletodo.task.repository

import com.example.simpletodo.task.domain.Task
import com.example.simpletodo.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate

interface TaskRepository : JpaRepository<Task, Long> {
    fun findFirstByUserAndIsDeletedFalseOrderByCreatedDesc(user: User): Task?

    @Query("select t from Task t where t.user=:user and ((t.dueDate between :start and :end) or t.dueDate is null)")
    fun getTaskByUserAndDueDateBetweenOrNull(user: User, start: LocalDate, end: LocalDate): List<Task>

    fun findByIdAndIsDeletedFalse(id: Long): Task?

}