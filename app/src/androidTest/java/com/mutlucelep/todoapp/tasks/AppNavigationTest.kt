package com.mutlucelep.todoapp.tasks

import android.view.Gravity
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.NoActivityResumedException
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.DrawerMatchers.isOpen
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.mutlucelep.todoapp.R
import com.mutlucelep.todoapp.utils.TestUtils.getToolbarNavigationContentDescription
import junit.framework.Assert
import org.junit.Assert.fail
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class AppNavigationTest {
    @Rule
    @JvmField
    val activityTestRule = ActivityTestRule(TaskActivity::class.java)

    @Test
    fun clickOnAndroidHomeIcon_OpensNavigation() {
        onView(withId(R.id.dl_task))
            .check(matches(isClosed(Gravity.START)))

        onView(
            withContentDescription(
                getToolbarNavigationContentDescription(
                    activityTestRule.activity, R.id.tb_task
                )
            )
        )
            .perform(click())

        onView(withId(R.id.dl_task))
            .check(matches(isOpen(Gravity.START)))
    }

    @Test
    fun backFromTasksScreen_ExitsApp() {
        assertPressingBackExitsApp()
    }

    private fun assertPressingBackExitsApp() {
        try {
            pressBack()
            fail("Should kill the app and throw an exception")
        } catch (e: NoActivityResumedException) {
            // Test OK
        }

    }
}