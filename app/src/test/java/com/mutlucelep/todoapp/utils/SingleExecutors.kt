package com.mutlucelep.todoapp.utils

import java.util.concurrent.Executor

class SingleExecutors : AppExecutors(instant) {
    companion object {
        private val instant = Executor { command -> command.run() }
    }
}