package com.mutlucelep.todoapp.data

import com.google.common.collect.Lists
import com.mutlucelep.todoapp.data.source.TaskDataSource


class FakeTaskRemoteDataSource: TaskDataSource{
    private val TASKS_SERVICE_DATA = LinkedHashMap<String, Task>()

    override fun getTasks(callback: TaskDataSource.LoadTasksCallback) {
        callback.onTasksLoaded(Lists.newArrayList(TASKS_SERVICE_DATA.values))
    }

    override fun getTask(taskId: String, callback: TaskDataSource.GetTaskCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clearCompletedTasks() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun completeTask(completedTask: Task) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun activateTask(task: Task) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun activateTask(taskId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllTask() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveTask(task: Task) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun completeTask(taskId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteTask(taskId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object{
        private var INSTANCE: FakeTaskRemoteDataSource? = null
        @JvmStatic fun getInstance(): FakeTaskRemoteDataSource{
            return INSTANCE ?: FakeTaskRemoteDataSource().apply { INSTANCE = this }
        }
    }
}