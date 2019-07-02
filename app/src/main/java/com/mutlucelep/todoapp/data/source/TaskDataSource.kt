package com.mutlucelep.todoapp.data.source

import com.mutlucelep.todoapp.data.Task

interface TaskDataSource {
    interface LoadTasksCallBack{
        fun onTasksLoaded(tasks: List<Task>)
        fun onDataNotAvailable()
    }

    fun getTasks(callback: LoadTasksCallBack)
}