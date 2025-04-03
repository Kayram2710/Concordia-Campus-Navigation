package minicap.concordia.campusnav.buildingmanager.entities;

import java.util.ArrayList;
import java.util.List;

import minicap.concordia.campusnav.buildingmanager.entities.poi.IndoorPOI;
import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;
import minicap.concordia.campusnav.buildingmanager.enumerations.POIType;

public class BuildingFloor {
    private final String floorName;
    private final BuildingName associatedBuilding;
    private final List<IndoorPOI> floorPOIs;
    private final String floorPlanResource;

    public BuildingFloor(String floorName, BuildingName building, String floorPlan){
        this.floorName = floorName;
        this.associatedBuilding = building;
        this.floorPlanResource = floorPlan;
        this.floorPOIs = new ArrayList<IndoorPOI>();
    }

    /**
     * Gets all points of interest for the current floor
     * @return ArrayList of IndoorPOI
     */
    public List<IndoorPOI> getAllPOIsForFloor() {
        return floorPOIs;
    }

    /**
     * Returns the points of interest for the floor with the given type
     * @param type The desired type
     * @return ArrayList of IndoorPOI that match the given type
     */
    public ArrayList<IndoorPOI> getPOIsOfType(POIType type) {
        ArrayList<IndoorPOI> foundPOIs = new ArrayList<>();
        for (IndoorPOI poi:floorPOIs) {
            if(poi.getPOIType() == type){
                foundPOIs.add(poi);
            }
        }
        return foundPOIs;
    }

    /**
     * Returns all points of interest for the floor that have the accessibility flag set to true
     * @return ArrayList of IndoorPOI
     */
    public ArrayList<IndoorPOI> getAccessibilityPOIs() {
        ArrayList<IndoorPOI> accessibilityPOIs = new ArrayList<>();
        for(IndoorPOI poi:floorPOIs){
            if(poi.getIsAccessibilityFeature()){
                accessibilityPOIs.add(poi);
            }
        }
        return accessibilityPOIs;
    }

    /**
     * Gets the floor name
     * @return The floor name
     */
    public String getFloorName() {
        return floorName;
    }

    /**
     * Gets the resource for the floor plan
     * @return The resource for the floor plan
     */
    public String getFloorPlanResource() {
        return floorPlanResource;
    }

    /**
     * Gets the building name associated with this floor
     * @return The BuildingName this floor is associated with
     */
    public BuildingName getAssociatedBuilding() {
        return associatedBuilding;
    }

    /**
     * Sets the IndoorPOIs for this floor
     * @param newPOIs The new list of IndoorPOIs
     */
    public void setFloorPOIs(List<IndoorPOI> newPOIs) {
        this.floorPOIs.clear();
        this.floorPOIs.addAll(newPOIs);
    }
}
