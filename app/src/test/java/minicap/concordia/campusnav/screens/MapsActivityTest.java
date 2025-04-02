package minicap.concordia.campusnav.screens;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import androidx.test.core.app.ApplicationProvider;
import androidx.fragment.app.Fragment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;

import java.lang.reflect.Field;

import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.map.AbstractMap;
import minicap.concordia.campusnav.map.MapCoordinates;
import minicap.concordia.campusnav.map.enums.MapColors;

@RunWith(RobolectricTestRunner.class)
public class MapsActivityTest {

    @Test
    public void testOnCreate_setsIntentExtrasCorrectly() {
        // Prepare an intent with extras that MapsActivity expects.
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MapsActivity.class);
        intent.putExtra(MapsActivity.KEY_STARTING_LAT, 45.0);
        intent.putExtra(MapsActivity.KEY_STARTING_LNG, -73.0);
        intent.putExtra(MapsActivity.KEY_CAMPUS_NOT_SELECTED, "LOY");
        intent.putExtra(MapsActivity.KEY_SHOW_SGW, true);

        // Build and start the activity.
        ActivityController<MapsActivity> controller = Robolectric.buildActivity(MapsActivity.class, intent);
        MapsActivity activity = controller.create().start().resume().get();

        // Verify that the campus text view is set as expected.
        TextView campusTextView = activity.findViewById(R.id.ToCampus);
        assertNotNull("Campus text view should not be null", campusTextView);
        assertEquals("Initial campus text should be 'LOY'", "LOY", campusTextView.getText().toString());
    }

    @Test
    public void testCampusSwitchButton_changesCampusText() throws Exception {
        // Start the activity with KEY_SHOW_SGW = true (so campusNotSelected is "LOY").
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MapsActivity.class);
        intent.putExtra(MapsActivity.KEY_STARTING_LAT, 45.0);
        intent.putExtra(MapsActivity.KEY_STARTING_LNG, -73.0);
        intent.putExtra(MapsActivity.KEY_CAMPUS_NOT_SELECTED, "LOY");
        intent.putExtra(MapsActivity.KEY_SHOW_SGW, true);

        ActivityController<MapsActivity> controller = Robolectric.buildActivity(MapsActivity.class, intent);
        MapsActivity activity = controller.create().start().resume().get();

        // Use reflection to inject a dummy AbstractMap into the private 'map' field.
        Field mapField = MapsActivity.class.getDeclaredField("map");
        mapField.setAccessible(true);
        AbstractMap dummyMap = new AbstractMap(null) {
            @Override
            public Fragment initialize() {
                return null;
            }
            @Override
            public void addMarker(MapCoordinates position, String title, MapColors color, boolean clearOtherMarkers) { }
            @Override
            public void addMarker(MapCoordinates position, String title, MapColors color) { }
            @Override
            public void addMarker(MapCoordinates position, String title, boolean clearOtherMarkers) { }
            @Override
            public void addMarker(MapCoordinates position, String title) { }
            @Override
            public void clearAllMarkers() { }
            @Override
            public void clearPathFromMap() { }
            @Override
            public void displayRoute(MapCoordinates origin, MapCoordinates destination, String travelMode) { }
            @Override
            public void centerOnCoordinates(MapCoordinates coordinates) { }
            @Override
            public void switchToFloor(String floorName) { }
            @Override
            public boolean toggleLocationTracking(boolean isEnabled) { return false; }
        };
        mapField.set(activity, dummyMap);

        // Find the campus switch button and simulate a click.
        Button campusSwitchBtn = activity.findViewById(R.id.campusSwitch);
        assertNotNull("Campus switch button should not be null", campusSwitchBtn);

        TextView campusTextView = activity.findViewById(R.id.ToCampus);
        assertNotNull("Campus text view should not be null", campusTextView);

        // Initially, the campus text should be "LOY".
        assertEquals("Initial campus text should be 'LOY'", "LOY", campusTextView.getText().toString());

        // Simulate clicking the campus switch button.
        campusSwitchBtn.performClick();

        // Verify that the campus text now changed to "SGW" after the button click.
        assertEquals("After switching, campus text should be 'SGW'", "SGW", campusTextView.getText().toString());
    }
}
