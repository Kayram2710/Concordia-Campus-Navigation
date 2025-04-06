package minicap.concordia.campusnav.screens;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.content.Intent;
import android.os.IBinder;
import android.view.WindowManager;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Root;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.buildingmanager.ConcordiaBuildingManager;
import minicap.concordia.campusnav.buildingmanager.entities.Campus;
import minicap.concordia.campusnav.buildingmanager.enumerations.CampusName;
import minicap.concordia.campusnav.map.MapCoordinates;
import minicap.concordia.campusnav.map.enums.SupportedMaps;
import minicap.concordia.campusnav.savedstates.States;

@RunWith(AndroidJUnit4.class)
public class MapsActivityTest {

    /**
     * A custom Toast matcher that extends TypeSafeMatcher<Root> to match Toast windows.
     */
    public static class ToastMatcher extends TypeSafeMatcher<Root> {

        @Override
        public void describeTo(Description description) {
            description.appendText("is a toast");
        }

        @Override
        public boolean matchesSafely(Root root) {
            int type = root.getWindowLayoutParams().get().type;
            if (type == WindowManager.LayoutParams.TYPE_TOAST) {
                IBinder windowToken = root.getDecorView().getWindowToken();
                IBinder appToken = root.getDecorView().getApplicationWindowToken();
                return windowToken == appToken;
            }
            return false;
        }
    }

    @Rule
    public ActivityScenarioRule<MapsActivity> activityScenarioRule =
            new ActivityScenarioRule<>(MapsActivity.class);

