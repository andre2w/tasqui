package com.github.andre2w.tasqui

import com.github.salomonbrys.kotson.get
import com.github.salomonbrys.kotson.jsonObject
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject

class TaskRepository(private val jsonFileReader: JsonFileReader) {
    fun add(task: Task) {
        val tasks = jsonFileReader.read() as JsonArray
        tasks.add(task.toJson())
        jsonFileReader.save(tasks)
    }

    fun nextId(): Int {
        val tasks = jsonFileReader.read() as JsonArray
        return (tasks.map { it["id"].asInt }.max() ?: 0) + 1
    }

    fun all(): List<Task> {
        val tasks = jsonFileReader.read() as JsonArray
        return tasks.map { it.toTask() }.toList()
    }

    private fun Task.toJson() : JsonObject {
        return jsonObject(
            "id" to this.id,
            "description" to this.description
        )
    }

    private fun JsonElement.toTask() = Task(this["id"].asInt, this["description"].asString)
}
