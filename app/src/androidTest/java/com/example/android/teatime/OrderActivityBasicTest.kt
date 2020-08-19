package com.example.android.teatime

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.example.android.teatime.OrderActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Kush on 14/07/2020.
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class OrderActivityBasicTest {
    @Rule
    var mActivityTestRule = ActivityTestRule(OrderActivity::class.java)
    @Test
    fun clickIncrementButton_ChangesQuantityAndCost() {
        // 1. Find the view
        // 2. Perform action on the view
        Espresso.onView(ViewMatchers.withId(R.id.increment_button)).perform(ViewActions.click())

        // 3. Check if the view does what you expected
        Espresso.onView(ViewMatchers.withId(R.id.quantity_text_view)).check(ViewAssertions.matches(ViewMatchers.withText("1")))
        Espresso.onView(ViewMatchers.withId(R.id.cost_text_view)).check(ViewAssertions.matches(ViewMatchers.withText("$5.00")))
    }

    @Test
    fun clickDecrementButton_ChangesQuantityAndCost() {

        // Check the initial quantity variable is zero
        Espresso.onView(ViewMatchers.withId(R.id.quantity_text_view)).check(ViewAssertions.matches(ViewMatchers.withText("0")))

        // Click on decrement button
        Espresso.onView(ViewMatchers.withId(R.id.decrement_button))
                .perform(ViewActions.click())

        // Verify that the decrement button decreases the quantity by 1
        Espresso.onView(ViewMatchers.withId(R.id.quantity_text_view)).check(ViewAssertions.matches(ViewMatchers.withText("0")))

        // Verify that the increment button also increases the total cost to $5.00
        Espresso.onView(ViewMatchers.withId(R.id.cost_text_view)).check(ViewAssertions.matches(ViewMatchers.withText("$0.00")))
    }
}