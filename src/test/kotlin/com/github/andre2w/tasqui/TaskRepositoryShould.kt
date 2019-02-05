package com.github.andre2w.tasqui

import com.github.salomonbrys.kotson.jsonArray
import com.github.salomonbrys.kotson.jsonObject
import com.google.gson.JsonObject
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class TaskRepositoryShould {

    private val jsonFileReader = mockk<JsonFileReader>(relaxed = true)
    private val taskRepository = LocalFileTaskRepository(jsonFileReader)
    private val task1 = Task(1, "Keep Summer safe")
    private val task2 = Task(2, "Buy szechuan sauce")
    private val jsonTasks = listOf(taskToJson(task1), taskToJson(task2))

    @BeforeEach
    internal fun setUp() {
        clearMocks(jsonFileReader)
    }

    @Test
    internal fun `append task to json`() {
        every { jsonFileReader.read() } returns jsonArray(taskToJson(task1))

        taskRepository.save(task2)

        val expected = jsonArray(taskToJson(task1), taskToJson(task2))
        verify {
            jsonFileReader.save(expected)
        }
    }

    @Test
    internal fun `return next id`() {
        every { jsonFileReader.read() } returns jsonArray(jsonTasks)

        assertEquals(3, taskRepository.nextId())
    }

    @Test
    internal fun `return all tasks stored`() {
        every { jsonFileReader.read() } returns jsonArray(jsonTasks)

        val tasks = taskRepository.all()

        assertEquals(listOf(task1, task2), tasks)
    }

    @Test
    internal fun `delete task from json`() {
        every { jsonFileReader.read() } returns jsonArray(jsonTasks)

        taskRepository.delete(1)

        verify {
            jsonFileReader.save(jsonArray(taskToJson(task2)))
        }
    }

    private fun taskToJson(task: Task) : JsonObject {
        return jsonObject(
            "id" to task.id,
            "description" to task.description
        )
    }
}