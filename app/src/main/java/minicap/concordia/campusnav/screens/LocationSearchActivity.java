package minicap.concordia.campusnav.screens;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;


import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.buildingmanager.ConcordiaBuildingManager;
import minicap.concordia.campusnav.buildingmanager.entities.Building;
import minicap.concordia.campusnav.buildingmanager.entities.BuildingFloor;
import minicap.concordia.campusnav.buildingmanager.entities.poi.IndoorPOI;
import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;
import minicap.concordia.campusnav.buildingmanager.enumerations.POIType;
import minicap.concordia.campusnav.map.MapCoordinates;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class LocationSearchActivity extends AppCompatActivity {

    private static final String DEFAULT_FLOOR = "1";

    public static final String KEY_PREVIOUS_INPUT_STRING = "previous_input";
    public static final String KEY_IS_STARTING_LOCATION = "is_starting_location";

    public static final String KEY_RETURN_CHOSEN_LOCATION_STRING = "return_chosen_location";
    public static final String KEY_RETURN_IS_CURRENT_LOCATION_BOOL = "return_current_location";
    public static final String KEY_RETURN_BOOL_IS_DESTINATION = "return_is_destination";

    public static final String KEY_RETURN_BOOL_IS_INDOORS = "return_is_indoors";

    public static final String KEY_RETURN_CHOSEN_COORDS = "return_chosen_coordinates";

    public static final String KEY_RETURN_STRING_BUILDING_NAME = "return_building_name";

    public static final String KEY_RETURN_STRING_FLOOR_ID = "return_floor_id";
    private EditText searchInput;
    private RecyclerView resultsRecyclerView;
    private LocationAdapter adapter;

    private boolean isStartLocation = false;

    private List<LocationItem> locations = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);

        String previousLocation = "";
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            previousLocation = bundle.getString(KEY_PREVIOUS_INPUT_STRING);
            isStartLocation = bundle.getBoolean(KEY_IS_STARTING_LOCATION);
        }

        searchInput = findViewById(R.id.locationSearch);

        if(isStartLocation) {
            Button currentLocationButton = findViewById(R.id.useCurrentLocationButton);
            currentLocationButton.setVisibility(VISIBLE);
            currentLocationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MapCoordinates dummyData = new MapCoordinates(0, 0);
                    Intent returnData = new Intent();
                    returnData.putExtra(KEY_RETURN_CHOSEN_LOCATION_STRING, "");
                    returnData.putExtra(KEY_RETURN_IS_CURRENT_LOCATION_BOOL, true);
                    returnData.putExtra(KEY_RETURN_BOOL_IS_DESTINATION, false);
                    returnData.putExtra(KEY_RETURN_CHOSEN_COORDS, dummyData);
                    returnData.putExtra(KEY_RETURN_BOOL_IS_INDOORS, false);
                    setResult(RESULT_OK, returnData);
                    finish();
                }
            });

            searchInput.setHint(R.string.starting_point_search_hint);
        }
        else {
            //It would be pointless to set the current location as the destination
            Button currentLocationButton = findViewById(R.id.useCurrentLocationButton);
            currentLocationButton.setVisibility(GONE);

            searchInput.setHint(R.string.destination_search_hint);
        }

        if(previousLocation != null && !previousLocation.isEmpty() && !previousLocation.isBlank()) {
            searchInput.setText(previousLocation);
        }

        setDefaultLocationList();

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Not needed
            }

            @Override
            public void afterTextChanged(Editable s) {
                // We use length here because the isEmpty() call requires min API 35 (we are currently 24)
                if(s.length() == 0) {
                    adapter.filter("");
                    return;
                }

                //may be unnecessary
                if(s.toString().contains("\n")) {
                    s.delete(s.length() - 3, s.length() - 1);
                }
                adapter.filter(s.toString());
            }
        });
    }

    /**
     * Sets the default list of locations shown
     */
    private void setDefaultLocationList() {
        ConcordiaBuildingManager manager = ConcordiaBuildingManager.getInstance();

        List<Building> buildings = manager.getAllBuildings();
        Map<BuildingName, List<IndoorPOI>> poisToAdd = new EnumMap<>(BuildingName.class);

        //We want the buildings to be at the top, so that all the classrooms don't take up the screen
        for(Building cur : buildings) {
            MapCoordinates coordsWithFloor = new MapCoordinates(cur.getLatitude(), cur.getLongitude(), DEFAULT_FLOOR);
            LocationItem building = new LocationItem(cur.getBuildingName(), coordsWithFloor);

            locations.add(building);

            List<IndoorPOI> allPoisForBuilding = new ArrayList<>();
            for(BuildingFloor floor : cur.getFloors()) {
                List<IndoorPOI> indoorPOIs = floor.getPOIsOfType(POIType.CLASS_ROOM);

                allPoisForBuilding.addAll(indoorPOIs);
            }
            poisToAdd.put(cur.getBuildingIdentifier(), allPoisForBuilding);
        }

        for(Map.Entry<BuildingName, List<IndoorPOI>> entry : poisToAdd.entrySet()) {
            String buildingName = manager.getBuilding(entry.getKey()).getBuildingName();

            for(IndoorPOI poi : entry.getValue()) {
                LocationItem item = new LocationItem(poi.getPoiName(), poi.getLocation(), buildingName, poi.getFloorName());
                locations.add(item);
            }
        }

        resultsRecyclerView = findViewById(R.id.results_recycler_view);

        adapter = new LocationAdapter(locations);
        resultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        resultsRecyclerView.setAdapter(adapter);

        adapter.setOnClickListener(new LocationAdapter.OnItemClickedListener() {
            @Override
            public void onOutdoorItemClick(String locationName, MapCoordinates coordinates) {
                runOnUiThread(() -> {
                    Intent returnData = new Intent();
                    returnData.putExtra(KEY_RETURN_CHOSEN_LOCATION_STRING, locationName);
                    returnData.putExtra(KEY_RETURN_IS_CURRENT_LOCATION_BOOL, false);
                    returnData.putExtra(KEY_RETURN_BOOL_IS_DESTINATION, !isStartLocation);
                    returnData.putExtra(KEY_RETURN_CHOSEN_COORDS, coordinates);
                    returnData.putExtra(KEY_RETURN_BOOL_IS_INDOORS, false);
                    setResult(RESULT_OK, returnData);
                    finish();
                });
            }

            @Override
            public void onIndoorItemClick(String locationName, MapCoordinates coordinates, String buildingName, String floorId) {
                runOnUiThread(() -> {
                    Intent returnData = new Intent();
                    returnData.putExtra(KEY_RETURN_CHOSEN_LOCATION_STRING, locationName);
                    returnData.putExtra(KEY_RETURN_IS_CURRENT_LOCATION_BOOL, false);
                    returnData.putExtra(KEY_RETURN_BOOL_IS_DESTINATION, !isStartLocation);
                    returnData.putExtra(KEY_RETURN_CHOSEN_COORDS, coordinates);
                    returnData.putExtra(KEY_RETURN_BOOL_IS_INDOORS, true);
                    returnData.putExtra(KEY_RETURN_STRING_BUILDING_NAME, buildingName);
                    returnData.putExtra(KEY_RETURN_STRING_FLOOR_ID, floorId);
                    setResult(RESULT_OK, returnData);
                    finish();
                });
            }
        });
    }
}


