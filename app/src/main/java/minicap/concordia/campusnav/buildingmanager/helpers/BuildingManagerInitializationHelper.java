package minicap.concordia.campusnav.buildingmanager.helpers;

import android.util.Log;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import minicap.concordia.campusnav.buildingmanager.entities.Building;
import minicap.concordia.campusnav.buildingmanager.entities.BuildingFloor;
import minicap.concordia.campusnav.buildingmanager.entities.Campus;
import minicap.concordia.campusnav.buildingmanager.entities.poi.IndoorPOI;
import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;
import minicap.concordia.campusnav.buildingmanager.enumerations.CampusName;
import minicap.concordia.campusnav.buildingmanager.resources.IndoorPOIResource_en_CA;

public class BuildingManagerInitializationHelper {

    private static final String TAG = "BuildingManagerInitializationHelper";

    private BuildingManagerInitializationHelper() {}

    /**
     * Creates a map of CampusName, Campus objects that are read from the resource bundle
     * @return Map of CampusName, Campus based on resources
     */
    public static Map<CampusName, Campus> createCampuses() {
        CampusName[] campusesToGet = new CampusName[] {CampusName.SGW, CampusName.LOYOLA};
        Map<CampusName, Campus> finalCampuses = new EnumMap<>(CampusName.class);

        try {
            Locale locale = new Locale("en", "CA");
            ResourceBundle bundle = ResourceBundle.getBundle("minicap.concordia.campusnav.buildingmanager.resources.BuildingResource", locale);

            for(int i = 0; i < campusesToGet.length; i++){
                Campus fetchedCampus = (Campus)bundle.getObject(campusesToGet[i].getResourceName());
                finalCampuses.put(campusesToGet[i], fetchedCampus);
            }
        }
        catch(MissingResourceException e){
            Log.d(TAG, "Missing campus resource: " + e.getMessage());
        }
        catch(ClassCastException ce){
            Log.d(TAG, "One of the resources could not be casted as a campus!\nError: " + ce.getMessage());
        }

        return finalCampuses;
    }

    /**
     * Creates a map of BuildingName, Building based on the resources.
     *  Also populates indoor points of interest for each building floor
     * @return Map of BuildingName, Building based on the resource bundles
     */
    public static Map<BuildingName, Building> createBuildings(){
        // When adding a building, add the key and the resource key below
        // Maintaining the order is extremely important
        BuildingName[] buildingsToGet = new BuildingName[] {
                BuildingName.PSYCHOLOGY_BUILDING,
                BuildingName.GS_BUILDING,
                BuildingName.Q_ANNEX,
                BuildingName.Z_ANNEX,
                BuildingName.CI_ANNEX,
                BuildingName.GUY_DE_MAISONNEUVE_BUILDING,
                BuildingName.T_ANNEX,
                BuildingName.FA_ANNEX,
                BuildingName.R_ANNEX,
                BuildingName.VANIER_LIBRARY,
                BuildingName.HALL,
                BuildingName.GREY_NUNS_BUILDING,
                BuildingName.MU_ANNEX,
                BuildingName.F_C_SMITH_BUILDING,
                BuildingName.PHYSICAL_SERVICES_BUILDING,
                BuildingName.K_ANNEX,
                BuildingName.TORONTO_DOMINION_BUILDING,
                BuildingName.ST_IGNATIUS_OF_LOYOLA_CHURCH,
                BuildingName.P_ANNEX,
                BuildingName.TERREBONNE_BUILDING,
                BuildingName.HINGSTON_HALL_WING_HC,
                BuildingName.APPLIED_SCIENCE_HUB,
                BuildingName.BB_ANNEX,
                BuildingName.MI_ANNEX,
                BuildingName.MOLSON_SCHOOL_OF_BUSINESS,
                BuildingName.OSCAR_PETERSON_CONCERT_HALL,
                BuildingName.VANIER_EXTENSION,
                BuildingName.ER_BUILDING,
                BuildingName.CL_ANNEX,
                BuildingName.LOYOLA_JESUIT_HALL_AND_CONFERENCE_CENTRE,
                BuildingName.PERFORM_CENTRE,
                BuildingName.QUADRANGLE,
                BuildingName.SOLAR_HOUSE,
                BuildingName.BH_ANNEX,
                BuildingName.PR_ANNEX,
                BuildingName.COMMUNICATION_STUDIES_AND_JOURNALISM_BUILDING,
                BuildingName.LD_BUILDING,
                BuildingName.ADMINISTRATION_BUILDING,
                BuildingName.HINGSTON_HALL_WING_HA,
                BuildingName.HINGSTON_HALL_WING_HB,
                BuildingName.RECREATION_AND_ATHLETICS_COMPLEX,
                BuildingName.LEARNING_SQUARE,
                BuildingName.X_ANNEX,
                BuildingName.ENGINEERING_COMPUTER_SCIENCE_AND_VISUAL_ARTS_INTEGRATED_COMPLEX,
                BuildingName.SAMUEL_BRONFMAN_BUILDING,
                BuildingName.FAUBOURG_BUILDING,
                BuildingName.V_ANNEX,
                BuildingName.JW_MCCONNELL_BUILDING,
                BuildingName.RICHARD_J_RENAUD_SCIENCE_COMPLEX,
                BuildingName.FAUBOURG_STE_CATHERINE_BUILDING,
                BuildingName.STUDENT_CENTRE,
                BuildingName.CENTRE_FOR_STRUCTURAL_AND_FUNCTIONAL_GENOMICS,
                BuildingName.B_ANNEX,
                BuildingName.JESUIT_RESIDENCE,
                BuildingName.VISUAL_ARTS_BUILDING,
                BuildingName.RR_ANNEX,
                BuildingName.M_ANNEX,
                BuildingName.LOYOLA_CENTRAL_BUILDING,
                BuildingName.EN_ANNEX,
                BuildingName.GREY_NUNS_ANNEX,
                BuildingName.S_ANNEX,
                BuildingName.STINGER_DOME,
                BuildingName.D_ANNEX
        };

        EnumMap<BuildingName, Building> finalBuildings = new EnumMap<>(BuildingName.class);

        try {
            Locale locale = new Locale("en", "CA");
            ResourceBundle bundle = ResourceBundle.getBundle("minicap.concordia.campusnav.buildingmanager.resources.BuildingResource", locale);
            ResourceBundle indoorPOIBundle = ResourceBundle.getBundle("minicap.concordia.campusnav.buildingmanager.resources.IndoorPOIResource", locale);

            for(int i = 0; i < buildingsToGet.length; i++){
                Building fetchedBuilding = (Building)bundle.getObject(buildingsToGet[i].getResourceName());

                Collection<BuildingFloor> floors = fetchedBuilding.getFloors();
                for(BuildingFloor floor : floors) {
                    String desiredResource = buildingsToGet[i].getResourceName() + IndoorPOIResource_en_CA.FLOOR_TAG + floor.getFloorName();
                    List<IndoorPOI> fetchedPOIs = (List<IndoorPOI>) indoorPOIBundle.getObject(desiredResource);

                    floor.setFloorPOIs(fetchedPOIs);
                }

                finalBuildings.put(buildingsToGet[i], fetchedBuilding);
            }
        }
        catch(MissingResourceException e){
            Log.d(TAG, "Missing building resource: " + e.getMessage());
        }
        catch(ClassCastException ce){
            Log.d(TAG, "One of the resources could not be casted as a building!\nError: " + ce.getMessage());
        }

        return finalBuildings;
    }
}
