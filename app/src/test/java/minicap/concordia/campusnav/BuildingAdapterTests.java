package minicap.concordia.campusnav;

import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.RuntimeEnvironment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import minicap.concordia.campusnav.buildingmanager.entities.Building;
import minicap.concordia.campusnav.buildingmanager.entities.BuildingFloor;
import minicap.concordia.campusnav.buildingmanager.enumerations.CampusName;
import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;
import minicap.concordia.campusnav.components.BuildingAdapter;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 33)
public class BuildingAdapterTests {

    private Building dummyBuilding;
    private BuildingAdapter adapter;

    // Dummy subclass of Building using dummy values for required constructor parameters.
    // We override getBuildingName() to return the desired name.
    private static class DummyBuilding extends Building {
        private final String dummyBuildingName;

        public DummyBuilding(String buildingName) {
            // Call the superclass constructor with dummy values.
            // The second parameter might be used internally, but we override getBuildingName().
            super("dummyId", buildingName, CampusName.SGW,
                    new HashMap<String, BuildingFloor>(), 0.0, 0.0, 0, "dummy", BuildingName.HALL);
            this.dummyBuildingName = buildingName;
        }

        @Override
        public String getBuildingName() {
            return dummyBuildingName;
        }
    }

    @Before
    public void setUp() {
        // Create a dummy building instance with the building name "Mock Building".
        dummyBuilding = new DummyBuilding("Mock Building");

        // Create an adapter with one dummy building.
        List<Building> buildingList = Arrays.asList(dummyBuilding);
        adapter = new BuildingAdapter(buildingList);
    }

    @Test
    public void testAdapter_BindsBuildingName() {
        // Prepare a parent view to inflate item layouts.
        ViewGroup parent = new FrameLayout(RuntimeEnvironment.getApplication());

        // Create a ViewHolder by inflating the item layout.
        BuildingAdapter.BuildingViewHolder vh = adapter.onCreateViewHolder(parent, 0);

        // Bind the item at position 0.
        adapter.onBindViewHolder(vh, 0);

        // Verify that the TextView displays "Mock Building".
        String actualText = vh.tvBuildingName.getText().toString();
        assertEquals("Mock Building", actualText);
    }

    @Test
    public void testAdapter_ClickListener() {
        // Track if the callback was invoked.
        final boolean[] clickInvoked = { false };

        // Set a click listener that marks clickInvoked true and asserts the building name.
        adapter.setOnBuildingClickListener(building -> {
            clickInvoked[0] = true;
            assertEquals("Mock Building", building.getBuildingName());
        });

        // Prepare a parent view.
        ViewGroup parent = new FrameLayout(RuntimeEnvironment.getApplication());

        // Create and bind a ViewHolder.
        BuildingAdapter.BuildingViewHolder vh = adapter.onCreateViewHolder(parent, 0);
        adapter.onBindViewHolder(vh, 0);

        // Simulate a click on the item view.
        vh.itemView.performClick();

        // Verify the click listener was invoked.
        assertTrue("Click listener should have been invoked", clickInvoked[0]);
    }
}
