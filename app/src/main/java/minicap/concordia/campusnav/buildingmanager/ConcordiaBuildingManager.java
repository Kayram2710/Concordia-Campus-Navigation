package minicap.concordia.campusnav.buildingmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import minicap.concordia.campusnav.buildingmanager.entities.Building;
import minicap.concordia.campusnav.buildingmanager.entities.Campus;
import minicap.concordia.campusnav.buildingmanager.entities.poi.OutdoorPOI;
import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;
import minicap.concordia.campusnav.buildingmanager.enumerations.CampusName;
import minicap.concordia.campusnav.buildingmanager.enumerations.POIType;
import minicap.concordia.campusnav.buildingmanager.helpers.BuildingManagerInitializationHelper;

public class ConcordiaBuildingManager {

    private static ConcordiaBuildingManager mInstance = null;

    private HashMap<BuildingName, Building> buildings;
    private HashMap<CampusName, Campus> campuses;
    private ArrayList<OutdoorPOI> outdoorPOIs;

    private ConcordiaBuildingManager() {
        buildings = new HashMap<>();
        campuses = new HashMap<>();
        outdoorPOIs = new ArrayList<>();
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
    public ArrayList<Building> getBuildingsForCampus(CampusName name){
        Campus campus = null;
        ArrayList<BuildingName> buildingNames = new ArrayList<>();
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
     * Returns all outdoor points of interest
     * @return ArrayList of all OutdoorPOI
     */
    public ArrayList<OutdoorPOI> getAllOutdoorPOIs(){
        return outdoorPOIs;
    }

    /**
     * Returns an ArrayList of OutdoorPOI that match the specified type
     * @param type The type of POI that is desired
     * @return ArrayList with all OutdoorPOI that match the specified type
     */
    public ArrayList<OutdoorPOI> getAllOutdoorPOIsOfType(POIType type) {
        ArrayList<OutdoorPOI> finalPOIs = new ArrayList<>();

        for(OutdoorPOI poi:outdoorPOIs){
            if(poi.getPOIType() == type){
                finalPOIs.add(poi);
            }
        }

        return finalPOIs;
    }

    /**
     * Returns an ArrayList of all OutdoorPOIs that have the accessibility flag regardless of type.
     * @return ArrayList of OutdoorPOIs with accessibility flag
     */
    public ArrayList<OutdoorPOI> getOutdoorAccessibilityPOIs(){
        ArrayList<OutdoorPOI> finalPOIs = new ArrayList<>();

        for(OutdoorPOI poi:outdoorPOIs){
            if(poi.getIsAccessibilityFeature()){
                finalPOIs.add(poi);
            }
        }

        return finalPOIs;
    }

    /**
     * Gets all outdoor POIs that have the accessibility flag and the desired type
     * @param type The POI type that is being searched for
     * @return ArrayList of OutdoorPOI that match the given type and have the accessibility flag
     */
    public ArrayList<OutdoorPOI> getOutdoorAccessibilityPOIs(POIType type){
        ArrayList<OutdoorPOI> finalPOIs = new ArrayList<>();

        for(OutdoorPOI poi:outdoorPOIs) {
            if(poi.getIsAccessibilityFeature() && poi.getPOIType() == type){
                finalPOIs.add(poi);
            }
        }

        return finalPOIs;
    }

    /**
     * This method instantiates all the known campuses, buildings and outdoor POIs
     */
    private void initialize() {
        campuses = BuildingManagerInitializationHelper.createCampuses();
        buildings = BuildingManagerInitializationHelper.createBuildings();
        outdoorPOIs = BuildingManagerInitializationHelper.createOutdoorPOIs();
    }

    /**
     * Returns an ArrayList of all Buildings (combining all campus buildings).
     * @return A list of every Building stored in this manager.
     */
    public ArrayList<Building> getAllBuildings() {
        return new ArrayList<>(buildings.values());
    }
}
