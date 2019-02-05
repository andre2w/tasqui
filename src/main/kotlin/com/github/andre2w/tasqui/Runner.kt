package com.github.andre2w.tasqui

import com.github.ajalt.clikt.core.subcommands

class Runner {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val fileReader = JsonFileReader()
            val taskRepository = LocalFileTaskRepository(fileReader)
            val console = Console()

            Tasqui()
                .subcommands(Add(taskRepository),Tasks(taskRepository, console), Delete(taskRepository))
                .main(args)
        }
    }

}