package minicap.concordia.campusnav;

import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import minicap.concordia.campusnav.buildingmanager.entities.Building;
import minicap.concordia.campusnav.components.BuildingAdapter;
import org.robolectric.RuntimeEnvironment;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 33)
public class BuildingAdapterTests {

    private Building mockBuilding;
    private BuildingAdapter adapter;

    @Before
    public void setUp() {
        // Create a mock Building using Mockito
        mockBuilding = Mockito.mock(Building.class);
        Mockito.when(mockBuilding.getBuildingName()).thenReturn("Mock Building");

        // Create an adapter with one mock building
        List<Building> buildingList = Arrays.asList(mockBuilding);
        adapter = new BuildingAdapter(buildingList);
    }

    @Test
    public void testAdapter_BindsBuildingName() {
        // Prepare a parent view to inflate item layouts
        ViewGroup parent = new FrameLayout(RuntimeEnvironment.getApplication());

        // Create a ViewHolder
        BuildingAdapter.BuildingViewHolder vh = adapter.onCreateViewHolder(parent, 0);

        // Bind the (only) item in position 0
        adapter.onBindViewHolder(vh, 0);

        // The text in tvBuildingName should match "Mock Building"
        String actualText = vh.tvBuildingName.getText().toString();
        assertEquals("Mock Building", actualText);
    }

    @Test
    public void testAdapter_ClickListener() {
        // track if the callback was invoked
        final boolean[] clickInvoked = { false };

        // Provide a listener that sets clickInvoked[0] = true
        adapter.setOnBuildingClickListener(building -> {
            clickInvoked[0] = true;
            // assert which building was clicked
            assertEquals("Mock Building", building.getBuildingName());
        });

        // simulate binding a ViewHolder for position 0
        ViewGroup parent = new FrameLayout(RuntimeEnvironment.getApplication());
        BuildingAdapter.BuildingViewHolder vh = adapter.onCreateViewHolder(parent, 0);
        adapter.onBindViewHolder(vh, 0);

        // Simulate a click on that item
        vh.itemView.performClick();

        // Verify that the click listener was called
        assertTrue("Click listener should have been invoked", clickInvoked[0]);
    }
}
