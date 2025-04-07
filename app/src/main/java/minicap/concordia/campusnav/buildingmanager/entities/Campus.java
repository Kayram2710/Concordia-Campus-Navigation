package minicap.concordia.campusnav.buildingmanager.entities;

import java.util.List;

import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;
import minicap.concordia.campusnav.map.MapCoordinates;

public class Campus extends Location {

    private List<BuildingName> associatedBuildings;

    public Campus(MapCoordinates coordinates, List<BuildingName> associatedBuildings) {
        super(coordinates);
        this.associatedBuildings = associatedBuildings;
    }

    /**
     * Gets the BuildingNames that are associated with this campus
     * @return The BuildingNames that are associated with this campus
     */
    public List<BuildingName> getAssociatedBuildings() {
        return associatedBuildings;
    }

    /**
     * Gets the name of the campus
     * @return The name of the campus
     */
    public String getCampusName(){
        return this.getLocationName();
    }
}
