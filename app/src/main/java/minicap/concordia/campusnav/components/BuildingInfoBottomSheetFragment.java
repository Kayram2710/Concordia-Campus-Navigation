package minicap.concordia.campusnav.components;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.buildingmanager.ConcordiaBuildingManager;
import minicap.concordia.campusnav.buildingmanager.entities.Building;
import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;

public class BuildingInfoBottomSheetFragment extends BottomSheetDialogFragment {

    private static final String ARG_BUILDING_NAME = "building_name";
    private static final String TAG = "BuildingInfoBottomSheet";

    private TextView buildingNameText, buildingAddress, buildingDetails;
    private ImageView buildingImage;
    private ImageButton directionsButton;

    public interface BuildingInfoListener {
        void directionButtonOnClick(Building building);
    }

    private BuildingInfoListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // Set listener only if the activity implements BuildingInfoListener
        if (context instanceof BuildingInfoListener) {
            listener = (BuildingInfoListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    /**
     * Factory method to create a new instance of the Bottom Sheet with a given building name.
     */
    public static BuildingInfoBottomSheetFragment newInstance(BuildingName buildingName) {
        BuildingInfoBottomSheetFragment fragment = new BuildingInfoBottomSheetFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_BUILDING_NAME, buildingName);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.building_information_bottomsheet, container, false);

        initializeViews(view);
        loadBuildingInfo();

        return view;
    }

    /**
     * Initializes UI elements.
     */
    private void initializeViews(View view) {
        buildingNameText = view.findViewById(R.id.building_name);
        buildingAddress = view.findViewById(R.id.building_address);
        buildingDetails = view.findViewById(R.id.building_details);
        buildingImage = view.findViewById(R.id.building_image);
        directionsButton = view.findViewById(R.id.btn_directions);
    }

    /**
     * Loads building information using the identifier from arguments.
     */
    private void loadBuildingInfo() {
        if (getArguments() == null) {
            Log.e(TAG, "No arguments provided.");
            return;
        }

        BuildingName buildingName = (BuildingName) getArguments().getSerializable(ARG_BUILDING_NAME);
        if (buildingName == null) {
            Log.e(TAG, "Invalid building name provided.");
            return;
        }

        Log.d(TAG, "Fetching Building Info for: " + buildingName);

        // Get the building from the manager
        Building building = ConcordiaBuildingManager.getInstance().getBuilding(buildingName);
        if (building != null) {
            populateBuildingData(building);
        } else {
            Log.e(TAG, "Building not found in ConcordiaBuildingManager!");
        }
    }

    /**
     * Populates UI elements with building data.
     */
    private void populateBuildingData(Building building) {
        buildingNameText.setText(building.getBuildingName());
        buildingAddress.setText(building.getBuildingAddress());
        buildingDetails.setText(building.getDescription());
        directionsButton.setImageResource(R.drawable.ic_directions);

        if (building.getBuildingImageRes() != 0) {
            buildingImage.setImageResource(building.getBuildingImageRes());
        }

        // Set click listener for the Directions button
        directionsButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.directionButtonOnClick(building);
                dismiss();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        listener = null;
    }

    // for testing
    public void setBuildingInfoListener(BuildingInfoListener listener) {
        this.listener = listener;
    }
}
