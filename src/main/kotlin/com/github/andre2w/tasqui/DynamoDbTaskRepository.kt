package com.github.andre2w.tasqui

import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest

class DynamoDbTaskRepository(private val dynamoDbClient: DynamoDbClient) : TaskRepository {

    private val tableName = "tasqui"

    override fun delete(id: Int) {
        dynamoDbClient.deleteItem { delete ->
            delete.tableName(tableName)
            delete.key(mapOf("task_id" to id.toAttributeValue()))
        }
    }

    override fun all(): List<Task> {
        val scanResponse = dynamoDbClient.scan { scan ->
            scan.tableName(tableName)
        }

        return scanResponse.items().map { it.toTask() }
    }

    override fun save(task: Task) {
        dynamoDbClient.putItem(
            PutItemRequest.builder()
                .tableName(tableName)
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
        Task(this["task_id"]!!.n().toInt(), this["description"]!!.s() )
}
