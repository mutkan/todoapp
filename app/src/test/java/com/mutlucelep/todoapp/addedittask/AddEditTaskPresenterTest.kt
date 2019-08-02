package com.mutlucelep.todoapp.addedittask

import com.mutlucelep.todoapp.any
import com.mutlucelep.todoapp.capture
import com.mutlucelep.todoapp.data.Task
import com.mutlucelep.todoapp.data.source.TaskDataSource
import com.mutlucelep.todoapp.data.source.TaskRepository
import com.mutlucelep.todoapp.eq
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class AddEditTaskPresenterTest {
    @Mock
    lateinit var addEditTaskView: AddEditTaskContractor.View
    @Mock
    lateinit var taskRepository: TaskRepository
    @Captor
    lateinit var getTaskCallbackCaptor: ArgumentCaptor<TaskDataSource.GetTaskCallback>


    lateinit var addEditTaskPresenter: AddEditTaskPresenter
    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        `when`(addEditTaskView.isActive).thenReturn(true)
    }

    @Test
    fun createPresenter_checksThePresenterSetToTheView() {
        addEditTaskPresenter = AddEditTaskPresenter(null,true, taskRepository, addEditTaskView)

        verify(addEditTaskView).presenter = addEditTaskPresenter
    }

    @Test
    fun saveNewTaskToRepository_ShowTasksListScreen(){
        addEditTaskPresenter = AddEditTaskPresenter(null,true, taskRepository, addEditTaskView)

        addEditTaskPresenter.saveTask("TITLE1", "DESC")

        verify(taskRepository).saveTask(any<Task>())
        verify(addEditTaskView).showTasksList()
    }
}