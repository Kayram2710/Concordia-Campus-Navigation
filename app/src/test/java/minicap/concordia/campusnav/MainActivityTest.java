package minicap.concordia.campusnav;

import static org.junit.Assert.*;

import android.content.Intent;
import android.widget.Button;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowActivity;

import minicap.concordia.campusnav.screens.MainActivity;
import minicap.concordia.campusnav.screens.MapsActivity;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class MainActivityTest {

    @Test
    public void clickingSGWButton_shouldStartMapsActivityWithExpectedExtras() {
        // Arrange: build the activity
        MainActivity activity = Robolectric.buildActivity(MainActivity.class)
                .create().start().resume().get();

        // Act: find the SGW campus button and click it
        Button sgwButton = activity.findViewById(R.id.viewSGWCampusButton);
        sgwButton.performClick();

        // Capture the started activity intent via ShadowActivity
        ShadowActivity shadowActivity = Shadows.shadowOf(activity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        assertNotNull("An intent should have been started", startedIntent);

        // Assert: the intent should target MapsActivity
        assertEquals("Intent should start MapsActivity",
                MapsActivity.class.getName(),
                startedIntent.getComponent().getClassName());

        // Check extras: based on MainActivity, clicking SGW should put:
        // KEY_CAMPUS_NOT_SELECTED = "LOY"
        // KEY_SHOW_SGW = true
        double lat = startedIntent.getDoubleExtra(MapsActivity.KEY_STARTING_LAT, 0);
        double lng = startedIntent.getDoubleExtra(MapsActivity.KEY_STARTING_LNG, 0);
        String campusNotSelected = startedIntent.getStringExtra(MapsActivity.KEY_CAMPUS_NOT_SELECTED);
        boolean showSGW = startedIntent.getBooleanExtra(MapsActivity.KEY_SHOW_SGW, false);

        // For this test we assume the campus not selected is "LOY" and showSGW is true.
        assertEquals("LOY", campusNotSelected);
        assertTrue(showSGW);

        // Optionally, ensure the latitude and longitude are non-zero (assuming your Campus data provides them)
        assertNotEquals(0.0, lat, 0.0001);
        assertNotEquals(0.0, lng, 0.0001);
    }

    @Test
    public void clickingLoyButton_shouldStartMapsActivityWithExpectedExtras() {
        // Arrange: build the activity
        MainActivity activity = Robolectric.buildActivity(MainActivity.class)
                .create().start().resume().get();

        // Act: find the Loyola campus button and click it
        Button loyButton = activity.findViewById(R.id.viewLoyCampusButton);
        loyButton.performClick();

        // Capture the started activity intent
        ShadowActivity shadowActivity = Shadows.shadowOf(activity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        assertNotNull("An intent should have been started", startedIntent);

        // Assert: the intent should target MapsActivity
        assertEquals("Intent should start MapsActivity",
                MapsActivity.class.getName(),
                startedIntent.getComponent().getClassName());

        // Check extras: for Loyola button, MainActivity sets:
        // KEY_CAMPUS_NOT_SELECTED = "SGW"
        // KEY_SHOW_SGW = false
        String campusNotSelected = startedIntent.getStringExtra(MapsActivity.KEY_CAMPUS_NOT_SELECTED);
        boolean showSGW = startedIntent.getBooleanExtra(MapsActivity.KEY_SHOW_SGW, true);

        assertEquals("SGW", campusNotSelected);
        assertFalse(showSGW);
    }
}
