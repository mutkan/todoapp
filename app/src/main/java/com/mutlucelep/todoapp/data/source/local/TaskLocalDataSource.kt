package com.mutlucelep.todoapp.data.source.local

import com.mutlucelep.todoapp.data.Task
import com.mutlucelep.todoapp.data.source.TaskDataSource
import com.mutlucelep.todoapp.utils.AppExecutors

class TaskLocalDataSource(val appExecutors: AppExecutors, val taskDao: TaskDao) : TaskDataSource {
    override fun getTasks(callback: TaskDataSource.LoadTasksCallback) {
        appExecutors.diskIO.execute {
            val tasks = taskDao.getTasks()
            appExecutors.mainThreadHandler.execute {
                if (tasks.isEmpty()) {
                    callback.onDataNotAvailable()
                } else {
                    callback.onTasksLoaded(tasks)
                }
            }
        }
    }

    override fun getTask(taskId: String, callback: TaskDataSource.GetTaskCallback) {
        appExecutors.diskIO.execute {
            val task = taskDao.getTaskById(taskId)
            appExecutors.mainThreadHandler.execute {
                if (task != null) {
                    callback.onTaskLoaded(task)
                } else {
                    callback.onDataNotAvailable()
                }
            }
        }
    }

    override fun clearCompletedTasks() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteTask(taskId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllTask() {
       appExecutors.diskIO.execute {
           taskDao.deleteTasks()
       }
    }

    override fun completeTask(completedTask: Task) {
        appExecutors.diskIO.execute {
            taskDao.updateCompletedTask(completedTask.id, true)
        }
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
        appExecutors.diskIO.execute { taskDao.insertTask(task) }
    }

    companion object {
        private var INSTANCE: TaskLocalDataSource? = null

        @JvmStatic
        fun getInstance(appExecutors: AppExecutors, taskDao: TaskDao): TaskLocalDataSource {
            if (INSTANCE == null) {
                synchronized(TaskLocalDataSource::javaClass) {
                    INSTANCE = TaskLocalDataSource(appExecutors, taskDao)
                }
            }
            return INSTANCE!!
        }

        fun clearInstance() {
            INSTANCE = null
        }
    }

}