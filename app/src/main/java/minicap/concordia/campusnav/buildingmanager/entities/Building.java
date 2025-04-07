package minicap.concordia.campusnav.buildingmanager.entities;

import java.util.Collection;
import java.util.Map;

import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;
import minicap.concordia.campusnav.buildingmanager.enumerations.CampusName;
import minicap.concordia.campusnav.map.MapCoordinates;

public class Building extends Location{
    private String description;
    private CampusName associatedCampus;

    private String buildingAddress;
    private int buildingImageRes;

    private BuildingName buildingIdentifier;
    private Map<String, BuildingFloor> floors;

    private String mapId;


    public Building(MapCoordinates coordinates, String description, CampusName associatedCampus, Map<String, BuildingFloor> floors, int buildingImageRes, String buildingAddress, BuildingName buildingIdentifier, String mapId) {
        super(coordinates);
        this.associatedCampus = associatedCampus;
        this.description = description;
        this.floors = floors;
        this.buildingImageRes = buildingImageRes;
        this.buildingAddress = buildingAddress;
        this.buildingIdentifier = buildingIdentifier;
        this.mapId = mapId;
    }


    /**
     * Gets the BuildingName that represents this building
     * @return BuildingName enum that represents the current building
     */
    public BuildingName getBuildingIdentifier() {
        return buildingIdentifier;
    }

    /**
     * Gets the address for this building
     * @return String address
     */
    public String getBuildingAddress() {
        return buildingAddress;
    }

    /**
     * Gets image resource for this building
     * @return resource id of image
     */
    public int getBuildingImageRes() {
        return buildingImageRes;
    }
    /**
     * Gets the building name
     * @return The building name
     */
    public String getBuildingName() {
        return this.getLocationName();
    }

    /**
     * Gets a description of the building
     * @return A string description of the building
     */
    public String getDescription(){
        return description;
    }

    /**
     * Gets all the floors for this Building
     * @return A collection of all the floors for this building
     */
    public Collection<BuildingFloor> getFloors() {
        return floors.values();
    }

    /**
     * Gets a single floor from the building
     * @param floorName The name of the floor
     * @return The BuildingFloor object associated with this floor
     */
    public BuildingFloor getFloor(String floorName) {
        return floors.get(floorName);
    }

    /**
     * Gets the campus associated with this building
     * @return The CampusName for the campus this building belongs to
     */
    public CampusName getAssociatedCampus(){
        return associatedCampus;
    }

    /**
     * Returns the mapId used for MappedIn
     * @return mapId
     */
    public String getMapId() {
        return mapId;
    }

    @Override
    public String toString(){
        return this.getLocationName();
    }
}
