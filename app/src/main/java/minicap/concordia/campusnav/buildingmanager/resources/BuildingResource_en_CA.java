package minicap.concordia.campusnav.buildingmanager.resources;

import static java.util.Map.entry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ListResourceBundle;
import java.util.Map;

import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.buildingmanager.entities.Building;
import minicap.concordia.campusnav.buildingmanager.entities.BuildingFloor;
import minicap.concordia.campusnav.buildingmanager.entities.Campus;
import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;
import minicap.concordia.campusnav.buildingmanager.enumerations.CampusName;

public class BuildingResource_en_CA extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return new Object[][]{
                {
                    "CampusSGW", new Campus(
                        "Sir George William campus",
                        new ArrayList<BuildingName>(Arrays.asList(
                                BuildingName.HALL,
                                BuildingName.MOLSON_SCHOOL_OF_BUSINESS
                        )),
                        45.49701f,
                        -73.57877f)
                },
                {
                    "CampusLoyola", new Campus(
                        "Loyola campus",
                        new ArrayList<BuildingName>(Arrays.asList(
                                BuildingName.LOYOLA_CENTRAL_BUILDING,
                                BuildingName.VANIER_EXTENSION,
                                BuildingName.VANIER_LIBRARY
                        )),
                        45.45863f,
                        -73.64188f)
                },
                {
                    "BuildingHall", new Building(
                        "Hall building",
                        "The Hall building",
                        CampusName.SGW,
                        new HashMap<String, BuildingFloor>(Map.ofEntries(
                                entry("1", new BuildingFloor("1", BuildingName.HALL, "none")),
                                entry("2", new BuildingFloor("2", BuildingName.HALL, "none")),
                                entry("8", new BuildingFloor("8", BuildingName.HALL, "none")),
                                entry("9", new BuildingFloor("9", BuildingName.HALL, "none"))
                        )),
                        45.49701f,
                        -73.57877f,
                        R.drawable.hallbuilding,
                        "1455 De Maisonneuve Blvd W, Montreal, QC H3G 1M8",
                        BuildingName.HALL
                )},
                {
                    "BuildingJMSB", new Building(
                        "John Molson School of Business",
                        "The John Molson School of Business building",
                        CampusName.SGW,
                        new HashMap<String, BuildingFloor>(Map.ofEntries(
                                entry("1", new BuildingFloor("1", BuildingName.MOLSON_SCHOOL_OF_BUSINESS, "none")),
                                entry("S2", new BuildingFloor("S2", BuildingName.MOLSON_SCHOOL_OF_BUSINESS, "none"))
                        )),
                        45.45863f,
                        -73.57906f,
                        0,
                        "1455 De Maisonneuve Blvd W, Montreal, QC H3G 1M8",
                        BuildingName.MOLSON_SCHOOL_OF_BUSINESS
                )},
                {
                    "BuildingCC", new Building(
                        "Loyola Central building",
                        "The Loyola Central building",
                        CampusName.LOYOLA,
                        new HashMap<>(Map.ofEntries(
                                entry("1", new BuildingFloor("1", BuildingName.LOYOLA_CENTRAL_BUILDING, "none"))
                        )),
                        45.45863f,
                        -73.64066f,
                        0,
                        "1455 De Maisonneuve Blvd W, Montreal, QC H3G 1M8",
                        BuildingName.LOYOLA_CENTRAL_BUILDING
                )},
                {
                    "BuildingVL", new Building(
                        "Vanier library building",
                        "The vanier library building",
                        CampusName.LOYOLA,
                        new HashMap<>(Map.ofEntries(
                                entry("1", new BuildingFloor("1", BuildingName.VANIER_LIBRARY, "none")),
                                entry("2", new BuildingFloor("2", BuildingName.VANIER_LIBRARY, "none"))
                        )),
                        45.45891f,
                        -73.63888f,
                        0,
                        "1455 De Maisonneuve Blvd W, Montreal, QC H3G 1M8",
                        BuildingName.VANIER_LIBRARY
                )},
                {
                    "BuildingVE", new Building(
                        "Vanier Extension Building",
                        "The vanier extension building",
                        CampusName.LOYOLA,
                        new HashMap<>(Map.ofEntries(
                                entry("1", new BuildingFloor("1", BuildingName.VANIER_EXTENSION, "none")),
                                entry("2", new BuildingFloor("2", BuildingName.VANIER_EXTENSION, "none"))
                        )),
                        45.45891f,
                        -73.63888f,
                        0,
                        "1455 De Maisonneuve Blvd W, Montreal, QC H3G 1M8",
                        BuildingName.VANIER_EXTENSION
                )}
        };
    }
}
