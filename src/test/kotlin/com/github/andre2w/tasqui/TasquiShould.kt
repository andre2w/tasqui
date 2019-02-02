package com.github.andre2w.tasqui

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
            taskRepository.add(task)
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
}