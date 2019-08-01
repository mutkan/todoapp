package com.mutlucelep.todoapp.tasks

import com.mutlucelep.todoapp.BasePresenter
import com.mutlucelep.todoapp.BaseView
import com.mutlucelep.todoapp.data.Task

interface TaskContractor {
    interface View: BaseView <Presenter>{
        var isActive: Boolean

        fun showTasks(tasks: List<Task>)
        fun showNoTasks()
        fun showNoActiveTasks()
        fun showNoCompletedTasks()
        fun showLoadingIndicator(active: Boolean)
        fun showLoadingTasksError()
        fun showAddTask()
        fun showTaskMarkedCompleted()
        fun showTaskMarkedActive()
//        fun showCompletedTasksCleared()
        fun showActiveFilterLabel()
        fun showCompletedFilterLabel()
        fun showAllFilterLabel()
        fun showTaskDetailUi(taskId: String)
//        fun showSuccessfulMessage()
//        fun showFilteringPopUpMenu()
    }

    interface Presenter: BasePresenter{
        var currentFiltering: TaskFilterType

        fun loadTasks(forceUpdate: Boolean)
        fun openTaskDetail(requestedTask: Task)
        fun completeTask(completedTask: Task)
        fun activateTask(activeTask: Task)
//        fun clearCompletedTasks()
        fun addNewTask()

    }
}