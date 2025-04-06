package minicap.concordia.campusnav.screens;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.buildingmanager.ConcordiaBuildingManager;
import minicap.concordia.campusnav.buildingmanager.entities.Campus;
import minicap.concordia.campusnav.buildingmanager.enumerations.CampusName;
import minicap.concordia.campusnav.savedstates.States;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Before
    public void setUp() {
        // Initialize Espresso-Intents to capture and verify intents.
        Intents.init();
        // Clear any previously set state.
        States.getInstance().setCampus(null);
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    /**
     * Verify that the SGW and Loyola campus buttons are displayed.
     */
    @Test
    public void testButtonsDisplayed() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        onView(withId(R.id.viewSGWCampusButton)).check(matches(isDisplayed()));
        onView(withId(R.id.viewLoyCampusButton)).check(matches(isDisplayed()));
        scenario.close();
    }

    /**
     * Verify that clicking the SGW campus button updates the state and starts MapsActivity.
     */
    @Test
    public void testSgwCampusButtonClick() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        onView(withId(R.id.viewSGWCampusButton)).perform(click());

        // Verify that an intent to start MapsActivity was fired.
        intended(hasComponent(MapsActivity.class.getName()));

        // Verify that the States singleton was updated with the SGW campus.
        Campus expectedCampus = ConcordiaBuildingManager.getInstance().getCampus(CampusName.SGW);
        Campus actualCampus = States.getInstance().getCampus();
        assertEquals("The campus set in state should be SGW", expectedCampus, actualCampus);
        scenario.close();
    }

    /**
     * Verify that clicking the Loyola campus button updates the state and starts MapsActivity.
     */
    @Test
    public void testLoyCampusButtonClick() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        onView(withId(R.id.viewLoyCampusButton)).perform(click());

        // Verify that an intent to start MapsActivity was fired.
        intended(hasComponent(MapsActivity.class.getName()));

        // Verify that the States singleton was updated with the Loyola campus.
        Campus expectedCampus = ConcordiaBuildingManager.getInstance().getCampus(CampusName.LOYOLA);
        Campus actualCampus = States.getInstance().getCampus();
        assertEquals("The campus set in state should be Loyola", expectedCampus, actualCampus);
        scenario.close();
    }
}
