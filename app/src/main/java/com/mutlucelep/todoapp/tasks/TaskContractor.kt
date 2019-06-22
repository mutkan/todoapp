package com.mutlucelep.todoapp.tasks

import com.mutlucelep.todoapp.BasePresenter
import com.mutlucelep.todoapp.BaseView
import com.mutlucelep.todoapp.data.Task

interface TaskContractor {
    interface View: BaseView{
        var isActive: Boolean

        fun showTasks()
        fun showNoTasks()
        fun showNoActiveTasks()
        fun showNoCompletedTasks()
        fun showLoadingIndicator(active: Boolean)
        fun showLoadingTasksError()
        fun showTaskMarkedCompleted()
        fun showTaskMarkedActive()
        fun showCompletedTasksCleared()
        fun showActiveFilterLabel()
        fun showCompletedFilterLabel()
        fun showAllFilterLabel()
        fun showSuccessfulMessage()
        fun showFilteringPopUpMenu()
    }

    interface Presenter: BasePresenter{
        var currentFiltering: String

        fun loadTasks()
        fun completeTask(completedTask: Task)
        fun activateTask(activeTask: Task)
        fun clearCompletedTasks()
        fun addNewTask()
    }
}