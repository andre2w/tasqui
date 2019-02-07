package com.github.andre2w.tasqui.helpers

import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.*
import java.lang.IllegalStateException
import java.net.URI

class DynamoDbHelper(val dynamoDbClient: DynamoDbClient) {


    companion object {

        fun connect(endpoint : String) : DynamoDbHelper {
            val dynamoDbClient = DynamoDbClient.builder()
                .endpointOverride(URI.create(endpoint))
                .build() ?: throw IllegalStateException()

            deleteExistingTable(dynamoDbClient)

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

            return DynamoDbHelper(dynamoDbClient)
        }

        private fun deleteExistingTable(dynamoDbClient: DynamoDbClient) {
            val tableExists = dynamoDbClient.listTables()
                .tableNames()
                .contains("tasqui")

            if (tableExists) {
                dynamoDbClient.deleteTable(
                    DeleteTableRequest.builder()
                        .tableName("tasqui")
                        .build()
                )
            }
        }
    }


}