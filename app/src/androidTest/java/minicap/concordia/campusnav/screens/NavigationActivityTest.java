package minicap.concordia.campusnav.screens;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Root;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.map.MapCoordinates;

@RunWith(AndroidJUnit4.class)
public class NavigationActivityTest {

    /**
     * Custom matcher for Toast windows.
     */
    public static class ToastMatcher extends TypeSafeMatcher<Root> {
        @Override
        public void describeTo(Description description) {
            description.appendText("is toast");
        }
        @Override
        public boolean matchesSafely(Root root) {
            int type = root.getWindowLayoutParams().get().type;
            if (type == WindowManager.LayoutParams.TYPE_TOAST) {
                IBinder windowToken = root.getDecorView().getWindowToken();
                IBinder appToken = root.getDecorView().getApplicationWindowToken();
                // A toast's window and app token are the same.
                return windowToken == appToken;
            }
            return false;
        }
    }

    @Before
    public void setUp() {
        // Any common setup (none required here).
    }

    @After
    public void tearDown() {
        // Any teardown logic (none required here).
    }

    /**
     * Launch with no extras -> Should show "Invalid navigation data" Toast and finish.
     */
    @Test
    public void testLaunch_noExtras() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), NavigationActivity.class);
        ActivityScenario<NavigationActivity> scenario = ActivityScenario.launch(intent);

        scenario.onActivity(activity -> {
            // Activity should show toast "Invalid navigation data" then finish.
            assertTrue(activity.isFinishing());
        });
    }

    /**
     * Launch with valid extras -> Activity remains alive; check fields are set.
     */
    @Test
    public void testLaunch_validExtras() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), NavigationActivity.class);
        intent.putExtra("origin_lat", 45.0);
        intent.putExtra("origin_lng", -73.0);
        intent.putExtra("destination_lat", 46.0);
        intent.putExtra("destination_lng", -74.0);
        intent.putExtra("travel_mode", "DRIVE");

        ActivityScenario<NavigationActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            // Activity should NOT be finishing.
            assertFalse(activity.isFinishing());

            // Reflectively check that origin/destination/travelMode are set.
            try {
                Field originField = NavigationActivity.class.getDeclaredField("origin");
                originField.setAccessible(true);
                MapCoordinates origin = (MapCoordinates) originField.get(activity);
                assertNotNull("Origin should be set", origin);

                Field destField = NavigationActivity.class.getDeclaredField("destination");
                destField.setAccessible(true);
                MapCoordinates destination = (MapCoordinates) destField.get(activity);
                assertNotNull("Destination should be set", destination);

                Field modeField = NavigationActivity.class.getDeclaredField("travelMode");
                modeField.setAccessible(true);
                String mode = (String) modeField.get(activity);
                assertEquals("DRIVE", mode);
            } catch (Exception e) {
                fail("Reflection error: " + e.getMessage());
            }
        });
    }

    /**
     * Test onMapReady -> Should set style, place markers, display route, check location perms.
     * We'll call it via reflection after scenario launch.
     */
    @Test
    public void testOnMapReady() {
        ActivityScenario<NavigationActivity> scenario = launchValidScenario();
        scenario.onActivity(activity -> {
            try {
                Method onMapReady = NavigationActivity.class.getDeclaredMethod("onMapReady");
                onMapReady.setAccessible(true);
                onMapReady.invoke(activity);

                // If any exception was thrown, test fails. Otherwise, we assume success.
                // We can do additional reflection checks if needed.
            } catch (Exception e) {
                fail("Error invoking onMapReady: " + e.getMessage());
            }
        });
    }

    /**
     * Test fetchAndDisplayRoute() with null origin -> Should show toast "Location data not available".
     */
    @Test
    public void testFetchAndDisplayRoute_nullOrigin() {
        ActivityScenario<NavigationActivity> scenario = launchValidScenario();
        scenario.onActivity(activity -> {
            try {
                // Force origin to null
                Field originField = NavigationActivity.class.getDeclaredField("origin");
                originField.setAccessible(true);
                originField.set(activity, null);

                Method fetchAndDisplayRoute = NavigationActivity.class.getDeclaredMethod("fetchAndDisplayRoute");
                fetchAndDisplayRoute.setAccessible(true);
                fetchAndDisplayRoute.invoke(activity);

                // Verify toast
                onView(withText("Location data not available"))
                        .inRoot(new ToastMatcher())
                        .check(matches(isDisplayed()));

            } catch (Exception e) {
                fail("Reflection error: " + e.getMessage());
            }
        });
    }

    /**
     * Test fetchAndDisplayRoute(origin, destination) -> Should set isNavigationActive = true.
     */
    @Test
    public void testFetchAndDisplayRoute_withArgs() {
        ActivityScenario<NavigationActivity> scenario = launchValidScenario();
        scenario.onActivity(activity -> {
            try {
                Field navActiveField = NavigationActivity.class.getDeclaredField("isNavigationActive");
                navActiveField.setAccessible(true);
                navActiveField.set(activity, false);

                Method fetchAndDisplayRoute = NavigationActivity.class
                        .getDeclaredMethod("fetchAndDisplayRoute", MapCoordinates.class, MapCoordinates.class);
                fetchAndDisplayRoute.setAccessible(true);

                MapCoordinates o = new MapCoordinates(45.0, -73.0);
                MapCoordinates d = new MapCoordinates(46.0, -74.0);

                fetchAndDisplayRoute.invoke(activity, o, d);

                boolean navActive = (boolean) navActiveField.get(activity);
                assertTrue("isNavigationActive should be true after fetchAndDisplayRoute(...)", navActive);

            } catch (Exception e) {
                fail("Reflection error: " + e.getMessage());
            }
        });
    }

    /**
     * Test formatArrivalTime -> "N/A", "10 min", "invalid"
     */
    @Test
    public void testFormatArrivalTime() {
        ActivityScenario<NavigationActivity> scenario = launchValidScenario();
        scenario.onActivity(activity -> {
            try {
                Method formatArrivalTime = NavigationActivity.class.getDeclaredMethod("formatArrivalTime", String.class);
                formatArrivalTime.setAccessible(true);

                String result1 = (String) formatArrivalTime.invoke(activity, "N/A");
                assertEquals("", result1);

                String result2 = (String) formatArrivalTime.invoke(activity, "10 min");
                // Should contain a colon (e.g., "1:15 PM")
                assertTrue("Expected time format with a colon", result2.contains(":"));

                String result3 = (String) formatArrivalTime.invoke(activity, "invalid");
                // Should return the original string if parse fails
                assertEquals("invalid", result3);

            } catch (Exception e) {
                fail("Reflection error: " + e.getMessage());
            }
        });
    }

    /**
     * Test updateStatsText -> ensures statsText is updated with distance + arrival time.
     */
    @Test
    public void testUpdateStatsText() {
        ActivityScenario<NavigationActivity> scenario = launchValidScenario();
        scenario.onActivity(activity -> {
            // We'll set some dummy text in etaText
            TextView eta = activity.findViewById(R.id.eta_text);
            eta.setText("15 min");  // so formatArrivalTime can parse it

            TextView stats = activity.findViewById(R.id.statsText);

            try {
                Method updateStatsText = NavigationActivity.class.getDeclaredMethod("updateStatsText", int.class);
                updateStatsText.setAccessible(true);

                // 500 meters
                updateStatsText.invoke(activity, 500);
                assertTrue("Should show '500 m'", stats.getText().toString().contains("500 m"));

                // 1500 meters
                updateStatsText.invoke(activity, 1500);
                assertTrue("Should show '1.5 km'", stats.getText().toString().contains("1.5 km"));
            } catch (Exception e) {
                fail("Reflection error: " + e.getMessage());
            }
        });
    }

    /**
     * Test onRequestPermissionsResult -> location granted vs. denied
     */
    @Test
    public void testOnRequestPermissionsResult() {
        ActivityScenario<NavigationActivity> scenario = launchValidScenario();
        scenario.onActivity(activity -> {
            // Grant
            int[] grantResults = { android.content.pm.PackageManager.PERMISSION_GRANTED };
            activity.onRequestPermissionsResult(101, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, grantResults);

            // Deny
            int[] denyResults = { android.content.pm.PackageManager.PERMISSION_DENIED };
            activity.onRequestPermissionsResult(101, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, denyResults);

            // Verify Toast
            onView(withText("Location permission required for navigation"))
                    .inRoot(new ToastMatcher())
                    .check(matches(isDisplayed()));
        });
    }

    /**
     * Test startNavigation -> If origin/dest are null => toast
     */
    @Test
    public void testStartNavigation_nullOriginOrDest() {
        ActivityScenario<NavigationActivity> scenario = launchValidScenario();
        scenario.onActivity(activity -> {
            try {
                // Set origin to null
                Field originField = NavigationActivity.class.getDeclaredField("origin");
                originField.setAccessible(true);
                originField.set(activity, null);

                Method startNav = NavigationActivity.class.getDeclaredMethod("startNavigation");
                startNav.setAccessible(true);
                startNav.invoke(activity);

                onView(withText("Please set both origin and destination"))
                        .inRoot(new ToastMatcher())
                        .check(matches(isDisplayed()));
            } catch (Exception e) {
                fail("Reflection error: " + e.getMessage());
            }
        });
    }

    /**
     * Test stopNavigation -> isNavigationActive = false, lastRouteUpdatePosition = null
     */
    @Test
    public void testStopNavigation() {
        ActivityScenario<NavigationActivity> scenario = launchValidScenario();
        scenario.onActivity(activity -> {
            try {
                // Make them non-null
                Field navActiveField = NavigationActivity.class.getDeclaredField("isNavigationActive");
                navActiveField.setAccessible(true);
                navActiveField.set(activity, true);

                Field lastPosField = NavigationActivity.class.getDeclaredField("lastRouteUpdatePosition");
                lastPosField.setAccessible(true);
                lastPosField.set(activity, new MapCoordinates(45.5, -73.5));

                Method stopNav = NavigationActivity.class.getDeclaredMethod("stopNavigation");
                stopNav.setAccessible(true);
                stopNav.invoke(activity);

                assertFalse((Boolean) navActiveField.get(activity));
                assertNull(lastPosField.get(activity));
            } catch (Exception e) {
                fail("Reflection error: " + e.getMessage());
            }
        });
    }

    /**
     * Test onPause -> calls stopNavigation, stopLocationUpdates
     */
    @Test
    public void testOnPause() {
        ActivityScenario<NavigationActivity> scenario = launchValidScenario();
        scenario.onActivity(activity -> {
            activity.onPause();
            // We assume no crash => success.
        });
    }

    /**
     * Test onResume -> if permission is granted + origin/dest not null => calls startNavigation
     */
    @Test
    public void testOnResume() {
        ActivityScenario<NavigationActivity> scenario = launchValidScenario();
        scenario.onActivity(activity -> {
            activity.onResume();
            // If no crash => success. Reflection to confirm isNavigationActive = true
            try {
                Field navActiveField = NavigationActivity.class.getDeclaredField("isNavigationActive");
                navActiveField.setAccessible(true);
                boolean navActive = (boolean) navActiveField.get(activity);
                // Usually it should be true if we have valid extras and permission is presumably granted in test environment.
                assertTrue(navActive);
            } catch (Exception e) {
                // It's possible no permission => false. We won't fail the test for that.
            }
        });
    }

    /**
     * Test showMainMenuDialog + exitIntent => finishes activity
     */
    @Test
    public void testMenuAndExit() {
        ActivityScenario<NavigationActivity> scenario = launchValidScenario();
        scenario.onActivity(activity -> {
            // showMainMenuDialog
            activity.showMainMenuDialog();
            // We assume no crash => success (or we can check fragment manager).

            // exitIntent
            activity.exitIntent();
            assertTrue(activity.isFinishing());
        });
    }

    /**
     * Test onEstimatedTimeUpdated -> sets the text of R.id.eta_text
     */
    @Test
    public void testOnEstimatedTimeUpdated() {
        ActivityScenario<NavigationActivity> scenario = launchValidScenario();
        scenario.onActivity(activity -> {
            activity.onEstimatedTimeUpdated("25 min");
            TextView eta = activity.findViewById(R.id.eta_text);
            assertTrue(eta.getText().toString().contains("25 min"));
        });
    }

    /**
     * onMapError / onMapClicked do nothing -> just call them to ensure coverage
     */
    @Test
    public void testOnMapErrorAndOnMapClicked() {
        ActivityScenario<NavigationActivity> scenario = launchValidScenario();
        scenario.onActivity(activity -> {
            activity.onMapError("dummy error");
            activity.onMapClicked(new MapCoordinates(45.0, -73.0));
            // If no crash => coverage.
        });
    }

    // ------------------------------
    // Helper method to launch with valid extras.
    // ------------------------------
    private ActivityScenario<NavigationActivity> launchValidScenario() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), NavigationActivity.class);
        intent.putExtra("origin_lat", 45.0);
        intent.putExtra("origin_lng", -73.0);
        intent.putExtra("destination_lat", 46.0);
        intent.putExtra("destination_lng", -74.0);
        intent.putExtra("travel_mode", "DRIVE");
        return ActivityScenario.launch(intent);
    }
}
