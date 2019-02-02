package com.github.andre2w.tasqui

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class AddShould {

    @Test
    internal fun `create task from argument`() {
        val taskRepository = mockk<TaskRepository>(relaxed = true)
        val taskText = "keep summer safe"

        every { taskRepository.nextId() } returns 1

        Add(taskRepository).main(arrayOf(taskText))

        verify {
            taskRepository.add(Task(1, taskText))
        }
    }
}