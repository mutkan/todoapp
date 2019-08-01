package com.mutlucelep.todoapp.addedittask

import com.mutlucelep.todoapp.BasePresenter
import com.mutlucelep.todoapp.BaseView

interface AddEditTaskContractor{
    interface View: BaseView<Presenter>{
        var isActive: Boolean
        fun setTitle(title: String)
        fun setDescription(description: String)
        fun showEmptyTaskError()
        fun showTasksList()

    }
    interface Presenter: BasePresenter{
        var isDataMissing: Boolean
        fun saveTask(title: String, description: String)
        fun populateTask()
    }
}