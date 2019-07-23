package com.mutlucelep.todoapp.data.source

import com.mutlucelep.todoapp.data.Task

interface TaskDataSource {
    interface LoadTasksCallback {
        fun onTasksLoaded(tasks: List<Task>)
        fun onDataNotAvailable()
    }

    interface GetTaskCallback {
        fun onTaskLoaded(task: Task)
        fun onDataNotAvailable()
    }

    fun getTasks(callback: LoadTasksCallback)
    fun getTask(taskId: String, callback: GetTaskCallback)
    fun completeTask(task: Task)
    fun completeTask(taskId: String)
    fun activateTask(task: Task)
    fun activateTask(taskId: String)
    fun deleteAllTask()
    fun deleteTask(taskId: String)
    fun saveTask(task: Task)
    fun clearCompletedTasks()
}