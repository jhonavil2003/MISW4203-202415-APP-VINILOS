package com.example.app_vinilos_g17.ui.view

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.example.app_vinilos_g17.R
import org.hamcrest.Matchers.allOf


@RunWith(AndroidJUnit4::class)
class CollectorDetailFragmentTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(HomeActivity::class.java)

    @Test
    fun verifyRedirectCollectorDetail() {
        Thread.sleep(1000)
        onView(withId(R.id.navigation_collectors)).perform(ViewActions.click())
        Thread.sleep(1000)
        onView(withId(R.id.collectorRv)).check(matches(isDisplayed()))
        onView(withId(R.id.collectorRv)).perform(click())
        Thread.sleep(1000)
        onView(allOf(withId(R.id.textView2), withText("Manolo Bellon")))
            .perform(ViewActions.click())
        Thread.sleep(1000)
        onView(withId(R.id.collectorName)).check(matches(withText("Manolo Bellon")))
        onView(withId(R.id.collectorTelephone)).check(matches(withText("3502457896")))
        onView(withId(R.id.collectorEmail)).check(matches(withText("manollo@caracol.com.co")))
    }

}
