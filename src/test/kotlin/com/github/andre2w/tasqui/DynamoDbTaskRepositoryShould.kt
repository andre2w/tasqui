package com.github.andre2w.tasqui

import com.github.andre2w.tasqui.helpers.DynamoDBHelper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DynamoDbTaskRepositoryShould {

    private val dynamoDBHelper: DynamoDBHelper = DynamoDBHelper.connect()
    private lateinit var dynamoDbTaskRepository: DynamoDbTaskRepository

    @BeforeEach
    internal fun setUp() {
        dynamoDbTaskRepository = DynamoDbTaskRepository(dynamoDBHelper.dynamoDbClient)
        dynamoDBHelper.setupTable()
    }

    @Test
    internal fun `add Task to DynamoDB`() {
        val task = Task(1, "Task description")

        dynamoDbTaskRepository.save(task)

        val storedTask = dynamoDBHelper.findById(task.id.toString())
        assertEquals(storedTask, task)
    }

    @Test
    internal fun `retrieve all Tasks`() {
        val task1 = Task(1, "Task description")
        val task2 = Task(2, "Another task description")
        dynamoDBHelper.save(task1, task2)

        val tasks = dynamoDbTaskRepository.all()

        assertEquals(listOf(task2, task1), tasks)
    }
}