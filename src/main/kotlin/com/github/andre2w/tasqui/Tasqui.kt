package com.github.andre2w.tasqui

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.int

class Tasqui : CliktCommand() {
    override fun run() = Unit
}

class Add(private val localFileTaskRepository: LocalFileTaskRepository) : CliktCommand("Add new task") {
    private val description by argument("description", "Task description")

    override fun run() {
        localFileTaskRepository.save(Task(localFileTaskRepository.nextId(), description))
    }
}

// Kotlin don't let me use List as the class name
class Tasks(private val localFileTaskRepository: LocalFileTaskRepository, private val console: Console)
    : CliktCommand("Prints all tasks") {

    override fun run() {
        val tasks = localFileTaskRepository.all()

        tasks.map { "${it.id} - ${it.description}" }
            .forEach(console::print)
    }
}

class Delete(private val localFileTaskRepository: LocalFileTaskRepository) : CliktCommand("Delete a task") {
    private val taskId by argument(help = "Id of the task to be deleted").int()

    override fun run() {
         localFileTaskRepository.delete(taskId)
    }
}
