package com.example.simpletodo.task.service

import com.example.simpletodo.settings.exception.BadRequestException
import com.example.simpletodo.task.controller.dto.AddTaskReq
import com.example.simpletodo.task.controller.dto.ChangeStatusReq
import com.example.simpletodo.task.domain.Task
import com.example.simpletodo.task.repository.TaskHistoryRepository
import com.example.simpletodo.task.repository.TaskRepository
import com.example.simpletodo.task.state.*
import com.example.simpletodo.user.domain.User
import com.example.simpletodo.user.repository.UserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.time.LocalDate

class TaskServiceTest {
    private lateinit var taskService: TaskService
    private lateinit var taskRepository: TaskRepository
    private lateinit var userRepository: UserRepository
    private lateinit var taskHistoryRepository: TaskHistoryRepository
    private lateinit var stateFactory: StateFactory

    val loginId = "tester"
    val savedUser = User(loginId, "nickName", "inputPwd")
    val taskId: Long = 0L
    val todo = Task("title", "content", Task.TaskStatus.TODO, LocalDate.now(), savedUser)
    val inProgress = Task("title", "content", Task.TaskStatus.IN_PROGRESS, LocalDate.now(), savedUser)
    val pending = Task("title", "content", Task.TaskStatus.PENDING, LocalDate.now(), savedUser)
    val done = Task("title", "content", Task.TaskStatus.DONE, LocalDate.now(), savedUser)

    @BeforeEach
    fun before() {
        taskRepository = mock(TaskRepository::class.java)
        userRepository = mock(UserRepository::class.java)
        taskHistoryRepository = mock(TaskHistoryRepository::class.java)
        stateFactory = mock(StateFactory::class.java)
        taskService = TaskService(taskRepository, userRepository, taskHistoryRepository, stateFactory)
    }


    @Test
    fun 투두추가_성공() {
        `when`(userRepository.findUserByLoginIdAndIsDeletedFalseAndIsLoginTrue(loginId)).thenReturn(savedUser)
        `when`(taskRepository.save(any(Task::class.java))).thenReturn(todo)

        val req = AddTaskReq(loginId, "title", "content")
        val newTask = taskService.makeTask(req)
        assertEquals(newTask, todo)
    }

    @Test
    fun 투두추가_실패_회원정보없음() {
        `when`(userRepository.findUserByLoginIdAndIsDeletedFalseAndIsLoginTrue(loginId)).thenReturn(null)
        `when`(taskRepository.save(any(Task::class.java))).thenReturn(todo)

        assertThrows(BadRequestException::class.java) {
            val req = AddTaskReq(loginId, "title", "content")
            taskService.makeTask(req)
        }
    }

    @Test
    fun TODO_상태변경() {
        `when`(taskRepository.findByIdAndIsDeletedFalse(taskId)).thenReturn(todo)
        `when`(stateFactory.create(todo)).thenReturn(StateTodo(taskHistoryRepository))
        `when`(userRepository.findUserByLoginIdAndIsDeletedFalseAndIsLoginTrue(loginId)).thenReturn(savedUser)

        //todo -> pending (X)
        val toPending = ChangeStatusReq(to = Task.TaskStatus.PENDING, memo = "to pending")
        assertThrows(BadRequestException::class.java) {
            taskService.updateTaskStatus(loginId, todo.id, toPending)
        }

        //todo -> in-progress (O)
        val toInProgress = ChangeStatusReq(to = Task.TaskStatus.IN_PROGRESS, memo = "to in-progress")
        var updatedTask = taskService.updateTaskStatus(loginId, todo.id, toInProgress)
        assertEquals(updatedTask.status, toInProgress.to)

        //todo -> done (O)
        val toDone = ChangeStatusReq(to = Task.TaskStatus.DONE, memo = "to done")
        updatedTask = taskService.updateTaskStatus(loginId, todo.id, toDone)
        assertEquals(updatedTask.status, toDone.to)
    }

