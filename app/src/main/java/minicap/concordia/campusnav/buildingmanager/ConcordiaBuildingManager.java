package minicap.concordia.campusnav.buildingmanager;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import minicap.concordia.campusnav.buildingmanager.entities.Building;
import minicap.concordia.campusnav.buildingmanager.entities.Campus;
import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;
import minicap.concordia.campusnav.buildingmanager.enumerations.CampusName;
import minicap.concordia.campusnav.buildingmanager.helpers.BuildingManagerInitializationHelper;

public class ConcordiaBuildingManager {

    private static ConcordiaBuildingManager mInstance = null;

    private Map<BuildingName, Building> buildings;
    private Map<CampusName, Campus> campuses;

    private ConcordiaBuildingManager() {
        buildings = new EnumMap<>(BuildingName.class);
        campuses = new EnumMap<>(CampusName.class);
        initialize();
    }

    /**
     * Gets the instance of ConcordiaBuildingManager
     * @return The current instance of ConcordiaBuildingManager
     */
    public static ConcordiaBuildingManager getInstance() {
        if (mInstance == null) {
            mInstance = new ConcordiaBuildingManager();
        }
        return mInstance;
    }

    /**
     * Gets a Building object based on the given name
     * @param name The name of the desired building
     * @return The Building if found, null if not
     */
    public Building getBuilding(BuildingName name) {
        return buildings.get(name);
    }

    /**
     * Gets a Campus based on the given name
     * @param name The desired campus
     * @return The Campus object for the given name, null if not found
     */
    public Campus getCampus(CampusName name){
        return campuses.get(name);
    }

    /**
     * Gets all buildings associated to a given campus
     * @param name The campus for which the buildings are returned
     * @return An ArrayList of Buildings that are related to the given campus
     */
    public List<Building> getBuildingsForCampus(CampusName name){
        Campus campus = null;
        List<BuildingName> buildingNames = new ArrayList<>();
        ArrayList<Building> finalBuildings = new ArrayList<>();

        campus = campuses.get(name);

        if(campus != null){
            buildingNames = campus.getAssociatedBuildings();
        }

        if(!buildingNames.isEmpty()){
            for(BuildingName buildingName:buildingNames){
                Building newBuilding = buildings.get(buildingName);
                if(newBuilding != null){
                    finalBuildings.add(newBuilding);
                }
            }
        }

        return finalBuildings;
    }

    /**
     * Returns a list of buildings that match (partially or otherwise) the provided name
     * @param buildingName The name of the building you are looking for
     * @return List of buildings that match or partially match the searched string
     */
    public List<Building> searchBuildingsByName(String buildingName) {
        ArrayList<Building> foundBuildings = new ArrayList<>();
        String searchName = buildingName.toLowerCase();

        for(Building building: buildings.values()) {
            if(building.getBuildingName().toLowerCase().contains(searchName)) {
                foundBuildings.add(building);
            }
        }

        return foundBuildings;
    }

    /**
     * This method instantiates all the known campuses, buildings and outdoor POIs
     */
    private void initialize() {
        campuses = BuildingManagerInitializationHelper.createCampuses();
        buildings = BuildingManagerInitializationHelper.createBuildings();
    }

    /**
     * Returns an ArrayList of all Buildings (combining all campus buildings).
     * @return A list of every Building stored in this manager.
     */
    public List<Building> getAllBuildings() {
        return new ArrayList<>(buildings.values());
    }
}
