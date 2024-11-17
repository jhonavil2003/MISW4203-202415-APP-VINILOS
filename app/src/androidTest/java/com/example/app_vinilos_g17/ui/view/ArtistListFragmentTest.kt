package com.example.app_vinilos_g17.ui.view

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.app_vinilos_g17.R
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.endsWith
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ArtistListFragmentTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(HomeActivity::class.java)

    private fun navigateToArtistList() {
        onView(withId(R.id.navigation_artists)).perform(click())
        Thread.sleep(2000)
    }

    @Test
    fun testArtistListDisplayed() {
        navigateToArtistList()
        onView(withId(R.id.progressBar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.artistsRv)).check(matches(isDisplayed()))
    }

    @Test
    fun testArtistTitleDisplayed() {
        navigateToArtistList()
        onView(allOf(
            withText(R.string.title_artists),
            withParent(withClassName(endsWith("Toolbar")))
        )).check(matches(isDisplayed()))
    }

    @Test
    fun testArtistContentDisplayed() {
        navigateToArtistList()
        onView(withId(R.id.artistsRv)).check(matches(hasMinimumChildCount(1)))
    }
}