package com.mutlucelep.todoapp.addedittask

import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class AddEditTaskPresenterTest {
    @Mock lateinit var addEditTaskView: AddEditTaskContractor.View
    @Before
    fun setUp(){
        MockitoAnnotations.initMocks(this)
        `when`(addEditTaskView.isActive).thenReturn(true)
    }


}