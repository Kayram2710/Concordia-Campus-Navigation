package minicap.concordia.campusnav.map;

import android.content.Context;

import androidx.fragment.app.Fragment;

import minicap.concordia.campusnav.buildingmanager.entities.Building;
import minicap.concordia.campusnav.buildingmanager.entities.BuildingFloor;
import minicap.concordia.campusnav.buildingmanager.entities.poi.OutdoorPOI;
import minicap.concordia.campusnav.buildingmanager.enumerations.POIType;
import minicap.concordia.campusnav.components.MappedInWebViewFragment;
import minicap.concordia.campusnav.map.enums.MapColors;

public class InternalMappedIn extends AbstractMap implements MappedInWebViewFragment.MappedInMapEventListener {

    private Building currentBuilding;
    private MappedInWebViewFragment mappedInFragment;

    public InternalMappedIn(MapUpdateListener listener) {
        super(listener);
        this.isIndoor = true;
    }

    @Override
    public Fragment initialize() {
        mappedInFragment = MappedInWebViewFragment.newInstance(this);

        return mappedInFragment;
    }

    @Override
    public void addMarker(OutdoorPOI opoi) {
        //Not used in Indoor
    }

    @Override
    public void addMarker(MapCoordinates position, String title, MapColors color, boolean clearOtherMarkers) {
        if(clearOtherMarkers) {
            clearAllMarkers();
        }

        mappedInFragment.addMarker(position, title);
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
        mappedInFragment.removeAllMarkers();
    }

    @Override
    public void clearPathFromMap() {
        mappedInFragment.clearPath();
    }

    @Override
    public void displayRoute(MapCoordinates origin, MapCoordinates destination, String travelMode) {
        boolean isAccessibility = travelMode.equals("WHEELCHAIR");

        BuildingFloor startFloor = currentBuilding.getFloor(origin.getName());
        BuildingFloor endFloor = currentBuilding.getFloor(destination.getName());

        String startFloorId = startFloor == null ? null : startFloor.getFloorId();
        String endFloorId = endFloor == null ? null : endFloor.getFloorId();

        MapCoordinates startWithFloor = new MapCoordinates(origin.getLat(), origin.getLng(), startFloorId);
        MapCoordinates endWithFloor = new MapCoordinates(destination.getLat(), destination.getLng(), endFloorId);

        mappedInFragment.drawPath(startWithFloor, endWithFloor, isAccessibility);
    }

    @Override
    public void displayPOI(MapCoordinates origin, POIType type) {
        //Not used
    }

    @Override
    public void centerOnCoordinates(MapCoordinates newCameraPosition) {
        //Not used
    }

    @Override
    public void loadBuilding(Building building, String initialFloor) {
        currentBuilding = building;
        String buildingId = building.getMapId();
        BuildingFloor floor = building.getFloor(initialFloor);
        String floorId = floor.getFloorId();

        mappedInFragment.loadMap(buildingId, floorId);
    }

    @Override
    public void switchFloor(String floor) {
        BuildingFloor newFloor = currentBuilding.getFloor(floor);
        String newFloorId = newFloor.getFloorId();

        mappedInFragment.switchFloor(newFloorId);
    }

    @Override
    public boolean toggleLocationTracking(boolean isEnabled) {
        mappedInFragment.toggleLocationTracking(isEnabled);
        return true;
    }

    @Override
    public void setStyle(Context context, int resourceID) {
        //Not used
    }

    @Override
    public void mapPageLoaded() {
        listener.onMapElementLoaded();
    }

    @Override
    public void mapLoaded() {
        listener.onMapReady();
    }

    @Override
    public void mapClicked(MapCoordinates coords) {
        listener.onMapClicked(coords);
    }
}
