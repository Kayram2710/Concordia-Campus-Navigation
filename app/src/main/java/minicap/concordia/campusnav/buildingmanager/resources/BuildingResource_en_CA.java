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

/**
 * Notes for maintainability:
 *   If you add a building, use the BuildingName.getResourceName() method to make it standard
 *   Also, add the building to the list in the BuildingManagerInitializationHelper.createBuildings()
 *   If you add a floor to a building, add the corresponding entry in IndoorPOIResource, or you will get a MissingResourceException
 */
public class BuildingResource_en_CA extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return new Object[][]{
                {
                    CampusName.SGW.getResourceName(), new Campus(
                        "Sir George William campus",
                        new ArrayList<BuildingName>(Arrays.asList(
                                BuildingName.HALL,
                                BuildingName.MOLSON_SCHOOL_OF_BUSINESS
                        )),
                        45.49701,
                        -73.57877)
                },
                {
                    CampusName.LOYOLA.getResourceName(), new Campus(
                        "Loyola campus",
                        new ArrayList<BuildingName>(Arrays.asList(
                                BuildingName.LOYOLA_CENTRAL_BUILDING,
                                BuildingName.VANIER_EXTENSION,
                                BuildingName.VANIER_LIBRARY
                        )),
                        45.45863,
                        -73.64188)
                },
                {
                    BuildingName.HALL.getResourceName(), new Building(
                        "Hall building",
                        "The Henry F. Hall Building is a high-density hub, located on De Maisonneuve Boulevard, on Concordia’s downtown Sir-George-Williams Campus.\nThe cube-like structure was completed in 1966. Its exterior is made of pre-fabricated, stressed concrete, a feature of the brutalist movement, often associated with French architect Le Corbusier.",
                        CampusName.SGW,
                        new HashMap<String, BuildingFloor>(Map.ofEntries(
                                entry("1", new BuildingFloor("1", BuildingName.HALL, "none")),
                                entry("2", new BuildingFloor("2", BuildingName.HALL, "none")),
                                entry("3", new BuildingFloor("3", BuildingName.HALL, "none")),
                                entry("4", new BuildingFloor("4", BuildingName.HALL, "none")),
                                entry("5", new BuildingFloor("5", BuildingName.HALL, "none")),
                                entry("6", new BuildingFloor("6", BuildingName.HALL, "none")),
                                entry("7", new BuildingFloor("7", BuildingName.HALL, "none")),
                                entry("8", new BuildingFloor("8", BuildingName.HALL, "none")),
                                entry("9", new BuildingFloor("9", BuildingName.HALL, "none")),
                                entry("10", new BuildingFloor("10", BuildingName.HALL, "none")),
                                entry("11", new BuildingFloor("11", BuildingName.HALL, "none"))
                        )),
                        45.49701,
                        -73.57877,
                        R.drawable.hallbuilding,
                        "1455 De Maisonneuve Blvd W, Montreal, QC H3G 1M8",
                        BuildingName.HALL
                )},
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName(), new Building(
                        "John Molson School of Business",
                        "In 2009 the John Molson Building officially opened on the corner of Guy and de Maisonneuve. It includes digitally equipped teaching amphitheatres and classrooms, faculty and graduate student offices, the Office of the Dean, student and faculty social space, as well as space for privatized programs. Special features also include case study rooms designed for group work, and laboratories for consumer behaviour research.",
                        CampusName.SGW,
                        new HashMap<String, BuildingFloor>(Map.ofEntries(
                                entry("S2", new BuildingFloor("S2", BuildingName.MOLSON_SCHOOL_OF_BUSINESS, "none")),
                                entry("S1", new BuildingFloor("S1", BuildingName.MOLSON_SCHOOL_OF_BUSINESS, "none")),
                                entry("1", new BuildingFloor("1", BuildingName.MOLSON_SCHOOL_OF_BUSINESS, "none")),
                                entry("2", new BuildingFloor("2", BuildingName.MOLSON_SCHOOL_OF_BUSINESS, "none")),
                                entry("3", new BuildingFloor("3", BuildingName.MOLSON_SCHOOL_OF_BUSINESS, "none")),
                                entry("4", new BuildingFloor("4", BuildingName.MOLSON_SCHOOL_OF_BUSINESS, "none")),
                                entry("5", new BuildingFloor("5", BuildingName.MOLSON_SCHOOL_OF_BUSINESS, "none")),
                                entry("6", new BuildingFloor("6", BuildingName.MOLSON_SCHOOL_OF_BUSINESS, "none")),
                                entry("7", new BuildingFloor("7", BuildingName.MOLSON_SCHOOL_OF_BUSINESS, "none")),
                                entry("8", new BuildingFloor("8", BuildingName.MOLSON_SCHOOL_OF_BUSINESS, "none")),
                                entry("9", new BuildingFloor("9", BuildingName.MOLSON_SCHOOL_OF_BUSINESS, "none")),
                                entry("10", new BuildingFloor("10", BuildingName.MOLSON_SCHOOL_OF_BUSINESS, "none")),
                                entry("11", new BuildingFloor("11", BuildingName.MOLSON_SCHOOL_OF_BUSINESS, "none")),
                                entry("12", new BuildingFloor("12", BuildingName.MOLSON_SCHOOL_OF_BUSINESS, "none")),
                                entry("13", new BuildingFloor("13", BuildingName.MOLSON_SCHOOL_OF_BUSINESS, "none")),
                                entry("14", new BuildingFloor("14", BuildingName.MOLSON_SCHOOL_OF_BUSINESS, "none")),
                                entry("15", new BuildingFloor("15", BuildingName.MOLSON_SCHOOL_OF_BUSINESS, "none"))
                        )),
                        45.45863,
                        -73.57906,
                        R.drawable.jmsb,
                        "1450 Guy Street Montreal, QC H3H 0A1",
                        BuildingName.MOLSON_SCHOOL_OF_BUSINESS
                )},
                {
                    BuildingName.LOYOLA_CENTRAL_BUILDING.getResourceName(), new Building(
                        "Loyola Central building",
                        "The first storey of the Central Building was occupied in 1945, but the completed building was officially opened in 1947, and it held classrooms, the library on nearly all of the top floor, a gymnasium, and student bedrooms at the south end of the second floor. The Guadagni Lounge in the former library space is named for Franco Guadagni, Loyola professor from 1942 until his death in 1964. He was the only Engineering faculty member from 1942 until 1959, and also taught all the Chemistry courses between 1942 and 1953.",
                        CampusName.LOYOLA,
                        new HashMap<>(Map.ofEntries(
                                entry("1", new BuildingFloor("1", BuildingName.LOYOLA_CENTRAL_BUILDING, "none"))
                        )),
                        45.45863,
                        -73.64066,
                        R.drawable.loy_central,
                        "7141 Sherbrooke St W, Montreal, Quebec H4B 2A7",
                        BuildingName.LOYOLA_CENTRAL_BUILDING
                )},
                {
                    BuildingName.VANIER_LIBRARY.getResourceName(), new Building(
                        "Vanier library building",
                        "The library is named after Major-General the Right Honourable Georges Philias Vanier, distinguished lawyer, soldier, diplomat, and Governor-General of Canada 1959-67. Vanier was a Loyola graduate (1906) and the recipient of the first Loyola Medal in 1963. In 1966 a 4.1-metre plaster replica of Michelangelo’s David was installed in the Vanier Library, a gift of Simpson's Department Store. It became a Loyola landmark and was the object of a number of student hi-jinks over the years, including painting it emerald green for St. Patrick‘s Day in 1967, and adorning it with fig leaves, hats, banana peels, and diapers.",
                        CampusName.LOYOLA,
                        new HashMap<>(Map.ofEntries(
                                entry("1", new BuildingFloor("1", BuildingName.VANIER_LIBRARY, "none")),
                                entry("2", new BuildingFloor("2", BuildingName.VANIER_LIBRARY, "none")),
                                entry("3", new BuildingFloor("3", BuildingName.VANIER_LIBRARY, "none"))
                        )),
                        45.45891,
                        -73.63888,
                        R.drawable.vanier_library,
                        "7141 Sherbrooke St W, Montreal, Quebec H4B 1R6",
                        BuildingName.VANIER_LIBRARY
                )},
                {
                    BuildingName.VANIER_EXTENSION.getResourceName(), new Building(
                        "Vanier Extension Building",
                        "The original Vanier Library Building (1964) has been called the Vanier Extension since the new Vanier Library opened in 1989. During the spring and summer of 2005 the second and third floors (3,257 sq. metres) of the older building were renovated and refitted to accommodate the specific needs of the Department of Applied Human Sciences. The new facilities include digitally equipped multi-functional classrooms that can be set up for traditional teaching, workshops, or seminars.",
                        CampusName.LOYOLA,
                        new HashMap<>(Map.ofEntries(
                                entry("1", new BuildingFloor("1", BuildingName.VANIER_EXTENSION, "none")),
                                entry("2", new BuildingFloor("2", BuildingName.VANIER_EXTENSION, "none")),
                                entry("3", new BuildingFloor("3", BuildingName.VANIER_EXTENSION, "none"))
                        )),
                        45.45891,
                        -73.63888,
                        R.drawable.vanier_library,
                        "7141 Rue Sherbrooke O, Montréal, QC H4B 1R6",
                        BuildingName.VANIER_EXTENSION
                )}
        };
    }
}