class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {
    private final List<LocationItem> allLocations = new ArrayList<>();

    private List<LocationItem> filteredLocations = new ArrayList<>();

    private OnItemClickedListener onClickListener;

    public LocationAdapter(List<LocationItem> locations) {
        this.allLocations.addAll(locations);
        this.filteredLocations.addAll(locations);
    }

    /**
     * Sets the on click listener for the adapter
     * @param onClickListener The new listener
     */
    public void setOnClickListener(OnItemClickedListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LocationItem cur = filteredLocations.get(position);
        holder.locationText.setText(cur.getLocationName());
        holder.locationText.setTextColor(Color.parseColor("#FFFFFF"));
        holder.itemView.setOnClickListener(v -> {
            if(onClickListener != null) {
                if(cur.getIsIndoors()) {
                    onClickListener.onIndoorItemClick(cur.getLocationName(), cur.getCoordinates(), cur.getBuildingName(), cur.getFloorId());
                }
                else {
                    onClickListener.onOutdoorItemClick(cur.getLocationName(), cur.getCoordinates());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredLocations.size();
    }

    /**
     * Filters the locations in the list
     * @param filterText The string for filtering
     */
    public void filter(String filterText) {
        filteredLocations.clear();
        if(filterText.isBlank() || filterText.isEmpty()) {
            filteredLocations.addAll(allLocations);
        }
        else {
            String lowerFilterText = filterText.toLowerCase();
            for(LocationItem item: allLocations) {
                if(item.getLocationName().toLowerCase().contains(lowerFilterText)) {
                    filteredLocations.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView locationText;

        public ViewHolder(View itemView) {
            super(itemView);
            locationText = itemView.findViewById(android.R.id.text1);
        }
    }

    public interface OnItemClickedListener {
        /**
         * Invoked when an outdoor item is clicked
         * @param locationName The name of the location
         * @param coordinates The coordinates of the location
         */
        void onOutdoorItemClick(String locationName, MapCoordinates coordinates);

        /**
         * Invoked when an indoor item is clicked
         * @param locationName The name of the location
         * @param coordinates The coordinates of the location
         * @param buildingName The name of the building as a string
         * @param floorId The id for the floor
         */
        void onIndoorItemClick(String locationName, MapCoordinates coordinates, String buildingName, String floorId);
    }
}

class LocationItem {
    private String locationName;

    private MapCoordinates coordinates;

    private boolean isIndoors;

    private String buildingName;

    private String floorId;

    public LocationItem(String name, MapCoordinates coordinates) {
        this(name, coordinates, false);
    }

    public LocationItem(String name, MapCoordinates coordinates, boolean isIndoors) {
        this.locationName = name;
        this.coordinates = coordinates;
        this.isIndoors = isIndoors;
    }

    public LocationItem(String name, MapCoordinates coordinates, String buildingName, String floorId) {
        this.locationName = name;
        this.coordinates = coordinates;
        this.isIndoors = true;
        this.buildingName = buildingName;
        this.floorId = floorId;
    }

    /**
     * Gets the location name for the item
     * @return The location name
     */
    public String getLocationName() {
        return locationName;
    }

    /**
     * Gets the coordinates for this item
     * @return This location's coordinates
     */
    public MapCoordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Gets flag indicating if the location item is indoors or outdoors
     * @return True if indoors, false otherwise
     */
    public boolean getIsIndoors() {
        return isIndoors;
    }

    /**
     * Gets the building name associated to this item
     * @return The string buiding name
     */
    public String getBuildingName() {
        return buildingName;
    }

    /**
     * Gets the floor id for the item
     * @return String floor id
     */
    public String getFloorId() {
        return floorId;
    }
}
