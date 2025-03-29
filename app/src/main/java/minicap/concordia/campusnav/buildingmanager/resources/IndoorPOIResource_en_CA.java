package minicap.concordia.campusnav.buildingmanager.resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListResourceBundle;

import minicap.concordia.campusnav.buildingmanager.entities.poi.IndoorPOI;
import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;
import minicap.concordia.campusnav.buildingmanager.enumerations.POIType;

/**
 * Notes for maintainability:
 *  The naming convention follows: <buildingResourceName>_FloorX
 *  I recommend using the BuildingName's getResourceName for the buildingResourceName
 */
public class IndoorPOIResource_en_CA extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return new Object[][]{
                {
                    BuildingName.HALL.getResourceName() + "_Floor1", new ArrayList<IndoorPOI>(Arrays.asList(
                        new IndoorPOI("Test A", POIType.CLASS_ROOM, BuildingName.HALL, "1", false, 45.49719, -73.57926),
                        new IndoorPOI("Test 2", POIType.WASHROOM, BuildingName.HALL, "1", false, 45.49738, -73.57864),
                        new IndoorPOI("Test Accessibility", POIType.ELEVATOR, BuildingName.HALL, "1", true, 45.9738, -73.57864)
                    ))
                },
                {
                    BuildingName.HALL.getResourceName() + "_Floor2", new ArrayList<IndoorPOI>(Arrays.asList(
                        new IndoorPOI("Test B", POIType.CLASS_ROOM, BuildingName.HALL, "2", false, 45.49719, -73.57926),
                        new IndoorPOI("Test 2", POIType.WASHROOM, BuildingName.HALL, "2", false, 45.49738, -73.57864)
                    ))
                },
                {
                    BuildingName.HALL.getResourceName() + "_Floor3", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.HALL.getResourceName() + "_Floor4", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.HALL.getResourceName() + "_Floor5", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.HALL.getResourceName() + "_Floor6", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.HALL.getResourceName() + "_Floor7", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.HALL.getResourceName() + "_Floor8", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.HALL.getResourceName() + "_Floor9", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.HALL.getResourceName() + "_Floor10", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.HALL.getResourceName() + "_Floor11", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + "_FloorS2", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + "_FloorS1", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + "_Floor1", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + "_Floor2", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + "_Floor3", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + "_Floor4", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + "_Floor5", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + "_Floor6", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + "_Floor7", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + "_Floor8", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + "_Floor9", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + "_Floor10", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + "_Floor11", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + "_Floor12", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + "_Floor13", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + "_Floor14", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + "_Floor15", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.LOYOLA_CENTRAL_BUILDING.getResourceName() + "_Floor1", new ArrayList<>()
                },
                {
                    BuildingName.VANIER_LIBRARY.getResourceName() + "_Floor1", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.VANIER_LIBRARY.getResourceName() + "_Floor2", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.VANIER_LIBRARY.getResourceName() + "_Floor3", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.VANIER_EXTENSION.getResourceName() + "_Floor1", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.VANIER_EXTENSION.getResourceName() + "_Floor2", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.VANIER_EXTENSION.getResourceName() + "_Floor3", new ArrayList<IndoorPOI>()
                }
        };
    }
}
