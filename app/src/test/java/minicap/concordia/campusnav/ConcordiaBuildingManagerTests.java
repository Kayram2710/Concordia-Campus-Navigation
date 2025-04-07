package minicap.concordia.campusnav;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import minicap.concordia.campusnav.buildingmanager.ConcordiaBuildingManager;
import minicap.concordia.campusnav.buildingmanager.entities.Building;
import minicap.concordia.campusnav.buildingmanager.entities.BuildingFloor;
import minicap.concordia.campusnav.buildingmanager.entities.Campus;
import minicap.concordia.campusnav.buildingmanager.entities.poi.IndoorPOI;
import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;
import minicap.concordia.campusnav.buildingmanager.enumerations.CampusName;
import minicap.concordia.campusnav.buildingmanager.enumerations.POIType;
import minicap.concordia.campusnav.map.MapCoordinates;


public class ConcordiaBuildingManagerTests {

    @Test
    public void BuildingManager_GetCampus_ReturnsCampus(){
        ConcordiaBuildingManager manager = ConcordiaBuildingManager.getInstance();

        String expectedCampusName = "Sir George William campus";
        float expectedLatitude = 45.49701f;
        float expectedLongitude = -73.57877f;

        Campus fetchedCampus = manager.getCampus(CampusName.SGW);

        Assert.assertEquals(expectedCampusName, fetchedCampus.getCampusName());
        Assert.assertEquals(expectedLatitude, fetchedCampus.getLatitude(), 0.00001);
        Assert.assertEquals(expectedLongitude, fetchedCampus.getLongitude(), 0.00001);
        Assert.assertTrue(fetchedCampus.getAssociatedBuildings().contains(BuildingName.HALL));
        Assert.assertTrue(fetchedCampus.getAssociatedBuildings().contains(BuildingName.MOLSON_SCHOOL_OF_BUSINESS));
    }

    @Test
    public void BuildingManager_GetBuilding_ReturnsCorrectBuilding(){
        ConcordiaBuildingManager manager = ConcordiaBuildingManager.getInstance();

        String expectedBuildingName = "Vanier library building";
        String expectedDescription = "The library is named after Major-General the Right Honourable Georges Philias Vanier, distinguished lawyer, soldier, diplomat, and Governor-General of Canada 1959-67. Vanier was a Loyola graduate (1906) and the recipient of the first Loyola Medal in 1963. In 1966 a 4.1-metre plaster replica of Michelangelo’s David was installed in the Vanier Library, a gift of Simpson's Department Store. It became a Loyola landmark and was the object of a number of student hi-jinks over the years, including painting it emerald green for St. Patrick‘s Day in 1967, and adorning it with fig leaves, hats, banana peels, and diapers.";
        CampusName expectedAssociatedCampus = CampusName.LOYOLA;
        int expectedNumberOfFloors = 3;
        float expectedLatitude = 45.45891f;
        float expectedLongitude = -73.63888f;

        Building actualBuilding = manager.getBuilding(BuildingName.VANIER_LIBRARY);

        Assert.assertEquals(expectedBuildingName, actualBuilding.getBuildingName());
        Assert.assertEquals(expectedDescription, actualBuilding.getDescription());
        Assert.assertEquals(expectedAssociatedCampus, actualBuilding.getAssociatedCampus());
        Assert.assertEquals(expectedNumberOfFloors, actualBuilding.getFloors().size());
        Assert.assertEquals(expectedLatitude, actualBuilding.getLatitude(), 0.00001);
        Assert.assertEquals(expectedLongitude, actualBuilding.getLongitude(), 0.00001);
    }

