package com.github.andre2w.tasqui

import com.github.andre2w.tasqui.helpers.DynamoDBHelper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DynamoDbTaskRepositoryShould {

    @Test
    internal fun `add Task to DynamoDB`() {
        val dynamoDbHelper = DynamoDBHelper.connect()
        val task = Task(1, "Task description")

        val dynamoDbTaskRepository = DynamoDbTaskRepository(dynamoDbHelper.dynamoDbClient)
        dynamoDbTaskRepository.save(task)

        val storedTask = dynamoDbHelper.findById(task.id.toString())
        assertEquals(storedTask, task)
    }

}