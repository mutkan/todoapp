package com.mutlucelep.todoapp.data.source

import com.mutlucelep.todoapp.data.Task

class TaskRepository(val taskRemoteDataSource: TaskDataSource, val taskLocalDataSource: TaskDataSource) :
    TaskDataSource {

    var cachedTasks = LinkedHashMap<String, Task>()
    var cacheIsDirty = false

    override fun getTasks(callback: TaskDataSource.LoadTasksCallback) {
        if (cachedTasks.isNotEmpty() && !cacheIsDirty) {
            callback.onTasksLoaded(ArrayList(cachedTasks.values))
            return
        }

        if (cacheIsDirty) {
            getTasksFromRemoteDataSource(callback)
        } else {
            // Query the local data storage if available. If not, query the api
            taskLocalDataSource.getTasks(object : TaskDataSource.LoadTasksCallback {
                override fun onTasksLoaded(tasks: List<Task>) {
                    refreshCache(tasks)
                    callback.onTasksLoaded(tasks)
                }

                override fun onDataNotAvailable() {
                    getTasksFromRemoteDataSource(callback)
                }

            })
        }

    }

    override fun getTask(taskId: String, callback: TaskDataSource.GetTaskCallback) {
        val cachedTask = getTaskWithId(taskId)

        if(cachedTask != null){
            callback.onTaskLoaded(cachedTask)
            return
        }

        taskLocalDataSource.getTask(taskId, object: TaskDataSource.GetTaskCallback{
            override fun onTaskLoaded(task: Task) {
                cacheAndPerform(task){
                    callback.onTaskLoaded(it)
                }
            }

            override fun onDataNotAvailable() {
                taskRemoteDataSource.getTask(taskId, object: TaskDataSource.GetTaskCallback{
                    override fun onTaskLoaded(task: Task) {
                        cacheAndPerform(task){
                            callback.onTaskLoaded(it)
                        }
                    }

                    override fun onDataNotAvailable() {
                        callback.onDataNotAvailable()
                    }
                })
            }
        })
    }

    fun getTasksFromRemoteDataSource(callback: TaskDataSource.LoadTasksCallback) {
        taskRemoteDataSource.getTasks(object : TaskDataSource.LoadTasksCallback {
            override fun onTasksLoaded(tasks: List<Task>) {
                refreshCache(tasks)
                refreshLocalDataSource(tasks)

                callback.onTasksLoaded(ArrayList(cachedTasks.values))
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }

        })
    }

    fun refreshLocalDataSource(lists: List<Task>){
        taskLocalDataSource.deleteAllTask()
        for(task in lists){
            taskLocalDataSource.saveTask(task)
        }
    }

    fun refreshCache(tasks: List<Task>) {
        cachedTasks.clear()
        tasks.forEach {
            cacheAndPerform(it) {}
        }
        cacheIsDirty = false
    }

    fun refreshTasks() {
        cacheIsDirty = true
    }

    override fun clearCompletedTasks() {
        taskRemoteDataSource.clearCompletedTasks()
        taskLocalDataSource.clearCompletedTasks()

        cachedTasks = cachedTasks.filterValues {
            !it.isCompleted
        } as LinkedHashMap<String, Task>
    }


    fun cacheAndPerform(task: Task, perform: (Task) -> Unit) {
        val cachedTask =
            Task(title = task.title, description = task.description, id = task.id).apply {
                isCompleted = task.isCompleted
            }
        cachedTasks.put(cachedTask.id, cachedTask)

        perform(cachedTask)
    }

    override fun activateTask(task: Task) {
        cacheAndPerform(task){
            it.isCompleted = false
            taskLocalDataSource.activateTask(it)
            taskRemoteDataSource.activateTask(it)
        }
    }

    override fun activateTask(taskId: String) {
        getTaskWithId(taskId)?.let {
            activateTask(it)
        }
    }

    override fun completeTask(task: Task) {
        cacheAndPerform(task) {
            it.isCompleted = true
            taskLocalDataSource.completeTask(it)
            taskRemoteDataSource.completeTask(it)
        }
    }

    override fun completeTask(taskId: String) {
        getTaskWithId(taskId)?.let {
            completeTask(it)
        }
    }

    override fun deleteAllTask() {
        taskRemoteDataSource.deleteAllTask()
        taskLocalDataSource.deleteAllTask()
        cachedTasks.clear()
    }

    override fun saveTask(task: Task) {
        cacheAndPerform(task) {
            taskLocalDataSource.saveTask(it)
            taskRemoteDataSource.saveTask(it)
        }
    }

    override fun deleteTask(taskId: String) {
        taskRemoteDataSource.deleteTask(taskId)
        taskLocalDataSource.deleteTask(taskId)
        cachedTasks.remove(taskId)

    }
    private fun getTaskWithId(taskId: String) = cachedTasks[taskId]

    companion object {
        private var INSTANCE: TaskRepository? = null

        @JvmStatic
        fun getInstance(taskRemoteDataSource: TaskDataSource, taskLocalDataSource: TaskDataSource): TaskRepository {
            return INSTANCE ?: TaskRepository(taskRemoteDataSource, taskLocalDataSource).apply { INSTANCE = this }
        }

        @JvmStatic
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}