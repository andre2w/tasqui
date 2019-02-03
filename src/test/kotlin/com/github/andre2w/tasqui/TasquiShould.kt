package com.github.andre2w.tasqui

import com.github.salomonbrys.kotson.jsonArray
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TasquiShould {

    private val taskText = "Keep Summer safe"
    private val task = Task(1, taskText)
    private val taskRepository = mockk<TaskRepository>(relaxed = true)

    @BeforeEach
    internal fun setUp() {
        clearMocks(taskRepository)
    }

    @Test
    internal fun `create task from argument`() {
        every { taskRepository.nextId() } returns 1

        Add(taskRepository).main(arrayOf(taskText))

        verify {
            taskRepository.save(task)
        }
    }

    @Test
    internal fun `print tasks into the console`() {
        val console = mockk<Console>(relaxed = true)
        every { taskRepository.all() } returns listOf(task)

        ListTasks(taskRepository, console).main(emptyArray())

        verify {
            console.print("${task.id} - ${task.description}")
        }
    }

    @Test
    internal fun `delete task from list`() {
        Delete(taskRepository).main(arrayOf(task.id.toString()))

        verify {
            taskRepository.delete(task.id)
        }
    }
}