package com.example.androiduitesting;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ShowActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> scenario =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    /**
     * Helper: adds a city and clicks on it in the list.
     */
    private void addAndClickCity(String cityName) {
        onView(withId(R.id.button_add)).perform(click());
        onView(withId(R.id.editText_name)).perform(ViewActions.typeText(cityName));
        onView(withId(R.id.button_confirm)).perform(click());

        // Click the city at position 0 in the ListView
        onData(is(instanceOf(String.class)))
                .inAdapterView(withId(R.id.city_list))
                .atPosition(0)
                .perform(click());
    }

    /**
     * Test 1: Check whether the activity correctly switched to ShowActivity.
     */
    @Test
    public void testActivitySwitch() {
        addAndClickCity("Edmonton");

        // Verify that an intent to ShowActivity was sent
        intended(hasComponent(ShowActivity.class.getName()));
    }

    /**
     * Test 2: Test whether the city name is consistent.
     */
    @Test
    public void testCityNameConsistency() {
        addAndClickCity("Edmonton");

        // We are now in ShowActivity — check the city name is displayed correctly
        onView(withId(R.id.show_city_name))
                .check(matches(withText("Edmonton")));
    }

    /**
     * Test 3: Test the "back" button.
     */
    @Test
    public void testBackButton() {
        addAndClickCity("Edmonton");

        // Click the BACK button in ShowActivity
        onView(withId(R.id.button_back)).perform(click());

        // Verify we're back on MainActivity by checking ADD CITY button exists
        onView(withId(R.id.button_add)).check(matches(isDisplayed()));
    }
}