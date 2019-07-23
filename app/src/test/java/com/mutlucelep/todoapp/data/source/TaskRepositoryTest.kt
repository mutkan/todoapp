package com.mutlucelep.todoapp.data.source

import com.google.common.collect.Lists
import com.mutlucelep.todoapp.any
import com.mutlucelep.todoapp.capture
import com.mutlucelep.todoapp.data.Task
import com.mutlucelep.todoapp.eq
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.*
import org.mockito.Mockito.*

class TaskRepositoryTest {
    private val TASK_TITLE_1 = "TITLE1"
    private val TASK_TITLE_2 = "TITLE2"
    private val TASK_TITLE_3 = "TITLE3"
    private val TASK_DESCRIPTION_1 = "DESCRIPTION1"
    private val TASK_DESCRIPTION_2 = "DESCRIPTION2"
    private val TASKS =
        Lists.newArrayList(Task(TASK_TITLE_1, TASK_DESCRIPTION_1), Task(TASK_TITLE_2, TASK_DESCRIPTION_2))
    private lateinit var taskRepository: TaskRepository

    @Mock
    private lateinit var remoteDataSource: TaskDataSource
    @Mock
    private lateinit var localDataSource: TaskDataSource
    @Mock
    private lateinit var loadTasksCallback: TaskDataSource.LoadTasksCallback
    @Mock
    private lateinit var getTaskCallback: TaskDataSource.GetTaskCallback

