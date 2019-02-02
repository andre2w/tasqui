package com.github.andre2w.tasqui

import com.github.salomonbrys.kotson.fromJson
import com.github.salomonbrys.kotson.get
import com.github.salomonbrys.kotson.jsonObject
import com.google.gson.Gson
import com.google.gson.JsonArray
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun Task.toJson() : JsonObject {
        return jsonObject(
            "id" to this.id,
            "description" to this.description
        )
    }
}
