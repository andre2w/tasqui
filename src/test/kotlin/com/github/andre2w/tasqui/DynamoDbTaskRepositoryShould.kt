package com.github.andre2w.tasqui

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.*
import java.lang.IllegalStateException
import java.net.URI

class DynamoDbTaskRepositoryShould {

    @Test
    internal fun `add Task to DynamoDB`() {

        val endpoint = "http://localhost:8000"

        val dynamoDbClient = DynamoDbClient.builder()
            .endpointOverride(URI.create(endpoint))
            .build() ?: throw IllegalStateException()

        dynamoDbClient.createTable { builder ->
            builder.tableName("tasqui")

            builder.provisionedThroughput { provisionedThroughput ->
                provisionedThroughput.readCapacityUnits(5)
                provisionedThroughput.writeCapacityUnits(5)
            }

            builder.keySchema(
                KeySchemaElement.builder()
                    .attributeName("task_id")
                    .keyType(KeyType.HASH)
                    .build()
            )

            builder.attributeDefinitions(
                AttributeDefinition.builder()
                    .attributeName("task_id")
                    .attributeType(ScalarAttributeType.N)
                    .build()
            )
        }

        val task = Task(1, "Task description")

        val dynamoDbTaskRepository = DynamoDbTaskRepository(dynamoDbClient)
        dynamoDbTaskRepository.save(task)

        val item = dynamoDbClient.getItem(
                GetItemRequest.builder()
                    .tableName("tasqui")
                    .key(mapOf("task_id" to AttributeValue.builder().n("1").build()))
                    .build()).item()

        val storedTask = Task(item["task_id"]!!.n().toInt(), item["description"]!!.s())

        Assertions.assertEquals(storedTask, task)
    }
}