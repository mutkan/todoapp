package com.mutlucelep.todoapp.data.source.utils

import com.mutlucelep.todoapp.utils.AppExecutors
import java.util.concurrent.Executor

class SingleExecutors : AppExecutors(instant, instant) {
    companion object {
        private val instant = Executor { command -> command.run() }
    }
}