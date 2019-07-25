package com.mutlucelep.todoapp.utils

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

open class AppExecutors(
    val diskIO: Executor = DiskIOThreadExecutor(),
    val mainThreadHandler: Executor = MainThreadExecutor()
) {
    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable?) {
            mainThreadHandler.post(command)
        }
    }

    private class DiskIOThreadExecutor : Executor {
        private val diskIO = Executors.newSingleThreadExecutor()

        override fun execute(command: Runnable?) {
            diskIO.execute(command)
        }
    }
}