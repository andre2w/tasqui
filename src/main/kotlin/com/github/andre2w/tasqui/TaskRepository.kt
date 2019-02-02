package com.github.andre2w.tasqui

import com.github.salomonbrys.kotson.fromJson
import com.github.salomonbrys.kotson.jsonObject
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject

class TaskRepository(private val fileReader: FileReader) {
    fun add(task: Task) {
        val gson = Gson()
        val tasks = gson.fromJson<JsonArray>(fileReader.read())
        tasks.add(task.toJson())
        fileReader.save(gson.toJson(tasks))
    }

    fun nextId(): Int {
        val tasks = Gson().fromJson<List<Task>>(fileReader.read())
        return (tasks.map { it.id }.max() ?: 0) + 1
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
