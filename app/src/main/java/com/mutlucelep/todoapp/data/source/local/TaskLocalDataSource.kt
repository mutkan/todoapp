package com.mutlucelep.todoapp.data.source.local

import com.mutlucelep.todoapp.data.Task
import com.mutlucelep.todoapp.data.source.TaskDataSource
import com.mutlucelep.todoapp.utils.AppExecutors

class TaskLocalDataSource(val appExecutors: AppExecutors, val taskDao: TaskDao): TaskDataSource{
    override fun getTasks(callback: TaskDataSource.LoadTasksCallback) {
       appExecutors.diskIO.execute {
           val tasks = taskDao.getTasks()
           appExecutors.mainThreadHandler.execute {
               if(tasks.isEmpty()){
                   callback.onDataNotAvailable()
               }else{
                   callback.onTasksLoaded(tasks)
               }
           }
       }
    }

    override fun getTask(taskId: String, callback: TaskDataSource.GetTaskCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clearCompletedTasks() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteTask(taskId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    override fun deleteAllTask() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun completeTask(completedTask: Task) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun completeTask(taskId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun activateTask(task: Task) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun activateTask(taskId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveTask(task: Task) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    companion object {
        private var INSTANCE: TaskLocalDataSource? = null

        @JvmStatic fun getInstance(appExecutors: AppExecutors, taskDao: TaskDao): TaskLocalDataSource{
            if (INSTANCE==null){
                synchronized(TaskLocalDataSource::javaClass){
                    INSTANCE = TaskLocalDataSource(appExecutors, taskDao)
                }
            }
            return INSTANCE!!
        }
    }

}