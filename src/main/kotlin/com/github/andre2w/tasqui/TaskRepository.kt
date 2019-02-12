package com.github.andre2w.tasqui

interface TaskRepository {
    fun save(task: Task)
    fun all(): List<Task>
    fun delete(id: Int)
    fun nextId(): Int
}