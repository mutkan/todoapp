package com.mutlucelep.todoapp.data.source

import com.mutlucelep.todoapp.data.Task

interface TaskDataSource {
    interface LoadTasksCallback{
        fun onTasksLoaded(tasks: List<Task>)
        fun onDataNotAvailable()
    }

    interface GetTaskCallback{
        fun onTaskLoaded(task: Task)
        fun onDataNotAvailable()
    }

    fun getTasks(callback: LoadTasksCallback)
    fun completeTask(task: Task)
    fun activateTask(task: Task)
    fun deleteAllTask()
}