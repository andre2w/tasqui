package com.github.andre2w.tasqui

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.int

class Tasqui : CliktCommand() {
    override fun run() = Unit
}

class Add(private val taskRepository: TaskRepository) : CliktCommand("Add new task") {
    private val description by argument("description", "Task description")

    override fun run() {
        taskRepository.save(Task(taskRepository.nextId(), description))
    }
}

class Tasks(private val taskRepository: TaskRepository, private val console: Console)
    : CliktCommand("Prints all tasks") {

    override fun run() {
        val tasks = taskRepository.all()

        tasks.asReversed()
            .sortedBy { it.id }
            .map { "${it.id} - ${it.description}" }
            .forEach(console::print)
    }
}

class Delete(private val taskRepository: TaskRepository) : CliktCommand("Delete a task") {
    private val taskId by argument(help = "Id of the task to be deleted").int()

    override fun run() {
         taskRepository.delete(taskId)
    }
}