    @Test
    public void BuildingManager_GetBuildingsForCampus_ReturnsCorrectBuildings(){
        ConcordiaBuildingManager manager = ConcordiaBuildingManager.getInstance();

        int expectedNumberOfBuildings = 36;

        String expectedBuildingNames = "John Molson School of Business";
        String expectedDescriptions =  "In 2009 the John Molson Building officially opened on the corner of Guy and de Maisonneuve. It includes digitally equipped teaching amphitheatres and classrooms, faculty and graduate student offices, the Office of the Dean, student and faculty social space, as well as space for privatized programs. Special features also include case study rooms designed for group work, and laboratories for consumer behaviour research.";
        CampusName expectedAssociatedCampus = CampusName.SGW;
        int expectedNumberOfFloors = 17 ;
        float expectedLatitudes = 45.495323f ;
        float expectedLongitudes = -73.579229f;

        List<Building> actualBuildings = manager.getBuildingsForCampus(CampusName.SGW);

        Assert.assertEquals(expectedNumberOfBuildings, actualBuildings.size());

        Building currentBuilding = actualBuildings.get(15);

        Assert.assertEquals(expectedBuildingNames, currentBuilding.getBuildingName());
        Assert.assertEquals(expectedDescriptions, currentBuilding.getDescription());
        Assert.assertEquals(expectedAssociatedCampus, currentBuilding.getAssociatedCampus());
        Assert.assertEquals(expectedNumberOfFloors, currentBuilding.getFloors().size());
        Assert.assertEquals(expectedLatitudes, currentBuilding.getLatitude(), 0.00001);
        Assert.assertEquals(expectedLongitudes, currentBuilding.getLongitude(), 0.00001);

    }

    @Test
    public void Building_GetFloors_ReturnsCorrectFloors(){
        ConcordiaBuildingManager manager = ConcordiaBuildingManager.getInstance();

        Building hallBuilding = manager.getBuilding(BuildingName.HALL);

        int expectedNumberOfFloors = 11;
        List<String> expectedFloorNames = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"));
        BuildingName expectedBuildingName = BuildingName.HALL;

        Collection<BuildingFloor> floors = hallBuilding.getFloors();

        Assert.assertEquals(expectedNumberOfFloors, floors.size());

        for(BuildingFloor floor:floors){
            Assert.assertTrue(expectedFloorNames.contains(floor.getFloorName()));
            Assert.assertEquals(expectedBuildingName, floor.getAssociatedBuilding());
        }
    }

    @Test
    public void BuildingManager_GetLocation_ReturnsCorrectLocation() {
        ConcordiaBuildingManager manager = ConcordiaBuildingManager.getInstance();

        Building hallBuilding = manager.getBuilding(BuildingName.HALL);

        MapCoordinates expectedCoords = new MapCoordinates(45.49701, -73.57877);

        MapCoordinates actualCoords = hallBuilding.getLocation();

        Assert.assertEquals(expectedCoords.getLat(), actualCoords.getLat(), 0.0001);
        Assert.assertEquals(expectedCoords.getLng(), actualCoords.getLng(), 0.0001);
    }

    @Test
    public void BuildingManager_GetBuildingByName_ReturnsCorrectly() {
        ConcordiaBuildingManager manager = ConcordiaBuildingManager.getInstance();

        Building expected = manager.getBuilding(BuildingName.HALL);

        List<Building> actuals = manager.searchBuildingsByName("Hall building");

        Assert.assertEquals(1, actuals.size());

        Building actualHall = actuals.get(0);

        Assert.assertEquals(expected.getBuildingName(), actualHall.getBuildingName());
        Assert.assertEquals(expected.getBuildingAddress(), actualHall.getBuildingAddress());
        Assert.assertEquals(expected.getBuildingIdentifier(), actualHall.getBuildingIdentifier());
        Assert.assertEquals(expected.getBuildingImageRes(), actualHall.getBuildingImageRes());
        Assert.assertEquals(expected.getDescription(), actualHall.getDescription());
        Assert.assertEquals(expected.getAssociatedCampus(), actualHall.getAssociatedCampus());
        Assert.assertEquals(expected.getFloors(), actualHall.getFloors());
    }

    @Test
    public void BuildingManager_GetAllBuildings() {
        ConcordiaBuildingManager manager = ConcordiaBuildingManager.getInstance();

        int expectedNumberOfBuildings = BuildingName.values().length;

        List<Building> actual = manager.getAllBuildings();

        Assert.assertEquals(expectedNumberOfBuildings, actual.size());
    }

