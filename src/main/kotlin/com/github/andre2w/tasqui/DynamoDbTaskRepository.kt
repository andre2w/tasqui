package com.github.andre2w.tasqui

import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest

class DynamoDbTaskRepository(private val dynamoDbClient: DynamoDbClient) : TaskRepository {

    override fun save(task: Task) {
        val item = mapOf(
            "task_id" to AttributeValue.builder().n(task.id.toString()).build(),
            "description" to AttributeValue.builder().s(task.description).build()
        )

        dynamoDbClient.putItem(
            PutItemRequest.builder()
                .tableName("tasqui")
                .item(item)
                .conditionExpression("attribute_not_exists(task_id)")
                .build())
    }

}
