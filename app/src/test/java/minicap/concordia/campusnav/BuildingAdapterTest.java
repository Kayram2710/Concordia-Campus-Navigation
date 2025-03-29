package minicap.concordia.campusnav;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.buildingmanager.entities.Building;
import minicap.concordia.campusnav.buildingmanager.entities.BuildingFloor;
import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;
import minicap.concordia.campusnav.buildingmanager.enumerations.CampusName;
import minicap.concordia.campusnav.components.BuildingAdapter;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28) // Adjust SDK as needed
public class BuildingAdapterTest {

    private BuildingAdapter adapter;
    private List<Building> testBuildings;

    @Before
    public void setUp() {
        // Create Building objects
        testBuildings = Arrays.asList(
                new Building(
                        "HALL_ID",
                        "Hall Building",
                        CampusName.SGW,
                        new HashMap<String, BuildingFloor>(),
                        45.4971,
                        -73.5788,
                        14,
                        "H",
                        BuildingName.HALL
                ),
                new Building(
                        "CC_ID",
                        "CC Building",
                        CampusName.LOYOLA,
                        new HashMap<String, BuildingFloor>(),
                        45.4580,
                        -73.6400,
                        8,
                        "CC",
                        BuildingName.LOYOLA_CENTRAL_BUILDING
                )
        );

        // Initialize the adapter
        adapter = new BuildingAdapter(testBuildings);
    }

    @Test
    public void test_getItemCount_returnsCorrectSize() {
        assertEquals("Item count should match the size of testBuildings",
                testBuildings.size(), adapter.getItemCount());
    }

    @Test
    public void test_onCreateViewHolder_inflatesCorrectLayout() {
        RecyclerView recyclerView = new RecyclerView(RuntimeEnvironment.getApplication());
        RecyclerView.ViewHolder viewHolder = adapter.onCreateViewHolder(recyclerView, 0);

        assertNotNull("ViewHolder should be created", viewHolder);
        TextView tvBuildingName = viewHolder.itemView.findViewById(R.id.tvBuildingName);
        assertNotNull("tvBuildingName should be in the layout", tvBuildingName);
    }

    @Test
    public void test_onBindViewHolder_bindsDataCorrectly() {
        RecyclerView recyclerView = new RecyclerView(RuntimeEnvironment.getApplication());
        BuildingAdapter.BuildingViewHolder viewHolder =
                adapter.onCreateViewHolder(recyclerView, 0);

        // Bind the first building
        adapter.onBindViewHolder(viewHolder, 0);

        TextView tvBuildingName = viewHolder.itemView.findViewById(R.id.tvBuildingName);
        // We expect "Hall Building" because that's the 'description' in our constructor
        assertEquals("Hall Building", tvBuildingName.getText().toString());
    }

    @Test
    public void test_setOnBuildingClickListener_triggersCallback() {
        RecyclerView recyclerView = new RecyclerView(RuntimeEnvironment.getApplication());
        BuildingAdapter.BuildingViewHolder viewHolder =
                adapter.onCreateViewHolder(recyclerView, 0);

        // Bind the first building
        adapter.onBindViewHolder(viewHolder, 0);

        // Track callback with an AtomicBoolean
        AtomicBoolean callbackTriggered = new AtomicBoolean(false);
        adapter.setOnBuildingClickListener(building -> {
            // Use building.getDescription() if that's your method name
            if ("Hall Building".equals(building.getDescription())) {
                callbackTriggered.set(true);
            }
        });

        // Simulate item click
        viewHolder.itemView.performClick();

        // Confirm the callback was invoked
        assertTrue("The onBuildingClick callback should be triggered on item click",
                callbackTriggered.get());
    }

    @Test
    public void test_updateBuildings_updatesListAndNotifies() {
        List<Building> newList = Arrays.asList(
                new Building(
                        "MOLSON_ID",
                        "Molson School of Business",
                        CampusName.SGW,
                        new HashMap<String, BuildingFloor>(),
                        45.4950,
                        -73.5780,
                        12,
                        "MB",
                        BuildingName.MOLSON_SCHOOL_OF_BUSINESS
                )
        );

        // Update the adapter's list
        adapter.updateBuildings(newList);

        // Confirm the item count changed
        assertEquals("Item count should match the size of newList",
                newList.size(), adapter.getItemCount());

        // Bind the first item of the new list
        RecyclerView recyclerView = new RecyclerView(RuntimeEnvironment.getApplication());
        BuildingAdapter.BuildingViewHolder viewHolder =
                adapter.onCreateViewHolder(recyclerView, 0);
        adapter.onBindViewHolder(viewHolder, 0);

        TextView tvBuildingName = viewHolder.itemView.findViewById(R.id.tvBuildingName);
        assertEquals("Molson School of Business", tvBuildingName.getText().toString());
    }
}