    @Test
    fun PENDING_상태변경() {
        `when`(taskRepository.findByIdAndIsDeletedFalse(taskId)).thenReturn(pending)
        `when`(stateFactory.create(pending)).thenReturn(StatePending(taskHistoryRepository))
        `when`(userRepository.findUserByLoginIdAndIsDeletedFalseAndIsLoginTrue(loginId)).thenReturn(savedUser)

        //pending -> todo (O)
        val toTodo = ChangeStatusReq(to = Task.TaskStatus.TODO, memo = "to todo")
        var updatedTask = taskService.updateTaskStatus(loginId, todo.id, toTodo)
        assertEquals(updatedTask.status, toTodo.to)

        //pending -> in-progress (O)
        val toInProgress = ChangeStatusReq(to = Task.TaskStatus.IN_PROGRESS, memo = "to in-progress")
        updatedTask = taskService.updateTaskStatus(loginId, todo.id, toInProgress)
        assertEquals(updatedTask.status, toInProgress.to)

        //pending -> done (O)
        val toDone = ChangeStatusReq(to = Task.TaskStatus.DONE, memo = "to done")
        updatedTask = taskService.updateTaskStatus(loginId, todo.id, toDone)
        assertEquals(updatedTask.status, toDone.to)
    }

    @Test
    fun INPROGRESS_상태변경() {
        `when`(taskRepository.findByIdAndIsDeletedFalse(taskId)).thenReturn(inProgress)
        `when`(stateFactory.create(inProgress)).thenReturn(StateInProgress(taskHistoryRepository))
        `when`(userRepository.findUserByLoginIdAndIsDeletedFalseAndIsLoginTrue(loginId)).thenReturn(savedUser)

        //in-progress -> todo (O)
        val toTodo = ChangeStatusReq(to = Task.TaskStatus.TODO, memo = "to todo")
        var updatedTask = taskService.updateTaskStatus(loginId, todo.id, toTodo)
        assertEquals(updatedTask.status, toTodo.to)

        //in-progress -> pending (O)
        val toPending = ChangeStatusReq(to = Task.TaskStatus.PENDING, memo = "to pending")
        updatedTask = taskService.updateTaskStatus(loginId, todo.id, toPending)
        assertEquals(updatedTask.status, toPending.to)

        //in-progress -> done (O)
        val toDone = ChangeStatusReq(to = Task.TaskStatus.DONE, memo = "to done")
        updatedTask = taskService.updateTaskStatus(loginId, todo.id, toDone)
        assertEquals(updatedTask.status, toDone.to)
    }

    @Test
    fun DONE_상태변경() {
        `when`(taskRepository.findByIdAndIsDeletedFalse(taskId)).thenReturn(done)
        `when`(stateFactory.create(done)).thenReturn(StateDone(taskHistoryRepository))
        `when`(userRepository.findUserByLoginIdAndIsDeletedFalseAndIsLoginTrue(loginId)).thenReturn(savedUser)

        //done -> todo (O)
        val toTodo = ChangeStatusReq(to = Task.TaskStatus.TODO, memo = "to todo")
        var updatedTask = taskService.updateTaskStatus(loginId, todo.id, toTodo)
        assertEquals(updatedTask.status, toTodo.to)

        //done -> pending (O)
        val toPending = ChangeStatusReq(to = Task.TaskStatus.PENDING, memo = "to pending")
        assertThrows(BadRequestException::class.java) {
            taskService.updateTaskStatus(loginId, todo.id, toPending)
        }

        //done -> in-progress (O)
        val toInProgress = ChangeStatusReq(to = Task.TaskStatus.IN_PROGRESS, memo = "to in-progress")
        updatedTask = taskService.updateTaskStatus(loginId, todo.id, toInProgress)
        assertEquals(updatedTask.status, toInProgress.to)
    }
}