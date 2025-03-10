package minicap.concordia.campusnav.buildingmanager.entities;

import java.util.Collection;
import java.util.HashMap;

import minicap.concordia.campusnav.buildingmanager.enumerations.CampusName;

public class Building extends Location{

    private String buildingName;
    private String description;
    private CampusName associatedCampus;
    private HashMap<String, BuildingFloor> floors;

    public Building(String buildingName, String description, CampusName associatedCampus, HashMap<String, BuildingFloor> floors, float latitude, float longitude) {
        super(latitude, longitude);
        this.associatedCampus = associatedCampus;
        this.buildingName = buildingName;
        this.description = description;
        this.floors = floors;
    }

    /**
     * Gets the building name
     * @return The building name
     */
    public String getBuildingName() {
        return buildingName;
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
     * Gets the campus associated with this building
     * @return The CampusName for the campus this building belongs to
     */
    public CampusName getAssociatedCampus(){
        return associatedCampus;
    }

    @Override
    public String toString(){
        return this.buildingName;
    }
}
