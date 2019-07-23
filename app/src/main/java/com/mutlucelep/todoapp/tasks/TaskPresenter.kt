package com.mutlucelep.todoapp.tasks

import com.mutlucelep.todoapp.data.Task
import com.mutlucelep.todoapp.data.source.TaskDataSource
import com.mutlucelep.todoapp.data.source.TaskRepository

class TaskPresenter(val taskRepository: TaskRepository, val taskView: TaskContractor.View) : TaskContractor.Presenter {
    override var currentFiltering = TaskFilterType.ALL_TASKS
    private var firstLoading = true

    init {
        taskView.presenter = this
    }

    override fun loadTasks(forceUpdate: Boolean) {
        loadTasks(forceUpdate || firstLoading, true)
        firstLoading = false
    }

    fun loadTasks(forceUpdate: Boolean, showLoadingUI: Boolean) {
        if (showLoadingUI)
            taskView.showLoadingIndicator(showLoadingUI)
        if (forceUpdate)
            taskRepository.refreshTasks()

        taskRepository.getTasks(object : TaskDataSource.LoadTasksCallback {
            override fun onTasksLoaded(tasks: List<Task>) {
                val tasksToShow = mutableListOf<Task>()
                for (task in tasks) {
                    when (currentFiltering) {
                        TaskFilterType.ALL_TASKS -> tasksToShow.add(task)
                        TaskFilterType.ACTIVE_TASKS -> if (task.isActive) {
                            tasksToShow.add(task)
                        }
                        TaskFilterType.COMPLETED_TASKS -> if (task.isCompleted) {
                            tasksToShow.add(task)
                        }
                    }
                }

                if (!taskView.isActive) {
                    return
                }

                if (showLoadingUI) {
                    taskView.showLoadingIndicator(false)
                }

                processTasks(tasksToShow)
            }

            override fun onDataNotAvailable() {
                if (!taskView.isActive) {
                    return
                }
                if (showLoadingUI) {
                    taskView.showLoadingIndicator(false)
                }

                taskView.showLoadingTasksError()
            }

        })
    }

    private fun processTasks(tasks: List<Task>) {
        if (tasks.isEmpty()) {
            processEmptyTask()
        } else {
            taskView.showTasks(tasks)
            showFilterLabel()
        }
    }

    private fun processEmptyTask() {
        when (currentFiltering) {
            TaskFilterType.COMPLETED_TASKS -> taskView.showNoCompletedTasks()
            TaskFilterType.ACTIVE_TASKS -> taskView.showNoActiveTasks()
            else -> taskView.showNoTasks()
        }
    }

    private fun showFilterLabel() {
        when (currentFiltering) {
            TaskFilterType.ACTIVE_TASKS -> taskView.showActiveFilterLabel()
            TaskFilterType.COMPLETED_TASKS -> taskView.showCompletedFilterLabel()
            else -> taskView.showAllFilterLabel()
        }
    }

    //    override fun completeTask(completedTask: Task) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun activateTask(activeTask: Task) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun clearCompletedTasks() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
    override fun addNewTask() {
        taskView.showAddTask()
    }

    override fun start() {
        loadTasks(false)
    }

    override fun openTaskDetail(requestedTask: Task) {
        taskView.showTaskDetailUi(requestedTask.id)
    }

    override fun completeTask(completedTask: Task) {
        taskRepository.completeTask(completedTask)
        taskView.showTaskMarkedCompleted()
        loadTasks(false, false)
    }

    override fun activateTask(activeTask: Task) {
        taskRepository.activateTask(activeTask)
        taskView.showTaskMarkedActive()
        loadTasks(false, false)
    }
}