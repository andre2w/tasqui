package com.github.andre2w.tasqui

import java.nio.file.Files
import java.nio.file.Paths

class FileReader {
    private val path = Paths.get("tasks.json")

    fun save(text: String) {
        Files.write(path, text.toByteArray())
    }

    fun read(): String {
        return Files.readAllLines(path).joinToString("")
    }

}
