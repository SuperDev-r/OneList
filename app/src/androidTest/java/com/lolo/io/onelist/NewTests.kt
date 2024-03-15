package com.lolo.io.onelist

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.lolo.io.onelist.screen.ContainerItemView
import com.lolo.io.onelist.screen.RecyclerView
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class NewTests : TestCase() {
    @get:Rule(order = 1)
    var activityScenario = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule(order = 0)
    var hiltAndroidRule = HiltAndroidRule(this)

    @Test
    fun addItemInList() = run {
        val listItem = "Check android app"

        /*OneScreen {
        addList.isDisplayed()
            addList.perform { click() }
        }*/

        RecyclerView {
            Assert.assertEquals(2, lists.getSize())

            lists {
                childAt<RecyclerView.NoteItem>(1) {
                    click()
                }
            }
        }

        ContainerItemView {
            Assert.assertEquals(1, listItems.getSize())

            listItems.firstChild<ContainerItemView.NoteItem> {
                text.hasText("Enjoy the app ! \uD83D\uDE03 100% FREE / 0 ads !")
            }
        }
    }
}

