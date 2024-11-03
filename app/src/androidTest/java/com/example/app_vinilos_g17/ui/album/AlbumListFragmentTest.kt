package com.example.app_vinilos_g17.ui.album

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.app_vinilos_g17.MainActivity
import com.example.app_vinilos_g17.R
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.endsWith
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AlbumListFragmentTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testAlbumListDisplayed() {
        Thread.sleep(2000)
        onView(withId(R.id.progressBar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.albumsRv)).check(matches(isDisplayed()))
    }

    @Test
    fun testAlbumTitleDisplayed() {
        Thread.sleep(2000)
        onView(allOf(
            withText(R.string.title_albums),
            withParent(withClassName(endsWith("Toolbar")))
        )).check(matches(isDisplayed()))
    }

    @Test
    fun testAlbumContentDisplayed() {
        Thread.sleep(2000)
        onView(withId(R.id.albumsRv)).check(matches(hasMinimumChildCount(1)))
    }
}