    @Test
    public void BuildingName_GetResourceName() {
        // BuildingName.values respects the order that the enum values are defined (top to bottom)
        String[] expectedResourceNames = new String[] {
                "BuildingPsychology",
                "BuildingGS",
                "BuildingQAnnex",
                "BuildingZAnnex",
                "BuildingCIAnnex",
                "BuildingGuyDeMaisonneuve",
                "BuildingTAnnex",
                "BuildingFAAnnex",
                "BuildingRAnnex",
                "BuildingVL",
                "BuildingHall",
                "BuildingGreyNuns",
                "BuildingMUAnnex",
                "BuildingFCSmith",
                "BuildingPhysicalServices",
                "BuildingKAnnex",
                "BuildingTorontoDominion",
                "BuildingStIgnatiusOfLoyola",
                "BuildingPAnnex",
                "BuildingTerrebonne",
                "BuildingHingstonHallWingHC",
                "BuildingAppliedScienceHub",
                "BuildingBBAnnex",
                "BuildingMIAnnex",
                "BuildingJMSB",
                "BuildingOscarPetersonConcertHall",
                "BuildingVE",
                "BuildingER",
                "BuildingCLAnnex",
                "BuildingLoyolaJesuitHallAndConferenceCentre",
                "BuildingPerformCentre",
                "BuildingQuadrangle",
                "BuildingSolarHouse",
                "BuildingBHAnnex",
                "BuildingPRAnnex",
                "BuildingCommunicationStudiesAndJournalism",
                "BuildingLD",
                "BuildingAdministration",
                "BuildingHingstonHallWingHA",
                "BuildingHingstonHallWingHB",
                "BuildingRecreationAndAthleticsComplex",
                "BuildingLearningSquare",
                "BuildingXAnnex",
                "BuildingEngineeringComputerScienceAndVisualArtsIntegratedComplex",
                "BuildingSamuelBronfman",
                "BuildingFaubourg",
                "BuildingVAnnex",
                "BuildingJWMcConnell",
                "BuildingRichardJRenaudScienceComplex",
                "BuildingFaubourgSteCatherine",
                "BuildingStudentCentre",
                "BuildingCentreForStructuralAndFunctionalGenomics",
                "BuildingBAnnex",
                "BuildingJesuitResidence",
                "BuildingVisualArts",
                "BuildingRRAnnex",
                "BuildingMAnnex",
                "BuildingCC",
                "BuildingENAnnex",
                "BuildingGreyNunsAnnex",
                "BuildingSAnnex",
                "BuildingStingerDome",
                "BuildingDAnnex"
        };

        int i = 0;
        for(BuildingName name : BuildingName.values()) {
            String actual = name.getResourceName();

            Assert.assertEquals(expectedResourceNames[i], actual);
            i++;
        }
    }

    @Test
    public void CampusName_GetResourceName() {
        //CampusName.values respects the order that enum values are defined (top to bottom)
        String[] expectedResourceNames = new String[] {
                "CampusSGW",
                "CampusLoyola"
        };
        int i = 0;
        for(CampusName name : CampusName.values()) {
            String actual = name.getResourceName();

            Assert.assertEquals(expectedResourceNames[i], actual);
            i++;
        }
    }

    @Test
    public void Building_GetFloors() {
        int expectedNumberOfFloors = 11;
        BuildingName expectedAssociatedBuilding = BuildingName.HALL;

        ConcordiaBuildingManager manager = ConcordiaBuildingManager.getInstance();
        Building actualHall = manager.getBuilding(BuildingName.HALL);
        Collection<BuildingFloor> actualFloors = actualHall.getFloors();

        Assert.assertEquals(expectedNumberOfFloors, actualFloors.size());

        for(BuildingFloor floor : actualFloors) {
            Assert.assertEquals(expectedAssociatedBuilding, floor.getAssociatedBuilding());
        }
    }

    @Test
    public void Building_GetSingleFloor() {
        BuildingName expectedAssociatedBuilding = BuildingName.HALL;
        String expectedFloorName = "1";

        ConcordiaBuildingManager manager = ConcordiaBuildingManager.getInstance();
        Building actualHall = manager.getBuilding(BuildingName.HALL);
        BuildingFloor actualFloor = actualHall.getFloor("1");

        Assert.assertEquals(expectedAssociatedBuilding, actualFloor.getAssociatedBuilding());
        Assert.assertEquals(expectedFloorName, actualFloor.getFloorName());
    }

    @Test
    public void Building_GetIdentifier() {
        BuildingName expectedIdentifier = BuildingName.MOLSON_SCHOOL_OF_BUSINESS;

        ConcordiaBuildingManager manager = ConcordiaBuildingManager.getInstance();

        Building actual = manager.getBuilding(BuildingName.MOLSON_SCHOOL_OF_BUSINESS);

        Assert.assertEquals(expectedIdentifier, actual.getBuildingIdentifier());
    }

