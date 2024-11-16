package com.example.app_vinilos_g17.ui.view

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.app_vinilos_g17.R
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.endsWith
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CollectorsFragmentTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(HomeActivity::class.java)

    @Before
    fun navigateToCollectors(){
        // Realiza clic en el botón que navega a CollectorsList
        onView(withId(R.id.navigation_collectors))
            .perform(ViewActions.click())
        Thread.sleep(2000)

    }

    @Test
    fun testCollectorsListDisplayed(){
        onView(withId(R.id.collectorRv)).check(matches(isDisplayed()))
    }

    @Test
    fun testCollectorsTitleDisplayed() {
        onView(allOf(
            withText(R.string.title_collectors),
            withParent(withClassName(endsWith("Toolbar")))
        )).check(matches(isDisplayed()))
    }

    @Test
    fun testAlbumContentDisplayed() {
        onView(withId(R.id.collectorRv)).check(matches(hasMinimumChildCount(1)))
    }
}