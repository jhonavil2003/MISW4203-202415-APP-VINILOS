package com.example.app_vinilos_g17.ui.view

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.app_vinilos_g17.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class AlbumListCreateFragmentTest {

    private fun fillAlbumForm(
        albumName: String,
        albumCover: String = "http://example.com/cover.jpg",
        albumReleaseDate: String = "2024-12-01",
        albumDescription: String = "Descripción del álbum"
    ) {
        onView(withId(R.id.editTextAlbumName)).perform(replaceText(albumName))
        onView(withId(R.id.editTextAlbumCover)).perform(replaceText(albumCover))
        onView(withId(R.id.editTextAlbumReleaseDate)).perform(replaceText(albumReleaseDate))
        onView(withId(R.id.editTextAlbumDescription)).perform(replaceText(albumDescription))
    }

    private fun generateUniqueAlbumName(): String {
        return "Álbum Test ${System.currentTimeMillis()}"
    }

    @get:Rule
    val activityRule = ActivityScenarioRule(HomeActivity::class.java)

    @Test
    fun testAlbumCreateNewAlbumSuccessfully() {
        val uniqueAlbumName = generateUniqueAlbumName()

        Thread.sleep(2000)
        onView(withId(R.id.fab_add_album))
            .check(matches(isDisplayed()))

        onView(withId(R.id.fab_add_album)).perform(click())

        fillAlbumForm(albumName = uniqueAlbumName)

        onView(withText("Crear")).perform(click())
    }

    @Test
    fun testAlbumCreateNewWithEmptyNameFails() {
        Thread.sleep(2000)
        onView(withId(R.id.fab_add_album))
            .check(matches(isDisplayed()))

        onView(withId(R.id.fab_add_album)).perform(click())

        fillAlbumForm(albumName = "")

        onView(withText("Crear")).perform(click())

        onView(withText(R.string.error_creating_album))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testAlbumCreateNewWithEmptyCoverFails() {
        val uniqueAlbumName = generateUniqueAlbumName()

        Thread.sleep(2000)
        onView(withId(R.id.fab_add_album))
            .check(matches(isDisplayed()))

        onView(withId(R.id.fab_add_album)).perform(click())

        fillAlbumForm(albumName = uniqueAlbumName, albumCover = "")

        onView(withText("Crear")).perform(click())

        onView(withText(R.string.error_creating_album))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testAlbumCreateNewWithEmptyReleaseDateAlbumFails() {
        val uniqueAlbumName = generateUniqueAlbumName()

        Thread.sleep(2000)
        onView(withId(R.id.fab_add_album))
            .check(matches(isDisplayed()))

        onView(withId(R.id.fab_add_album)).perform(click())

        fillAlbumForm(albumName = uniqueAlbumName, albumReleaseDate = "")

        onView(withText("Crear")).perform(click())

        onView(withText(R.string.error_invalid_release_date_album))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testAlbumCreateNewWithInvalidReleaseDateAlbumFails() {
        val uniqueAlbumName = generateUniqueAlbumName()

        Thread.sleep(2000)
        onView(withId(R.id.fab_add_album))
            .check(matches(isDisplayed()))

        onView(withId(R.id.fab_add_album)).perform(click())

        fillAlbumForm(albumName = uniqueAlbumName, albumReleaseDate = "22222")

        onView(withText("Crear")).perform(click())

        onView(withText(R.string.error_invalid_release_date_album))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testAlbumCreateNewWithEmptyDescriptionAlbumFails() {
        val uniqueAlbumName = generateUniqueAlbumName()

        Thread.sleep(2000)
        onView(withId(R.id.fab_add_album))
            .check(matches(isDisplayed()))

        onView(withId(R.id.fab_add_album)).perform(click())

        fillAlbumForm(albumName = uniqueAlbumName, albumDescription = "")

        onView(withText("Crear")).perform(click())

        onView(withText(R.string.error_creating_album))
            .check(matches(isDisplayed()))
    }
}