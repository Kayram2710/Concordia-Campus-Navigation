package minicap.concordia.campusnav.map;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.mappedin.sdk.MPIMapView;
import com.mappedin.sdk.listeners.MPIMapClickListener;
import com.mappedin.sdk.listeners.MPIMapViewListener;
import com.mappedin.sdk.models.MPIBlueDotPositionUpdate;
import com.mappedin.sdk.models.MPIBlueDotStateChange;
import com.mappedin.sdk.models.MPIData;
import com.mappedin.sdk.models.MPIMap;
import com.mappedin.sdk.models.MPIMapClickEvent;
import com.mappedin.sdk.models.MPINavigatable;
import com.mappedin.sdk.models.MPIState;
import com.mappedin.sdk.web.MPIOptions;

import minicap.concordia.campusnav.buildingmanager.entities.poi.OutdoorPOI;
import minicap.concordia.campusnav.buildingmanager.enumerations.POIType;
import minicap.concordia.campusnav.components.MappedInFragment;
import minicap.concordia.campusnav.map.enums.MapColors;
import minicap.concordia.campusnav.map.helpers.MapColorConversionHelper;

public class InternalMappedIn extends AbstractMap implements MPIMapViewListener, MPIMapClickListener {

    private MappedInFragment mappedInFragment;

    private MPIMapView curMap;

    public InternalMappedIn(MapUpdateListener listener) {
        super(listener);
    }

    @Override
    public Fragment initialize() {
        mappedInFragment = MappedInFragment.newInstance(this, this);

        return mappedInFragment;
    }

    @Override
    public void addMarker(OutdoorPOI opoi) {
        //TODO
    }

    @Override
    public void addMarker(MapCoordinates position, String title, MapColors color, boolean clearOtherMarkers) {
        if(clearOtherMarkers) {
            clearAllMarkers();
        }

        String markerHTML = MapColorConversionHelper.getMappedInMarkerHTML(color, title);

        MPIMap.MPICoordinate coord = position.toMappedInCoordinate(curMap.getCurrentMap());
        curMap.getMarkerManager().addByCoordinate(coord,
                markerHTML,
                new MPIOptions.Marker(),
                null);
    }

    @Override
    public void addMarker(MapCoordinates position, String title, MapColors color) {
        addMarker(position, title, color, false);
    }

    @Override
    public void addMarker(MapCoordinates position, String title, boolean clearOtherMarkers) {
        addMarker(position, title, MapColors.DEFAULT, clearOtherMarkers);
    }

    @Override
    public void addMarker(MapCoordinates position, String title) {
        addMarker(position, title, MapColors.DEFAULT, false);
    }

    @Override
    public void clearAllMarkers() {
        curMap.getMarkerManager().removeAll();
    }

    @Override
    public void clearPathFromMap() {

    }

    @Override
    public void displayRoute(MapCoordinates origin, MapCoordinates destination, String travelMode) {

    }

    @Override
    public void displayPOI(MapCoordinates origin, POIType type) {

    }

    @Override
    public void centerOnCoordinates(MapCoordinates newCameraPosition) {

    }

    @Override
    public void switchToFloor(String floorName) {

    }

    @Override
    public boolean toggleLocationTracking(boolean isEnabled) {
        if(isEnabled) {
            curMap.getBlueDotManager().enable(new MPIOptions.BlueDot());
        }
        else {
            curMap.getBlueDotManager().disable();
        }
        return true;
    }

    @Override
    public void setStyle(Context context, int resourceID) {
        //Not used
    }

    @Override
    public void onBlueDotPositionUpdate(@NonNull MPIBlueDotPositionUpdate mpiBlueDotPositionUpdate) {
        //To be updated
    }

    @Override
    public void onDataLoaded(@NonNull MPIData mpiData) {
        //Not used
    }

    @Override
    public void onMapChanged(@NonNull MPIMap mpiMap) {
        //To be implemented when switching floors
    }

    @Override
    public void onPolygonClicked(@NonNull MPINavigatable.MPIPolygon mpiPolygon) {
        //Maybe used in the future
    }

    @Override
    public void onNothingClicked() {
        //Not used
    }

    @Override
    public void onFirstMapLoaded() {
        Log.d("InternalMappedIn", "First map loaded");

        curMap = mappedInFragment.getMap();

        listener.onMapReady();
    }

    @Override
    public void onStateChanged(@NonNull MPIState mpiState) {
        //Not used
    }

    @Override
    public void onBlueDotStateChange(@NonNull MPIBlueDotStateChange mpiBlueDotStateChange) {
        //Not used
    }

    @Override
    public void onClick(@NonNull MPIMapClickEvent mpiMapClickEvent) {
        MPIMap.MPICoordinate coordinate = mpiMapClickEvent.getPosition();

        listener.onMapClicked(MapCoordinates.fromMappedInCoordinate(coordinate));
    }
}
