package com.github.andre2w.tasqui

import com.github.ajalt.clikt.core.subcommands

class Runner {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val taskRepository = DynamoDbTaskRepository(DynamoDBConnection.connect())
            val console = Console()

            Tasqui()
                .subcommands(Add(taskRepository),Tasks(taskRepository, console), Delete(taskRepository))
                .main(args)
        }
    }

}