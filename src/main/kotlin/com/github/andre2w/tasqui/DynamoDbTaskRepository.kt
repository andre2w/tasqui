package com.github.andre2w.tasqui

import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest

class DynamoDbTaskRepository(private val dynamoDbClient: DynamoDbClient) : TaskRepository {

    override fun all(): List<Task> {
        val scanResponse = dynamoDbClient.scan { scan ->
            scan.tableName("tasqui")
            scan.limit(1)
        }

        return scanResponse.items().map { it.toTask() }
    }

    override fun save(task: Task) {
        dynamoDbClient.putItem(
            PutItemRequest.builder()
                .tableName("tasqui")
                .item(task.toAttributeMap())
                .conditionExpression("attribute_not_exists(task_id)")
                .build())
    }

    private fun Task.toAttributeMap() : Map<String, AttributeValue> {
        return mapOf(
            "task_id" to id.toAttributeValue(),
            "description" to description.toAttributeValue()
        )
    }

    private fun Int.toAttributeValue() = AttributeValue.builder().n(this.toString()).build()

    private fun String.toAttributeValue() = AttributeValue.builder().s(this).build()

    private fun MutableMap<String, AttributeValue>.toTask() =
        Task(this["id"]!!.n().toInt(), this["description"]!!.s() )
}