    @Captor
    private lateinit var taskCallbackCaptor: ArgumentCaptor<TaskDataSource.GetTaskCallback>
    @Captor
    private lateinit var tasksCallbackCaptor: ArgumentCaptor<TaskDataSource.LoadTasksCallback>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        taskRepository = TaskRepository.getInstance(remoteDataSource, localDataSource)
    }

    @After
    fun endUp() {
        TaskRepository.destroyInstance()
    }


    @Test
    fun getTasks_requestAllTasksFromLocalDataSource() {
        // When Tasks are requested from the task repository
        taskRepository.getTasks(loadTasksCallback)

        // Then Tasks are loaded from the local data source
        verify(localDataSource).getTasks(any<TaskDataSource.LoadTasksCallback>())
    }

    @Test
    fun getTasks_checksCachedTasksAfterRequestLocalDataSource() {
        // When Tasks are requested from the task repository
        taskRepository.getTasks(loadTasksCallback)

        // Then Tasks are loaded from the local data source
        verify(localDataSource).getTasks(capture(tasksCallbackCaptor))
        tasksCallbackCaptor.value.onTasksLoaded(TASKS)

        assertThat(taskRepository.cachedTasks.size, `is`(2))
    }

    @Test
    fun saveTask_savesTaskToServiceAPI() {
        //Given a stub task with title and description
        val stubTask = Task(TASK_TITLE_1, TASK_DESCRIPTION_1)

        // When a task is saved to task repository
        taskRepository.saveTask(stubTask)

        // Then the service API and persistence repository are called and the cache is updated
        verify(remoteDataSource).saveTask(stubTask)
        verify(localDataSource).saveTask(stubTask)

        assertThat(taskRepository.cachedTasks.size, `is`(1))
    }

    @Test
    fun completeTask_completesTaskToServiceAPIAndUpdatesCache() {
        //Given a stub active task with title and description added to the repository
        with(taskRepository) {
            val stubTask = Task(TASK_TITLE_1, TASK_DESCRIPTION_1)
            saveTask(stubTask)

            //When a task is completed to task repository
            completeTask(stubTask)

            // Then the service API and persistent repository is called and the cache is updated
            verify(localDataSource).completeTask(stubTask)
            verify(remoteDataSource).completeTask(stubTask)
            assertThat(cachedTasks.size, `is`(1))
            val cachedCompletedTask = cachedTasks[stubTask.id]
            assertNotNull(cachedCompletedTask as Task)
            assertThat(cachedCompletedTask.isCompleted, `is`(true))
            assertThat(cachedCompletedTask.isActive, `is`(false))
        }

    }

    @Test
    fun completeTaskId_completesTaskToServiceAPIAndUpdatesCache() {
        //Given a stub active task with title and description added to the repository
        val newTask = Task(TASK_TITLE_1, TASK_DESCRIPTION_1)
        with(taskRepository) {
            saveTask(newTask)

            //When a task is completed using its id
            completeTask(newTask.id)

            //Then the service API and persistent repository is called and the cache is updated

            verify(remoteDataSource).completeTask(newTask)
            verify(localDataSource).completeTask(newTask)
            assertThat(cachedTasks.size, `is`(1))
            val cachedCompletedTask = cachedTasks[newTask.id]
            assertNotNull(cachedCompletedTask as Task)
            assertThat(cachedCompletedTask.isCompleted, `is`(true))
        }
    }

    @Test
    fun activeTask_activatesTaskToServiceAPIAndUpdatesCache() {
        val newTask = Task(TASK_TITLE_2, TASK_DESCRIPTION_2)
        with(taskRepository) {
            saveTask(newTask)
            activateTask(newTask)
            verify(remoteDataSource).activateTask(newTask)
            verify(localDataSource).activateTask(newTask)
            assertThat(cachedTasks.size, `is`(1))
            val newActivatedTaskInCache = cachedTasks[newTask.id]
            assertNotNull(newActivatedTaskInCache as Task)
            assertThat(newActivatedTaskInCache.isActive, `is`(true))
        }
    }

    @Test
    fun activeTaskId_activatesTaskToServiceAPIAndUpdatesCache() {
        val newTask = Task(TASK_TITLE_1, TASK_DESCRIPTION_1)
        with(taskRepository) {
            saveTask(newTask)
            activateTask(newTask.id)

            verify(remoteDataSource).activateTask(newTask)
            verify(localDataSource).activateTask(newTask)
            assertThat(cachedTasks.size, `is`(1))
            val newActivatedTaskInCache = cachedTasks[newTask.id]
            assertNotNull(newActivatedTaskInCache as Task)
            assertThat(newActivatedTaskInCache.isCompleted, `is`(false))
        }
    }

    @Test
    fun getTask_requestsSingleTaskFromLocalDataSource() {
        // When a task is requested from the task repository
        taskRepository.getTask(TASK_TITLE_1, getTaskCallback)

        verify(localDataSource).getTask(eq(TASK_TITLE_1), any<TaskDataSource.GetTaskCallback>())
    }

    @Test
    fun deleteCompletedTasks_deletesCompletedTasksToServiceAPIandUpdateCache() {
        with(taskRepository) {
            //Given 2 stub active tasks and 1 stub completed task in the repository
            val activeTask1 = Task(TASK_TITLE_1, TASK_DESCRIPTION_1)
            val activeTask2 = Task(TASK_TITLE_2, TASK_DESCRIPTION_2)
            val completedTask = Task(TASK_TITLE_3, TASK_DESCRIPTION_1).apply { isCompleted = true }
            saveTask(activeTask1)
            saveTask(activeTask2)
            saveTask(completedTask)

            //When a completed tasks are cleared to task repository

            clearCompletedTasks()

            //Then the service API and persistent repository are called and the cache is updated

            verify(remoteDataSource).clearCompletedTasks()
            verify(localDataSource).clearCompletedTasks()
            assertThat(cachedTasks.size, `is`(2))
            val deletedTask = cachedTasks[completedTask.id]
            assertNull(deletedTask)
        }
    }

    @Test
    fun deleteAllTasks_deletesAllTasksToServiceAPIAndUpdateCache() {
        with(taskRepository) {
            val activeTask1 = Task(TASK_TITLE_1, TASK_DESCRIPTION_1)
            val activeTask2 = Task(TASK_TITLE_2, TASK_DESCRIPTION_2)
            val completedTask = Task(TASK_TITLE_3, TASK_DESCRIPTION_1).apply { isCompleted = true }
            saveTask(activeTask1)
            saveTask(activeTask2)
            saveTask(completedTask)

            deleteAllTask()

            verify(remoteDataSource).deleteAllTask()
            verify(localDataSource).deleteAllTask()
            assertThat(cachedTasks.size, `is`(0))
        }
    }

    @Test
    fun deleteTask_deletesTaskToServiceAPIAndRemovedFromCache() {
        with(taskRepository) {
            val newTask = Task(TASK_TITLE_1, TASK_DESCRIPTION_1)
            saveTask(newTask)
            assertThat(cachedTasks.containsKey(newTask.id), `is`(true))

            deleteTask(newTask.id)

            verify(remoteDataSource).deleteTask(newTask.id)
            verify(localDataSource).deleteTask(newTask.id)

            assertThat(cachedTasks.containsKey(newTask.id), `is`(false))
        }
    }

    @Test
    fun getTasksWithDirtyCache_tasksAreLoadedFromRemoteDataSource() {
        with(taskRepository) {
            refreshTasks()
            getTasks(loadTasksCallback)
        }

        setTasksAvailable(remoteDataSource, TASKS)

        verify(localDataSource, never()).getTasks(loadTasksCallback)
        verify(loadTasksCallback).onTasksLoaded(TASKS)
    }

    @Test
    fun getTasksWithLocalDataSourceUnavailable_tasksAreRetrievedFromRemote() {
        taskRepository.getTasks(loadTasksCallback)

        setTasksNotAvailable(localDataSource)
        setTasksAvailable(remoteDataSource, TASKS)

        verify(loadTasksCallback).onTasksLoaded(TASKS)
    }

    @Test
    fun getTasksWithBothDataSourcesUnavailable_firesOnDataUnavailable() {
        taskRepository.getTasks(loadTasksCallback)

        setTasksNotAvailable(localDataSource)
        setTasksNotAvailable(remoteDataSource)

        verify(loadTasksCallback).onDataNotAvailable()

    }

    @Test
    fun getTaskWithBothDatasourceUnavailable_firesOnDataUnavailable() {
        val taskId = "12345"
        taskRepository.getTask(taskId,getTaskCallback)

        setTaskNotAvailable(localDataSource, taskId)
        setTaskNotAvailable(remoteDataSource, taskId)

        verify(getTaskCallback).onDataNotAvailable()

    }

    @Test
    fun getTasks_refreshesLocalDataSource(){
        with(taskRepository){
            refreshTasks()
            getTasks(loadTasksCallback)
        }

        setTasksAvailable(remoteDataSource,TASKS)

        verify(localDataSource, times(TASKS.size)).saveTask(any<Task>())
    }

    private fun setTasksAvailable(dataSource: TaskDataSource, lists: List<Task>) {
        verify(dataSource).getTasks(capture(tasksCallbackCaptor))
        tasksCallbackCaptor.value.onTasksLoaded(lists)
    }

    private fun setTasksNotAvailable(dataSource: TaskDataSource) {
        verify(dataSource).getTasks(capture(tasksCallbackCaptor))
        tasksCallbackCaptor.value.onDataNotAvailable()
    }

    private fun setTaskNotAvailable(dataSource: TaskDataSource, taskId: String) {
        verify(dataSource).getTask(eq(taskId), capture(taskCallbackCaptor))
        taskCallbackCaptor.value.onDataNotAvailable()
    }
}