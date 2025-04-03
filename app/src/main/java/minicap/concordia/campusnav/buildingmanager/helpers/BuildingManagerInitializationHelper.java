package minicap.concordia.campusnav.buildingmanager.helpers;

import static java.util.Map.entry;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import minicap.concordia.campusnav.buildingmanager.entities.Building;
import minicap.concordia.campusnav.buildingmanager.entities.BuildingFloor;
import minicap.concordia.campusnav.buildingmanager.entities.Campus;
import minicap.concordia.campusnav.buildingmanager.entities.poi.IndoorPOI;
import minicap.concordia.campusnav.buildingmanager.entities.poi.OutdoorPOI;
import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;
import minicap.concordia.campusnav.buildingmanager.enumerations.CampusName;

public class BuildingManagerInitializationHelper {

    /**
     * Creates a map of CampusName, Campus objects that are read from the resource bundle
     * @return Map of CampusName, Campus based on resources
     */
    public static HashMap<CampusName, Campus> createCampuses() {
        CampusName[] campusesToGet = new CampusName[] {CampusName.SGW, CampusName.LOYOLA};
        HashMap<CampusName, Campus> finalCampuses = new HashMap<>();

        try {
            Locale locale = new Locale("en", "CA");
            ResourceBundle bundle = ResourceBundle.getBundle("minicap.concordia.campusnav.buildingmanager.resources.BuildingResource", locale);

            for(int i = 0; i < campusesToGet.length; i++){
                Campus fetchedCampus = (Campus)bundle.getObject(campusesToGet[i].getResourceName());
                finalCampuses.put(campusesToGet[i], fetchedCampus);
            }
        }
        catch(MissingResourceException e){
            Log.d("BuildingManagerInitializationHelper", "Missing campus resource: " + e.getMessage());
        }
        catch(ClassCastException ce){
            Log.d("BuildingManagerInitializationHelper", "One of the resources could not be casted as a campus!\nError: " + ce.getMessage());
        }

        return finalCampuses;
    }

    /**
     * Creates a map of BuildingName, Building based on the resources.
     *  Also populates indoor points of interest for each building floor
     * @return Map of BuildingName, Building based on the resource bundles
     */
    public static HashMap<BuildingName, Building> createBuildings(){
        // When adding a building, add the key and the resource key below
        // Maintaining the order is extremely important
        BuildingName[] buildingsToGet = new BuildingName[] {
                BuildingName.HALL,
                BuildingName.MOLSON_SCHOOL_OF_BUSINESS,
                BuildingName.LOYOLA_CENTRAL_BUILDING,
                BuildingName.VANIER_LIBRARY,
                BuildingName.VANIER_EXTENSION
        };

        HashMap<BuildingName, Building> finalBuildings = new HashMap<>();

        try {
            Locale locale = new Locale("en", "CA");
            ResourceBundle bundle = ResourceBundle.getBundle("minicap.concordia.campusnav.buildingmanager.resources.BuildingResource", locale);
            ResourceBundle indoorPOIBundle = ResourceBundle.getBundle("minicap.concordia.campusnav.buildingmanager.resources.IndoorPOIResource", locale);

            for(int i = 0; i < buildingsToGet.length; i++){
                Building fetchedBuilding = (Building)bundle.getObject(buildingsToGet[i].getResourceName());

                Collection<BuildingFloor> floors = fetchedBuilding.getFloors();
                for(BuildingFloor floor : floors) {
                    String desiredResource = buildingsToGet[i].getResourceName() + "_Floor" + floor.getFloorName();
                    List<IndoorPOI> fetchedPOIs = (List<IndoorPOI>) indoorPOIBundle.getObject(desiredResource);

                    floor.setFloorPOIs(fetchedPOIs);
                }

                finalBuildings.put(buildingsToGet[i], fetchedBuilding);
            }
        }
        catch(MissingResourceException e){
            Log.d("BuildingManagerInitializationHelper", "Missing building resource: " + e.getMessage());
        }
        catch(ClassCastException ce){
            Log.d("BuildingManagerInitializationHelper", "One of the resources could not be casted as a building!\nError: " + ce.getMessage());
        }

        return finalBuildings;
    }

    public static ArrayList<OutdoorPOI> createOutdoorPOIs(){
        ArrayList<OutdoorPOI> outdoorPOIS = new ArrayList<>();

        //Nothing to create for now...

        return outdoorPOIS;
    }
}
