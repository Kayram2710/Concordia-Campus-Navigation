// File: app/src/test/java/minicap/concordia/campusnav/screens/MainActivityTest.java
package minicap.concordia.campusnav.screens;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotEquals;

import android.content.Intent;
import android.widget.Button;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.Shadows;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import minicap.concordia.campusnav.R;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 30) // Use an SDK version compatible with your app
public class MainActivityTest {

    @Test
    public void testSGWCampusButtonStartsMapsActivityWithCorrectExtras() {
        // Arrange: Create and start MainActivity.
        ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class).create().start().resume();
        MainActivity activity = controller.get();

        // Act: Find the SGW campus button and perform a click.
        Button sgwButton = activity.findViewById(R.id.viewSGWCampusButton);
        assertNotNull("SGW campus button should not be null", sgwButton);
        sgwButton.performClick();

        // Get the next started activity (MapsActivity)
        Intent nextIntent = Shadows.shadowOf(activity).getNextStartedActivity();
        assertNotNull("Intent should not be null", nextIntent);
        assertEquals("Next started activity should be MapsActivity",
                MapsActivity.class.getName(), nextIntent.getComponent().getClassName());

        // Assert: Check that the intent extras are set correctly.
        // For SGW campus button, we expect:
        // - KEY_CAMPUS_NOT_SELECTED to be "LOY"
        // - KEY_SHOW_SGW to be true.
        String campusNotSelected = nextIntent.getStringExtra(MapsActivity.KEY_CAMPUS_NOT_SELECTED);
        boolean showSGW = nextIntent.getBooleanExtra(MapsActivity.KEY_SHOW_SGW, false);
        assertEquals("Campus not selected should be 'LOY'", "LOY", campusNotSelected);
        assertEquals("Show SGW flag should be true", true, showSGW);

        // We also expect non-zero latitude and longitude (assuming your ConcordiaBuildingManager returns valid values)
        double lat = nextIntent.getDoubleExtra(MapsActivity.KEY_STARTING_LAT, 0.0);
        double lng = nextIntent.getDoubleExtra(MapsActivity.KEY_STARTING_LNG, 0.0);
        assertNotEquals("Latitude should not be 0", 0.0, lat, 0.0);
        assertNotEquals("Longitude should not be 0", 0.0, lng, 0.0);
    }

    @Test
    public void testLoyCampusButtonStartsMapsActivityWithCorrectExtras() {
        // Arrange: Create and start MainActivity.
        ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class).create().start().resume();
        MainActivity activity = controller.get();

        // Act: Find the Loyola campus button and perform a click.
        Button loyButton = activity.findViewById(R.id.viewLoyCampusButton);
        assertNotNull("Loy campus button should not be null", loyButton);
        loyButton.performClick();

        // Get the next started activity (MapsActivity)
        Intent nextIntent = Shadows.shadowOf(activity).getNextStartedActivity();
        assertNotNull("Intent should not be null", nextIntent);
        assertEquals("Next started activity should be MapsActivity",
                MapsActivity.class.getName(), nextIntent.getComponent().getClassName());

        // Assert: Check that the intent extras are set correctly.
        // For the Loyola campus button, we expect:
        // - KEY_CAMPUS_NOT_SELECTED to be "SGW"
        // - KEY_SHOW_SGW to be false.
        String campusNotSelected = nextIntent.getStringExtra(MapsActivity.KEY_CAMPUS_NOT_SELECTED);
        boolean showSGW = nextIntent.getBooleanExtra(MapsActivity.KEY_SHOW_SGW, true);
        assertEquals("Campus not selected should be 'SGW'", "SGW", campusNotSelected);
        assertEquals("Show SGW flag should be false", false, showSGW);
    }
}
