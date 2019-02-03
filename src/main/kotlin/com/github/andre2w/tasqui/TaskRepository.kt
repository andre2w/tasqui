package com.github.andre2w.tasqui

import com.github.salomonbrys.kotson.get
import com.github.salomonbrys.kotson.jsonObject
import com.github.salomonbrys.kotson.toJsonArray
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject

class TaskRepository(private val jsonFileReader: JsonFileReader) {

    private val tasks: JsonArray
        get() = jsonFileReader.read() as JsonArray

    fun save(task: Task) {
        val storedTasks = tasks
        storedTasks.add(task.toJson())
        jsonFileReader.save(storedTasks)
    }

    fun nextId(): Int {
        return (tasks.map { it["id"].asInt }.max() ?: 0) + 1
    }

    fun all(): List<Task> {
        return tasks.map { it.toTask() }.toList()
    }

    fun delete(id: Int) {
        val updatedTasks = tasks
            .filter { it["id"].asInt != id }
            .toJsonArray()

        jsonFileReader.save(updatedTasks)
    }

    private fun Task.toJson() : JsonObject {
        return jsonObject(
            "id" to this.id,
            "description" to this.description
        )
    }

    private fun JsonElement.toTask() = Task(this["id"].asInt, this["description"].asString)
}
