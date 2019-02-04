package com.github.andre2w.tasqui

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class JsonFileReader {

    private val path: Path
    private val gson = Gson()

    init {
        initializeFolder()
        path = Paths.get(System.getProperty("user.home") + "/.tasqui/tasks.json")
    }

    private fun initializeFolder() {
        val tasquiFolder = Paths.get(System.getProperty("user.home") + "/.tasqui")
        if (!Files.exists(tasquiFolder)) {
            Files.createDirectories(tasquiFolder)
        }
    }

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
