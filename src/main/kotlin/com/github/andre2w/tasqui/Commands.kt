package com.github.andre2w.tasqui

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument

class Add(private val taskRepository: TaskRepository) : CliktCommand("Add new task") {
    private val description by argument("description", "Task description")

    override fun run() {
        taskRepository.add(Task(taskRepository.nextId(), description))

    }
}

// Kotlin don't let me use List as the class name
class ListTasks(private val taskRepository: TaskRepository, private val console: Console)
    : CliktCommand("Prints all tasks") {


    override fun run() {
        val tasks = taskRepository.all()
        
        tasks.map { "${it.id} - ${it.description}" }
            .forEach(console::print)
    }

}
