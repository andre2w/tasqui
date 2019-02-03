package com.github.andre2w.tasqui

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class JsonFileReader {

    private val path: Path = Paths.get(System.getProperty("user.dir") + "/tasks.json")
    private val gson = Gson()

    fun save(text: JsonElement) {
        val tasksJson = gson.toJson(text)
        Files.write(path, tasksJson.toByteArray())
    }

    fun read(): JsonElement {
        if (!Files.exists(path)) {
            return JsonArray()
        }

        val storedTasks = Files.readAllLines(path).joinToString("")

        return gson.fromJson<JsonArray>(storedTasks)
    }

}
