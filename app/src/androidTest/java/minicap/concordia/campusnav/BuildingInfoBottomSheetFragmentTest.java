package minicap.concordia.campusnav;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import minicap.concordia.campusnav.buildingmanager.ConcordiaBuildingManager;
import minicap.concordia.campusnav.buildingmanager.entities.Building;
import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;
import minicap.concordia.campusnav.components.BuildingInfoBottomSheetFragment;
import minicap.concordia.campusnav.screens.MainActivity;

@RunWith(AndroidJUnit4.class)
public class BuildingInfoBottomSheetFragmentTest {

    private static final String TAG = "TEST_DRAWABLE";
    private final BuildingName testBuilding = BuildingName.HALL;

    @Before
    public void setUp() {
        // Ensure ConcordiaBuildingManager is initialized with real data
        InstrumentationRegistry.getInstrumentation().runOnMainSync(ConcordiaBuildingManager::getInstance);
    }

    @Test
    public void testBuildingInfoDisplayedCorrectly() {
        // Launch the activity
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                // Create and show the fragment
                BuildingInfoBottomSheetFragment fragment = BuildingInfoBottomSheetFragment.newInstance(testBuilding);
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(android.R.id.content, fragment)
                        .commitNow();

                assertNotNull(fragment.getView());

                // Get UI elements
                TextView nameTextView = fragment.getView().findViewById(R.id.building_name);
                TextView addressTextView = fragment.getView().findViewById(R.id.building_address);
                TextView detailsTextView = fragment.getView().findViewById(R.id.building_details);
                ImageView buildingImageView = fragment.getView().findViewById(R.id.building_image);

                // Fetch expected data from the real BuildingManager
                Building expectedBuilding = ConcordiaBuildingManager.getInstance().getBuilding(testBuilding);
                assertNotNull(expectedBuilding);

                // Verify UI content
                assertEquals(expectedBuilding.getBuildingName(), nameTextView.getText().toString());
                assertEquals(expectedBuilding.getBuildingAddress(), addressTextView.getText().toString());
                assertEquals(expectedBuilding.getDescription(), detailsTextView.getText().toString());

                // Verify ImageView
                Drawable actualDrawable = buildingImageView.getDrawable();
                Drawable expectedDrawable = expectedBuilding.getBuildingImageRes() == 0 ? null :
                        ContextCompat.getDrawable(activity, expectedBuilding.getBuildingImageRes());

                Log.d(TAG, "Actual Drawable class: " + (actualDrawable != null ? actualDrawable.getClass().getSimpleName() : "null"));
                Log.d(TAG, "Expected Drawable class: " + (expectedDrawable != null ? expectedDrawable.getClass().getSimpleName() : "null"));

                if (expectedDrawable == null) {
                    assertTrue("Expected a default empty drawable",
                            actualDrawable instanceof BitmapDrawable &&
                                    ((BitmapDrawable) actualDrawable).getBitmap().getWidth() == 53 &&
                                    ((BitmapDrawable) actualDrawable).getBitmap().getHeight() == 53 &&
                                    ((BitmapDrawable) actualDrawable).getBitmap().getConfig() == Bitmap.Config.ARGB_8888);

                } else {
                    assertNotNull("Expected image, but drawable was null", actualDrawable);

                        // Convert both to Bitmap and compare
                    if (actualDrawable instanceof BitmapDrawable && expectedDrawable instanceof BitmapDrawable) {
                        Bitmap actualBitmap = ((BitmapDrawable) actualDrawable).getBitmap();
                        Bitmap expectedBitmap = ((BitmapDrawable) expectedDrawable).getBitmap();

                        assertTrue("Bitmaps do not match", actualBitmap.sameAs(expectedBitmap));
                    }
                }

            });
        }
    }

    @Test
    public void testDirectionsButtonClick() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                // Create and show the fragment
                BuildingInfoBottomSheetFragment fragment = BuildingInfoBottomSheetFragment.newInstance(testBuilding);
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(android.R.id.content, fragment)
                        .commitNow();

                assertNotNull(fragment.getView());

                // Get UI elements
                ImageButton directionsButton = fragment.getView().findViewById(R.id.btn_directions);
                assertNotNull("Directions button not found", directionsButton);

                // Fetch expected data from BuildingManager
                Building expectedBuilding = ConcordiaBuildingManager.getInstance().getBuilding(testBuilding);
                assertNotNull(expectedBuilding);

                //  Mock listener for testing
                final boolean[] wasCalled = {false};
                BuildingInfoBottomSheetFragment.BuildingInfoListener testListener = new BuildingInfoBottomSheetFragment.BuildingInfoListener() {
                    @Override
                    public void directionButtonOnClick(Building building) {
                        wasCalled[0] = true;
                        assertEquals("Latitude mismatch", expectedBuilding.getLatitude(), building.getLatitude(), 0.0001);
                        assertEquals("Longitude mismatch", expectedBuilding.getLongitude(), building.getLongitude(), 0.0001);
                    }
                };

                // Set listener using the new method
                fragment.setBuildingInfoListener(testListener);

                // Perform click on directions button
                directionsButton.performClick();

                // Verify that the listener was triggered
                assertTrue("directionButtonOnClick() was not called", wasCalled[0]);
            });
        }
    }



}