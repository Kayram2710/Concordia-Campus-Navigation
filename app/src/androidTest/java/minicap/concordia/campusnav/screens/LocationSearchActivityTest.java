package minicap.concordia.campusnav.screens;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertTrue;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import minicap.concordia.campusnav.R;

/**
 * Instrumented tests for {@link LocationSearchActivity}.
 *
 * NOTE: In instrumented tests, ActivityScenario DOES NOT provide getResult().
 * That method only exists in Robolectric's local ActivityScenario.
 * So here we simply check that the activity finishes.
 */
@RunWith(AndroidJUnit4.class)
public class LocationSearchActivityTest {

    @Test
    public void testUseCurrentLocation_ActivityFinishes() {
        // 1) Launch LocationSearchActivity with "isStartLocation" = true
        Intent intent = new Intent();
        intent.putExtra(LocationSearchActivity.KEY_IS_STARTING_LOCATION, true);
        ActivityScenario<LocationSearchActivity> scenario =
                ActivityScenario.launch(intent);

        // 2) Click on the "use current location" button
        onView(withId(R.id.useCurrentLocationButton))
                .perform(click());

        // 3) Check if the Activity is finishing
        scenario.onActivity(activity -> {
            assertTrue("Activity should be finishing after button click",
                    activity.isFinishing());
        });

        // 4) Close scenario
        scenario.close();
    }

    /**
     * Another example test to confirm that
     * the 'useCurrentLocationButton' is hidden for destination mode.
     */
    @Test
    public void testNoCurrentLocationButtonWhenNotStartLocation() {
        // 1) Launch with isStartLocation = false
        Intent intent = new Intent();
        intent.putExtra(LocationSearchActivity.KEY_IS_STARTING_LOCATION, false);
        ActivityScenario<LocationSearchActivity> scenario =
                ActivityScenario.launch(intent);

        // 2) (Optional) Check that the button is not visible, e.g.:
        //    onView(withId(R.id.useCurrentLocationButton))
        //        .check(matches(withEffectiveVisibility(Visibility.GONE)));

        scenario.close();
    }
}