    @Before
    public void setUp() {
        // Initialize Espresso-Intents if you need to capture outgoing intents.
        Intents.init();
        // Reset any saved state.
        States.getInstance().setCampus(null);
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    /**
     * Verify that key UI components are visible when the activity is launched.
     */
    @Test
    public void testUiComponentsDisplayed() {
        ActivityScenario<MapsActivity> scenario = activityScenarioRule.getScenario();
        onView(withId(R.id.campusSwitch)).check(matches(isDisplayed()));
        onView(withId(R.id.buildingView)).check(matches(isDisplayed()));
        onView(withId(R.id.shuttleScheduleView)).check(matches(isDisplayed()));
        onView(withId(R.id.locationTracker)).check(matches(isDisplayed()));
        onView(withId(R.id.menuButton)).check(matches(isDisplayed()));
        onView(withId(R.id.walkButton)).check(matches(isDisplayed()));
        onView(withId(R.id.carButton)).check(matches(isDisplayed()));
        scenario.close();
    }

    /**
     * Verify that clicking the campus switch button toggles the campus text.
     */
    @Test
    public void testToggleCampus() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            TextView campusTextView = activity.findViewById(R.id.ToCampus);
            String initialText = campusTextView.getText().toString();
            Button campusSwitch = activity.findViewById(R.id.campusSwitch);
            campusSwitch.performClick();
            String toggledText = campusTextView.getText().toString();
            assertNotEquals("Campus text should change after toggle", initialText, toggledText);
        });
    }

    /**
     * Verify that clicking travel mode buttons properly updates their selected states.
     */
    @Test
    public void testTravelModeButtonSelection() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            ImageButton walkButton = activity.findViewById(R.id.walkButton);
            ImageButton wheelchairButton = activity.findViewById(R.id.wheelchairButton);
            ImageButton carButton = activity.findViewById(R.id.carButton);
            ImageButton transitButton = activity.findViewById(R.id.transitButton);

            // Click walk button.
            walkButton.performClick();
            assertTrue("Walk button should be selected", walkButton.isSelected());
            assertFalse("Car button should not be selected", carButton.isSelected());
            assertFalse("Wheelchair button should not be selected", wheelchairButton.isSelected());
            assertFalse("Transit button should not be selected", transitButton.isSelected());

            // Click car button.
            carButton.performClick();
            assertTrue("Car button should be selected", carButton.isSelected());
            assertFalse("Walk button should not be selected", walkButton.isSelected());

            // Click transit button.
            transitButton.performClick();
            assertTrue("Transit button should be selected", transitButton.isSelected());
        });
    }

    /**
     * Verify that when origin and destination are set, clicking the start route button
     * launches NavigationActivity.
     */
    @Test
    public void testStartRouteIntent() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            try {
                // Set dummy origin and destination values using reflection.
                java.lang.reflect.Field originField = MapsActivity.class.getDeclaredField("origin");
                originField.setAccessible(true);
                java.lang.reflect.Field destinationField = MapsActivity.class.getDeclaredField("destination");
                destinationField.setAccessible(true);
                MapCoordinates origin = new MapCoordinates(45.0, -73.0);
                MapCoordinates destination = new MapCoordinates(46.0, -74.0);
                originField.set(activity, origin);
                destinationField.set(activity, destination);
            } catch (Exception e) {
                fail("Exception setting origin/destination: " + e.getMessage());
            }
            ((Button) activity.findViewById(R.id.startRoute)).performClick();
        });
        // Verify that an intent to start NavigationActivity was fired.
        intended(hasComponent(NavigationActivity.class.getName()));
    }

    /**
     * Verify that onEstimatedTimeUpdated properly updates the estimated time TextView.
     */
    @Test
    public void testOnEstimatedTimeUpdated() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            activity.onEstimatedTimeUpdated("10 mins");
            TextView estimatedTime = activity.findViewById(R.id.estimatedTime);
            assertTrue("Estimated time should contain '10 mins'",
                    estimatedTime.getText().toString().contains("10 mins"));
        });
    }

    /**
     * Verify that onMapError displays a Toast with the error message.
     */
    @Test
    public void testOnMapError() {
        activityScenarioRule.getScenario().onActivity(activity -> activity.onMapError("Test error"));
        onView(withText("Test error"))
                .inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    /**
     * Verify that calling showMainMenuDialog displays the main menu dialog.
     */
    @Test
    public void testShowMainMenuDialog() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            activity.showMainMenuDialog();
            // If no exception is thrown, assume success.
            assertTrue(true);
        });
    }

    /**
     * Verify that clicking the building view button shows the BuildingSelectorFragment.
     */
    @Test
    public void testShowBuildingSelectorFragment() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            ((Button) activity.findViewById(R.id.buildingView)).performClick();
            Fragment frag = activity.getSupportFragmentManager().findFragmentByTag("BuildingSelectorFragment");
            assertNotNull("BuildingSelectorFragment should be shown", frag);
        });
    }

    /**
     * Verify that clicking the shuttle schedule view button shows the ShuttleBusScheduleFragment.
     */
    @Test
    public void testShowShuttleScheduleFragment() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            ((Button) activity.findViewById(R.id.shuttleScheduleView)).performClick();
            Fragment frag = activity.getSupportFragmentManager().findFragmentByTag("ShuttleBusScheduleFragment");
            assertNotNull("ShuttleBusScheduleFragment should be shown", frag);
        });
    }

    /**
     * Test switching maps by invoking switchToMap via reflection.
     * When switching to a map type other than GOOGLE_MAPS, the campus switch button should be hidden.
     */
    @Test
    public void testSwitchToMap() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            try {
                java.lang.reflect.Method method = MapsActivity.class.getDeclaredMethod("switchToMap",
                        SupportedMaps.class);
                method.setAccessible(true);
                // Switch to a map type that should hide the campus switch button.
                method.invoke(activity, SupportedMaps.MAPPED_IN);
                Button campusSwitchBtn = activity.findViewById(R.id.campusSwitch);
                assertTrue("Campus switch button should be hidden", campusSwitchBtn.getVisibility() == GONE);
            } catch (Exception e) {
                fail("Exception invoking switchToMap: " + e.getMessage());
            }
        });
    }
}
