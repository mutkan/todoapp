package com.mutlucelep.todoapp.addedittask

import com.mutlucelep.todoapp.data.Task
import com.mutlucelep.todoapp.data.source.TaskDataSource
import com.mutlucelep.todoapp.data.source.TaskRepository

class AddEditTaskPresenter(
    private val taskId: String?,
    override var isDataMissing: Boolean,
    val taskRepository: TaskRepository,
    val addEditTaskView: AddEditTaskContractor.View
) : AddEditTaskContractor.Presenter, TaskDataSource.GetTaskCallback {

    init {
        addEditTaskView.presenter = this
    }

    override fun populateTask() {
        taskRepository.getTask(taskId as String, this)
    }

    override fun saveTask(title: String, description: String) {
        if (taskId == null) {
            createTask(title, description)
        } else {
            updateTask(title, description)
        }
    }

    private fun createTask(title: String, description: String) {
        val newTask = Task(title, description)
        if (newTask.isEmpty) {
            addEditTaskView.showEmptyTaskError()
        } else {
            taskRepository.saveTask(newTask)
            addEditTaskView.showTasksList()
        }
    }

    private fun updateTask(title: String, description: String) {
        if (taskId == null) {
            throw RuntimeException("updateTask was called but task is new ")
        } // or we can add unsafe cast operator 'as'
        val updatedTask = Task(title, description, taskId)
        taskRepository.saveTask(updatedTask)
        addEditTaskView.showTasksList()
    }

    override fun start() {
        if (taskId != null && isDataMissing) {
            populateTask()
        }
    }

    override fun onTaskLoaded(task: Task) {
        if (addEditTaskView.isActive) {
            addEditTaskView.setTitle(task.title)
            addEditTaskView.setDescription(task.description)
        }
        isDataMissing = false
    }

    override fun onDataNotAvailable() {
        if (addEditTaskView.isActive) {
            addEditTaskView.showEmptyTaskError()
        }
    }
}