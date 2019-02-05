package com.github.andre2w.tasqui.integration

import org.junit.jupiter.api.Test
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.*
import java.net.URI


class DynamoDBTaskRepositoryShould {

    @Test
    internal fun `add Task to table`() {
        val dynamoDBHelper = DynamoDBHelper.build()
        dynamoDBHelper.createTasquiTable()

    }
}