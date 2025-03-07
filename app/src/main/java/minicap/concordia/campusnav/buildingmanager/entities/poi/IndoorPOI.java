package minicap.concordia.campusnav.buildingmanager.entities.poi;

import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;
import minicap.concordia.campusnav.buildingmanager.enumerations.POIType;

public class IndoorPOI extends POI {

    private BuildingName associatedBuilding;
    private String floorName;

    public IndoorPOI(String name, POIType type, BuildingName associatedBuilding, String floorName, boolean isAccessibilityFeature, float latitude, float longitude) {
        super(name, type, isAccessibilityFeature, latitude, longitude);
        this.associatedBuilding = associatedBuilding;
        this.floorName = floorName;
    }

    /**
     * Gets the building associated with this point of interest
     * @return The BuildingName associated with this POI
     */
    public BuildingName getAssociatedBuilding() {
        return associatedBuilding;
    }

    /**
     * Gets the name of the floor this point of interest is associated with
     * @return A string representing the floor name
     */
    public String getFloorName() {
        return floorName;
    }
}
