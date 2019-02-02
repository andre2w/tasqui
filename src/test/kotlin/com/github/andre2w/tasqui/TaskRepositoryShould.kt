package com.github.andre2w.tasqui

import com.github.salomonbrys.kotson.jsonArray
import com.github.salomonbrys.kotson.jsonObject
import com.google.gson.JsonObject
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class TaskRepositoryShould {

    @Test
    internal fun `append task to json`() {
        val fileReader = mockk<FileReader>(relaxed = true)
        val task1 = Task(1, "Keep Summer safe")
        val task2 = Task(2, "Buy szechuan sauce")
        every { fileReader.read() } returns jsonArray(taskToJson(task1)).toString()

        TaskRepository(fileReader).add(task2)

        val expected = jsonArray(taskToJson(task1), taskToJson(task2)).toString()
        verify {
            fileReader.save(expected)
        }
    }

    private fun taskToJson(task: Task) : JsonObject {
        return jsonObject(
            "id" to task.id,
            "description" to task.description
        )
    }
}