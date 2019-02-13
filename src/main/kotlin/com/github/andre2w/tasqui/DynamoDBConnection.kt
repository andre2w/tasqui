package com.github.andre2w.tasqui

import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import java.lang.IllegalStateException
import java.net.URI

class DynamoDBConnection {

    companion object {
        fun connect() : DynamoDbClient  {
            return DynamoDbClient.builder()
                .build() ?: throw IllegalStateException()
        }
    }

}