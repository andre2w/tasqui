package com.github.andre2w.tasqui.helpers

import com.github.andre2w.tasqui.Task
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.*
import java.lang.IllegalStateException
import java.net.URI

class DynamoDBHelper(val dynamoDbClient: DynamoDbClient) {

    private val primaryKey = "task_id"
    private val tableName = "tasqui"

    init {
        setupTable()
    }

    companion object {

        fun connect(endpoint: String = "http://localhost:8000"): DynamoDBHelper {
            val dynamoDbClient = DynamoDbClient.builder()
                .endpointOverride(URI.create(endpoint))
                .build() ?: throw IllegalStateException()

            return DynamoDBHelper(dynamoDbClient)
        }
    }

    fun findById(taskId: String): Task {
        val item = dynamoDbClient.getItem(
            GetItemRequest.builder()
                .tableName(tableName)
                .key(mapOf(primaryKey to AttributeValue.builder().n(taskId).build()))
                .build()
        ).item()

        if (item.isEmpty())
            throw ItemNotFoundInTable()

        return Task.from(item)
    }

    fun save(vararg tasks: Task) {
        tasks.forEach {
            dynamoDbClient.putItem(
                PutItemRequest.builder()
                    .tableName(tableName)
                    .item(it.toAttributeMap())
                    .conditionExpression("attribute_not_exists($primaryKey)")
                    .build())
        }
    }

    fun setupTable() {
        deleteTable()
        createTable()
    }

    private fun createTable() {
        dynamoDbClient.createTable { builder ->
            builder.tableName(tableName)

            builder.provisionedThroughput { provisionedThroughput ->
                provisionedThroughput.readCapacityUnits(5)
                provisionedThroughput.writeCapacityUnits(5)
            }

            builder.keySchema(
                KeySchemaElement.builder()
                    .attributeName(primaryKey)
                    .keyType(KeyType.HASH)
                    .build()
            )

            builder.attributeDefinitions(
                AttributeDefinition.builder()
                    .attributeName(primaryKey)
                    .attributeType(ScalarAttributeType.N)
                    .build()
            )
        }
    }

    private fun deleteTable() {
        val tableExists = dynamoDbClient.listTables()
            .tableNames()
            .contains(tableName)

        if (tableExists) {
            dynamoDbClient.deleteTable(
                DeleteTableRequest
                    .builder()
                    .tableName(tableName)
                    .build()
            )
        }
    }

    private fun Task.Companion.from(item: MutableMap<String, AttributeValue>) =
        Task(item[primaryKey]!!.n().toInt(), item["description"]!!.s())

    private fun Task.toAttributeMap() : Map<String, AttributeValue> {
        return mapOf(
            primaryKey to AttributeValue.builder().n(id.toString()).build(),
            "description" to AttributeValue.builder().s(description).build()
        )
    }
}