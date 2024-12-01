package com.example.app_vinilos_g17.ui.view

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.example.app_vinilos_g17.R

@RunWith(AndroidJUnit4::class)
class CollectorDetailFragmentTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(HomeActivity::class.java)

    @Test
    fun verifyRedirectCollectorDetail() {
        // Hacer click en el elemento que lleva a la pantalla de detalle del coleccionista
        //onView(withId(R.id.collectorsRv)).perform(click()) // Aquí debes usar el ID correspondiente del RecyclerView

        // Verificar que el nombre del coleccionista es correcto en el detalle
        //onView(withId(R.id.collectorName)).check(matches(withText("Poeta del pueblo"))) // Verificar que el nombre se muestra correctamente
    }
}
