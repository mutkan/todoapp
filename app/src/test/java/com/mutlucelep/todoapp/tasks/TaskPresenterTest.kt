package com.mutlucelep.todoapp.tasks

import com.google.common.collect.Lists
import com.mutlucelep.todoapp.argumentCaptor
import com.mutlucelep.todoapp.capture
import com.mutlucelep.todoapp.data.Task
import com.mutlucelep.todoapp.data.source.TaskDataSource
import com.mutlucelep.todoapp.data.source.TaskRepository
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.*
import org.mockito.Mockito.*

class TaskPresenterTest {
    @Mock
    private lateinit var taskRepository: TaskRepository
    @Mock
    private lateinit var taskView: TaskContractor.View
    @Captor
    lateinit var loadTaskCallbackCaptor: ArgumentCaptor<TaskDataSource.LoadTasksCallback>

    private lateinit var taskPresenter: TaskPresenter
    private lateinit var tasks: MutableList<Task>

    @Before
    fun setupTaskPresenter() {
        MockitoAnnotations.initMocks(this)

        taskPresenter = TaskPresenter(taskRepository, taskView)
        `when`(taskView.isActive).thenReturn(true);

        tasks = Lists.newArrayList(Task("title1", "description1"), Task("title2", "description2").apply {
            isCompleted = true
        }, Task("title3", "description3").apply {
            isCompleted = true
        })
    }

    @Test
    fun createPresenter_setThePresenterToView() {
        taskPresenter = TaskPresenter(taskRepository, taskView)

        verify(taskView).presenter = taskPresenter
    }

    @Test
    fun loadAllTasksFromRepositoryAndLoadIntoView() {
        with(taskPresenter) {
            currentFiltering = TaskFilterType.ALL_TASKS
            loadTasks(true)
        }

        verify(taskRepository).getTasks(capture(loadTaskCallbackCaptor))
        loadTaskCallbackCaptor.value.onTasksLoaded(tasks)

        val inorder = inOrder(taskView)

        inorder.verify(taskView).showLoadingIndicator(true)
        inorder.verify(taskView).showLoadingIndicator(false)

        val showTasksArgumentCaptor = argumentCaptor<List<Task>>()
        verify(taskView).showTasks(capture(showTasksArgumentCaptor))
        assertTrue(showTasksArgumentCaptor.value.size == 3)
    }

    @Test
    fun loadActiveTasksFromRepositoryAndLoadIntoView(){
        with(taskPresenter){
            currentFiltering = TaskFilterType.ACTIVE_TASKS
            loadTasks(true)
        }

        verify(taskRepository).getTasks(capture(loadTaskCallbackCaptor))
        loadTaskCallbackCaptor.value.onTasksLoaded(tasks)

        val inorder = inOrder(taskView)
        inorder.verify(taskView).showLoadingIndicator(true)
        inorder.verify(taskView).showLoadingIndicator(false)

        val showTasksArgumentCaptor = argumentCaptor<List<Task>>()
        verify(taskView).showTasks(capture(showTasksArgumentCaptor))
        assertTrue(showTasksArgumentCaptor.value.size == 1)
    }

    @Test
    fun loadCompletedTaskFromRepositoryAndLoadIntoView(){
        with(taskPresenter){
            currentFiltering = TaskFilterType.COMPLETED_TASKS
            loadTasks(true)
        }

        verify(taskRepository).getTasks(capture(loadTaskCallbackCaptor))
        loadTaskCallbackCaptor.value.onTasksLoaded(tasks)

        val inorder = inOrder(taskView)
        inorder.verify(taskView).showLoadingIndicator(true)
        inorder.verify(taskView).showLoadingIndicator(false)

        val showTasksArgumentCaptor = argumentCaptor<List<Task>>()
        verify(taskView).showTasks(capture(showTasksArgumentCaptor))
        assertTrue(showTasksArgumentCaptor.value.size == 2)
    }

    @Test
    fun clickOnFab_ShowsAddTaskUi(){
        taskPresenter.addNewTask()

        verify(taskView).showAddTask()
    }

    @Test
    fun clickOnTask_ShowDetailTaskUi(){
        val requestedTask = Task("Title", "Description")
        taskPresenter.openTaskDetail(requestedTask)

        verify(taskView).showTaskDetailUi(ArgumentMatchers.anyString())
    }

    @Test
    fun completeTask_ShowsTaskMarkedComplete(){
        val task = Task("Title Completed", "Description Completed")

        taskPresenter.completeTask(task)

        verify(taskRepository).completeTask(task)
        verify(taskView).showTaskMarkedCompleted()

    }

    @Test
    fun activateTask_ShowsTaskMarkedActive(){
        val task = Task("Completed Task", "Description").apply {
            isCompleted = true
        }

        taskPresenter.activateTask(task)

        verify(taskRepository).activateTask(task)
        verify(taskView).showTaskMarkedActive()
    }


    @Test
    fun unavailableTasks_ShowsError(){
        with(taskPresenter){
            currentFiltering = TaskFilterType.ALL_TASKS
            loadTasks(true)
        }

        verify(taskRepository).getTasks(capture(loadTaskCallbackCaptor))
        loadTaskCallbackCaptor.value.onDataNotAvailable()

        verify(taskView).showLoadingTasksError()
    }

    @Test
    fun processEmptyTask_showNoTasks(){
        with(taskPresenter){
            currentFiltering = TaskFilterType.ALL_TASKS
            loadTasks(true)
        }

        verify(taskRepository).getTasks(capture(loadTaskCallbackCaptor))
        tasks = Lists.newArrayList()
        loadTaskCallbackCaptor.value.onTasksLoaded(tasks)

        val inOrder = inOrder(taskView)
        inOrder.verify(taskView).showLoadingIndicator(true)
        inOrder.verify(taskView).showLoadingIndicator(false)


        verify(taskView).showNoTasks()

    }

    @Test
    fun processEmptyTask_showNoCompletedTasks(){
        with(taskPresenter){
            currentFiltering = TaskFilterType.COMPLETED_TASKS
            loadTasks(true)
        }

        verify(taskRepository).getTasks(capture(loadTaskCallbackCaptor))
        tasks = Lists.newArrayList()
        loadTaskCallbackCaptor.value.onTasksLoaded(tasks)

        val inOrder = inOrder(taskView)
        inOrder.verify(taskView).showLoadingIndicator(true)
        inOrder.verify(taskView).showLoadingIndicator(false)


        verify(taskView).showNoCompletedTasks()

    }

    @Test
    fun processEmptyTask_showActiveTasks(){
        with(taskPresenter){
            currentFiltering = TaskFilterType.ACTIVE_TASKS
            loadTasks(true)
        }

        verify(taskRepository).getTasks(capture(loadTaskCallbackCaptor))
        tasks = Lists.newArrayList()
        loadTaskCallbackCaptor.value.onTasksLoaded(tasks)

        val inOrder = inOrder(taskView)
        inOrder.verify(taskView).showLoadingIndicator(true)
        inOrder.verify(taskView).showLoadingIndicator(false)


        verify(taskView).showNoActiveTasks()

    }
}