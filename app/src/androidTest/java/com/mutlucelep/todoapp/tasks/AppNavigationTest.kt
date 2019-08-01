package com.mutlucelep.todoapp.tasks

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
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

}