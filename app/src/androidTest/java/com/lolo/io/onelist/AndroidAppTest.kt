package com.lolo.io.onelist

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isNotSelected
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.containsString
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class AndroidAppTest {


    @get:Rule(order = 1)
    var activityScenario = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule(order = 0)
    var hiltAndroidRule = HiltAndroidRule(this)

    @Before
    fun init() {
        hiltAndroidRule.inject()
    }

    @Test
    fun addItemInList() {
        val listItem = "Check android app"

        onView(allOf((withId(R.id.textView)), withText(containsString("TODO")))).check(
            matches(
                isCompletelyDisplayed()
            )
        ).perform(
            click()
        )
        onView(withId(R.id.addItemEditText)).perform(typeText(listItem))
        onView(withId(R.id.validate)).perform(click())
        onView(allOf(withId(R.id.text_item_on_list), withText(containsString(listItem)))).check(
            matches(isCompletelyDisplayed())
        )

    }

    @Test
    fun addNewInList() {
        val newList = "Add new list"
        val listItem = "Check android app"

        onView((withId(R.id.buttonRemoveList))).perform(
            click()
        )
        onView(withId(R.id.listTitle)).perform(typeText(newList))
        onView(withId(R.id.validateEditList)).check(matches(isCompletelyDisplayed()))
            .perform(click())
        onView(allOf((withId(R.id.textView)), withText(newList))).check(
            matches(
                isCompletelyDisplayed()
            )
        ).perform(
            click()
        )

        onView(withId(R.id.addItemEditText)).perform(typeText(listItem))
        onView(withId(R.id.validate)).perform(click())
        onView(
            allOf(
                withId(R.id.text_item_on_list),
                withText(listItem)
            )
        ).check(matches(isDisplayed()))
        onView(allOf((withId(R.id.text_item_on_list)), withText(containsString(listItem)))).check(
            matches(isCompletelyDisplayed())
        ).perform(
            click()
        )
        onView(allOf((withId(R.id.text_item_on_list)), withText(containsString(listItem)))).check(
            matches(isNotSelected())
        )
    }

    @Test
    fun deleteNewInList() {
        val newList = "Add new list"
        val listItem = "Check android app"

        onView((withId(R.id.buttonRemoveList))).perform(
            click()
        )
        Thread.sleep(500)
        onView(withId(R.id.listTitle)).perform(typeText(newList))
        onView(withId(R.id.validateEditList)).check(matches(isCompletelyDisplayed()))
            .perform(click())
        onView(allOf((withId(R.id.textView)), withText(newList))).check(
            matches(
                isCompletelyDisplayed()
            )
        ).perform(
            click()
        )

        onView(withId(R.id.addItemEditText)).perform(typeText(listItem))
        onView(withId(R.id.validate)).perform(click())

        fun isDisplayed(): Boolean {
            try {
                onView(withId(R.id.deleteListText)).check(matches(isCompletelyDisplayed()))
                return true
            } catch (e: NoMatchingViewException) {
                return false
            }
        }

        onView(allOf((withId(R.id.textView)), withText(newList))).check(
            matches(
                isCompletelyDisplayed()
            )
        ).perform(
            longClick()
        )
        if (isDisplayed()) {
            onView(allOf((withId(R.id.textView)), withText(newList))).check(
                matches(
                    isCompletelyDisplayed()
                )
            ).perform(
                longClick()
            )
            onView(withId(R.id.deleteIcon)).check(matches(isCompletelyDisplayed())).perform(click())
        }


        onView(withId(R.id.buttonRemoveList)).check(matches(isCompletelyDisplayed()))
            .perform(click())

        onView(withId(R.id.deleteListText)).check(
            matches(
                allOf(
                    isCompletelyDisplayed(),
                    withText(containsString("This will delete the list and all the items it contains. Are you sure you want to delete this list ? (this can't be undone)"))
                )
            )
        )

        onView(withId(R.id.validateDeleteList)).check(matches(isCompletelyDisplayed()))
            .perform(click())

    }
}