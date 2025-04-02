package minicap.concordia.campusnav.screens;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import minicap.concordia.campusnav.R;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 30) // Use an SDK version compatible with your app
public class TestLocationSearchActivity {

    /**
     * A test subclass that overrides asynchronous behavior so that the test does not hang.
     * (This requires that getUserLocationPath() is declared as protected in LocationSearchActivity.)
     */
    public static class TestableLocationSearchActivity extends LocationSearchActivity {

        protected void getUserLocationPath() {
            // Do nothing to prevent the asynchronous location call from running during tests.
        }
    }

    @Test
    public void testOnCreate_withStartLocation_showsCurrentLocationButtonAndHint() {
        // Arrange: create an intent with extras indicating start-location mode.
        Intent intent = new Intent();
        intent.putExtra(LocationSearchActivity.KEY_IS_STARTING_LOCATION, true);
        intent.putExtra(LocationSearchActivity.KEY_PREVIOUS_INPUT_STRING, "Previous Location");

        TestableLocationSearchActivity activity = Robolectric.buildActivity(TestableLocationSearchActivity.class, intent)
                .create().start().resume().get();

        // Act: find views by their IDs.
        EditText searchInput = activity.findViewById(R.id.locationSearch);
        Button currentLocationButton = activity.findViewById(R.id.useCurrentLocationButton);

        // Assert:
        assertNotNull("Search input should not be null", searchInput);
        assertNotNull("Current location button should not be null", currentLocationButton);
        // In start-location mode, the current location button should be visible.
        assertEquals("Button visibility should be VISIBLE", VISIBLE, currentLocationButton.getVisibility());
        assertEquals("Hint should match starting point search hint",
                activity.getString(R.string.starting_point_search_hint),
                searchInput.getHint().toString());
    }

    @Test
    public void testOnCreate_withDestination_hidesCurrentLocationButtonAndSetsHint() {
        // Arrange: create an intent for destination mode.
        Intent intent = new Intent();
        intent.putExtra(LocationSearchActivity.KEY_IS_STARTING_LOCATION, false);
        intent.putExtra(LocationSearchActivity.KEY_PREVIOUS_INPUT_STRING, "");

        TestableLocationSearchActivity activity = Robolectric.buildActivity(TestableLocationSearchActivity.class, intent)
                .create().start().resume().get();

        // Act: find views.
        EditText searchInput = activity.findViewById(R.id.locationSearch);
        Button currentLocationButton = activity.findViewById(R.id.useCurrentLocationButton);

        // Assert:
        assertNotNull("Search input should not be null", searchInput);
        // In destination mode, the current location button should be hidden.
        assertEquals("Button visibility should be GONE", GONE, currentLocationButton.getVisibility());
        assertEquals("Hint should match destination search hint",
                activity.getString(R.string.destination_search_hint),
                searchInput.getHint().toString());
    }
}
