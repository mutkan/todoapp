package com.mutlucelep.todoapp.tasks

import android.view.View
import android.widget.ListView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.google.common.base.Preconditions
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class TasksScreenTest {
    @Rule @JvmField var tasksActivityTestRule = object :
        ActivityTestRule<TaskActivity>(TaskActivity::class.java) {

        /**
         * To avoid a long list of tasks and the need to scroll through the list to find a
         * task, we call [TasksDataSource.deleteAllTasks] before each test.
         */
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            Injection.provideTaskRepository(InstrumentationRegistry.getTargetContext()).deleteAllTask()
        }
    }

    /**
     *  A custom [Matcher] which matches an item in a [RecyclerView] by its text.
     */
    private fun withItemText(itemText: String): Matcher<View> {
        Preconditions.checkArgument(itemText.isNotEmpty(), "itemText cannot be null or empty")
        return object : TypeSafeMatcher<View>() {
            public override fun matchesSafely(item: View): Boolean {
                return Matchers.allOf(
                    ViewMatchers.isDescendantOfA(ViewMatchers.isAssignableFrom(RecyclerView::class.java)),
                    ViewMatchers.withText(itemText)
                ).matches(item)
            }

            override fun describeTo(description: Description) {
                description.appendText("is isDescendantOfA LV with text " + itemText)
            }
        }
    }

    companion object{
        private val TITLE1 = "TITLE1"
        private val DESCRIPTION = "DESCR"
        private val TITLE2 = "TITLE2"
    }
}