    @Test
    public void BuildingFloor_GetAllPOIs() {
        int expectedNumberOfPOIs = 74;
        BuildingName expectedAssociatedBuilding = BuildingName.HALL;
        String expectedFloorName = "1";

        ConcordiaBuildingManager manager = ConcordiaBuildingManager.getInstance();
        Building actualBuilding = manager.getBuilding(BuildingName.HALL);

        BuildingFloor actualFloor = actualBuilding.getFloor("1");
        List<IndoorPOI> actualPOIs = actualFloor.getAllPOIsForFloor();

        Assert.assertEquals(expectedNumberOfPOIs, actualPOIs.size());

        for(IndoorPOI poi : actualPOIs) {
            Assert.assertEquals(expectedAssociatedBuilding, poi.getAssociatedBuilding());
            Assert.assertEquals(expectedFloorName, poi.getFloorName());
        }
    }

    @Test
    public void BuildingFloor_GetPOIOfTypeClassroom() {
        int expectedClassroomPOIs = 61;
        String expectedFloorName = "1";
        BuildingName expectedAssociatedBuilding = BuildingName.HALL;
        POIType expectedPOIType = POIType.CLASS_ROOM;

        ConcordiaBuildingManager manager = ConcordiaBuildingManager.getInstance();
        Building actualBuilding = manager.getBuilding(BuildingName.HALL);

        BuildingFloor actualFloor = actualBuilding.getFloor("1");
        List<IndoorPOI> actualPOIs = actualFloor.getPOIsOfType(POIType.CLASS_ROOM);

        Assert.assertEquals(expectedClassroomPOIs, actualPOIs.size());

        for(IndoorPOI poi : actualPOIs) {
            Assert.assertEquals(expectedAssociatedBuilding, poi.getAssociatedBuilding());
            Assert.assertEquals(expectedPOIType, poi.getPOIType());
            Assert.assertEquals(expectedFloorName, poi.getFloorName());
        }
    }

    @Test
    public void BuildingFloor_GetAccessibilityPOIs() {
        int expectedAccessibilityPOIs = 3;
        String expectedFloorName = "1";
        BuildingName expectedAssociatedBuilding = BuildingName.HALL;
        boolean expectedAccessibilityFlag = true;

        ConcordiaBuildingManager manager = ConcordiaBuildingManager.getInstance();
        Building actualBuilding = manager.getBuilding(BuildingName.HALL);

        BuildingFloor actualFloor = actualBuilding.getFloor("1");
        List<IndoorPOI> actualPOIs = actualFloor.getAccessibilityPOIs();

        Assert.assertEquals(expectedAccessibilityPOIs, actualPOIs.size());

        for(IndoorPOI poi : actualPOIs) {
            Assert.assertEquals(expectedAssociatedBuilding, poi.getAssociatedBuilding());
            Assert.assertEquals(expectedFloorName, poi.getFloorName());
            Assert.assertEquals(expectedAccessibilityFlag, poi.getIsAccessibilityFeature());
        }
    }

    @Test
    public void IndoorPOI_GetPOIName() {
        List<String> expectedPOINames = new ArrayList<>(Arrays.asList(
                "JMSB S2.210",
                "JMSB S2.275"
        ));

        int expectedNumberPOIs = expectedPOINames.size();

        String expectedFloorName = "S2";
        BuildingName expectedAssociatedBuilding = BuildingName.MOLSON_SCHOOL_OF_BUSINESS;

        ConcordiaBuildingManager manager = ConcordiaBuildingManager.getInstance();
        Building actualBuilding = manager.getBuilding(BuildingName.MOLSON_SCHOOL_OF_BUSINESS);

        BuildingFloor actualFloor = actualBuilding.getFloor("S2");
        List<IndoorPOI> actualPOIs = actualFloor.getAllPOIsForFloor();

        Assert.assertEquals(expectedNumberPOIs, actualPOIs.size());

        for(IndoorPOI poi : actualPOIs) {
            Assert.assertEquals(expectedAssociatedBuilding, poi.getAssociatedBuilding());
            Assert.assertEquals(expectedFloorName, poi.getFloorName());
            Assert.assertTrue(expectedPOINames.contains(poi.getPoiName()));
        }
    }
}
