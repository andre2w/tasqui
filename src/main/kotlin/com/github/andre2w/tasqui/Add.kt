package com.github.andre2w.tasqui

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument

class Add(private val taskRepository: TaskRepository) : CliktCommand("Add new task") {
    private val description by argument("description", "Task description")

    override fun run() {
        taskRepository.add(Task(taskRepository.nextId(), description))

    }

}
