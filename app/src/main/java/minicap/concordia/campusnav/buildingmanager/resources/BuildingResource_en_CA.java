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
import minicap.concordia.campusnav.map.MapCoordinates;

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
                        new MapCoordinates(45.49701, -73.57877, "Sir George William campus"),
                        new ArrayList<BuildingName>(Arrays.asList(
                                BuildingName.CI_ANNEX,
                                BuildingName.GREY_NUNS_ANNEX,
                                BuildingName.HALL,
                                BuildingName.LD_BUILDING,
                                BuildingName.MI_ANNEX,
                                BuildingName.P_ANNEX,
                                BuildingName.FAUBOURG_BUILDING,
                                BuildingName.LEARNING_SQUARE,
                                BuildingName.GUY_DE_MAISONNEUVE_BUILDING,
                                BuildingName.Q_ANNEX,
                                BuildingName.TORONTO_DOMINION_BUILDING,
                                BuildingName.PR_ANNEX,
                                BuildingName.RR_ANNEX,
                                BuildingName.FA_ANNEX,
                                BuildingName.M_ANNEX,
                                BuildingName.MOLSON_SCHOOL_OF_BUSINESS,
                                BuildingName.ER_BUILDING,
                                BuildingName.K_ANNEX,
                                BuildingName.T_ANNEX,
                                BuildingName.CL_ANNEX,
                                BuildingName.R_ANNEX,
                                BuildingName.VISUAL_ARTS_BUILDING,
                                BuildingName.X_ANNEX,
                                BuildingName.B_ANNEX,
                                BuildingName.SAMUEL_BRONFMAN_BUILDING,
                                BuildingName.GS_BUILDING,
                                BuildingName.FAUBOURG_STE_CATHERINE_BUILDING,
                                BuildingName.GREY_NUNS_BUILDING,
                                BuildingName.MU_ANNEX,
                                BuildingName.Z_ANNEX,
                                BuildingName.EN_ANNEX,
                                BuildingName.V_ANNEX,
                                BuildingName.ENGINEERING_COMPUTER_SCIENCE_AND_VISUAL_ARTS_INTEGRATED_COMPLEX,
                                BuildingName.S_ANNEX,
                                BuildingName.D_ANNEX,
                                BuildingName.JW_MCCONNELL_BUILDING
                        )))
                },
                {
                    CampusName.LOYOLA.getResourceName(), new Campus(
                        new MapCoordinates(45.45863, -73.64188, "Loyola campus"),
                        new ArrayList<BuildingName>(Arrays.asList(
                                BuildingName.LOYOLA_CENTRAL_BUILDING,
                                BuildingName.ST_IGNATIUS_OF_LOYOLA_CHURCH,
                                BuildingName.CENTRE_FOR_STRUCTURAL_AND_FUNCTIONAL_GENOMICS,
                                BuildingName.PERFORM_CENTRE,
                                BuildingName.HINGSTON_HALL_WING_HC,
                                BuildingName.LOYOLA_JESUIT_HALL_AND_CONFERENCE_CENTRE,
                                BuildingName.RECREATION_AND_ATHLETICS_COMPLEX,
                                BuildingName.QUADRANGLE,
                                BuildingName.TERREBONNE_BUILDING,
                                BuildingName.HINGSTON_HALL_WING_HA,
                                BuildingName.APPLIED_SCIENCE_HUB,
                                BuildingName.OSCAR_PETERSON_CONCERT_HALL,
                                BuildingName.STUDENT_CENTRE,
                                BuildingName.BB_ANNEX,
                                BuildingName.HINGSTON_HALL_WING_HB,
                                BuildingName.RICHARD_J_RENAUD_SCIENCE_COMPLEX,
                                BuildingName.PHYSICAL_SERVICES_BUILDING,
                                BuildingName.SOLAR_HOUSE,
                                BuildingName.VANIER_EXTENSION,
                                BuildingName.VANIER_LIBRARY,
                                BuildingName.JESUIT_RESIDENCE,
                                BuildingName.STINGER_DOME,
                                BuildingName.ADMINISTRATION_BUILDING,
                                BuildingName.F_C_SMITH_BUILDING,
                                BuildingName.BH_ANNEX,
                                BuildingName.COMMUNICATION_STUDIES_AND_JOURNALISM_BUILDING,
                                BuildingName.PSYCHOLOGY_BUILDING
                        )))
                },
                {
                    BuildingName.HALL.getResourceName(), new Building(
                        new MapCoordinates(45.49701, -73.57877, "Hall building"),
                        "The Henry F. Hall Building is a high-density hub, located on De Maisonneuve Boulevard, on Concordia’s downtown Sir-George-Williams Campus.\nThe cube-like structure was completed in 1966. Its exterior is made of pre-fabricated, stressed concrete, a feature of the brutalist movement, often associated with French architect Le Corbusier.",
                        CampusName.SGW,
                        new HashMap<String, BuildingFloor>(Map.ofEntries(
                                entry("1", new BuildingFloor("1", BuildingName.HALL, "m_c456d3c72998c98c")),
                                entry("2", new BuildingFloor("2", BuildingName.HALL, "m_a64b0271f6702bf1")),
                                entry("3", new BuildingFloor("3", BuildingName.HALL, BuildingFloor.NO_FLOOR_ID)),
                                entry("4", new BuildingFloor("4", BuildingName.HALL, BuildingFloor.NO_FLOOR_ID)),
                                entry("5", new BuildingFloor("5", BuildingName.HALL, BuildingFloor.NO_FLOOR_ID)),
                                entry("6", new BuildingFloor("6", BuildingName.HALL, BuildingFloor.NO_FLOOR_ID)),
                                entry("7", new BuildingFloor("7", BuildingName.HALL, BuildingFloor.NO_FLOOR_ID)),
                                entry("8", new BuildingFloor("8", BuildingName.HALL, "m_60db7dff71a44370")),
                                entry("9", new BuildingFloor("9", BuildingName.HALL, "m_20a3f55cfa5df04e")),
                                entry("10", new BuildingFloor("10", BuildingName.HALL, BuildingFloor.NO_FLOOR_ID)),
                                entry("11", new BuildingFloor("11", BuildingName.HALL, BuildingFloor.NO_FLOOR_ID))
                        )),
                        R.drawable.sgw_h,
                        "1455 De Maisonneuve Blvd W, Montreal, QC H3G 1M8",
                        BuildingName.HALL,
                        "67df02d0aa7c59000baf8d83"
                )},
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName(), new Building(
                        new MapCoordinates(45.495323, -73.579229, "John Molson School of Business"),
                        "In 2009 the John Molson Building officially opened on the corner of Guy and de Maisonneuve. It includes digitally equipped teaching amphitheatres and classrooms, faculty and graduate student offices, the Office of the Dean, student and faculty social space, as well as space for privatized programs. Special features also include case study rooms designed for group work, and laboratories for consumer behaviour research.",
                        CampusName.SGW,
                        new HashMap<String, BuildingFloor>(Map.ofEntries(
                                entry("S2", new BuildingFloor("S2", BuildingName.MOLSON_SCHOOL_OF_BUSINESS, "m_332633928c7c3640")),
                                entry("S1", new BuildingFloor("S1", BuildingName.MOLSON_SCHOOL_OF_BUSINESS, BuildingFloor.NO_FLOOR_ID)),
                                entry("1", new BuildingFloor("1", BuildingName.MOLSON_SCHOOL_OF_BUSINESS, "m_a7b5321e525dbec0")),
                                entry("2", new BuildingFloor("2", BuildingName.MOLSON_SCHOOL_OF_BUSINESS, BuildingFloor.NO_FLOOR_ID)),
                                entry("3", new BuildingFloor("3", BuildingName.MOLSON_SCHOOL_OF_BUSINESS, BuildingFloor.NO_FLOOR_ID)),
                                entry("4", new BuildingFloor("4", BuildingName.MOLSON_SCHOOL_OF_BUSINESS, BuildingFloor.NO_FLOOR_ID)),
                                entry("5", new BuildingFloor("5", BuildingName.MOLSON_SCHOOL_OF_BUSINESS, BuildingFloor.NO_FLOOR_ID)),
                                entry("6", new BuildingFloor("6", BuildingName.MOLSON_SCHOOL_OF_BUSINESS, BuildingFloor.NO_FLOOR_ID)),
                                entry("7", new BuildingFloor("7", BuildingName.MOLSON_SCHOOL_OF_BUSINESS, BuildingFloor.NO_FLOOR_ID)),
                                entry("8", new BuildingFloor("8", BuildingName.MOLSON_SCHOOL_OF_BUSINESS, BuildingFloor.NO_FLOOR_ID)),
                                entry("9", new BuildingFloor("9", BuildingName.MOLSON_SCHOOL_OF_BUSINESS, BuildingFloor.NO_FLOOR_ID)),
                                entry("10", new BuildingFloor("10", BuildingName.MOLSON_SCHOOL_OF_BUSINESS, BuildingFloor.NO_FLOOR_ID)),
                                entry("11", new BuildingFloor("11", BuildingName.MOLSON_SCHOOL_OF_BUSINESS, BuildingFloor.NO_FLOOR_ID)),
                                entry("12", new BuildingFloor("12", BuildingName.MOLSON_SCHOOL_OF_BUSINESS, BuildingFloor.NO_FLOOR_ID)),
                                entry("13", new BuildingFloor("13", BuildingName.MOLSON_SCHOOL_OF_BUSINESS, BuildingFloor.NO_FLOOR_ID)),
                                entry("14", new BuildingFloor("14", BuildingName.MOLSON_SCHOOL_OF_BUSINESS, BuildingFloor.NO_FLOOR_ID)),
                                entry("15", new BuildingFloor("15", BuildingName.MOLSON_SCHOOL_OF_BUSINESS, BuildingFloor.NO_FLOOR_ID))
                        )),
                        R.drawable.sgw_mb,
                        "1450 Guy Street Montreal, QC H3H 0A1",
                        BuildingName.MOLSON_SCHOOL_OF_BUSINESS,
                        "67f2ae593060f8000b749661"
                )},
                {
                    BuildingName.LOYOLA_CENTRAL_BUILDING.getResourceName(), new Building(
                        new MapCoordinates(45.45863, -73.64066, "Loyola Central building"),
                        "The first storey of the Central Building was occupied in 1945, but the completed building was officially opened in 1947, and it held classrooms, the library on nearly all of the top floor, a gymnasium, and student bedrooms at the south end of the second floor. The Guadagni Lounge in the former library space is named for Franco Guadagni, Loyola professor from 1942 until his death in 1964. He was the only Engineering faculty member from 1942 until 1959, and also taught all the Chemistry courses between 1942 and 1953.",
                        CampusName.LOYOLA,
                        new HashMap<>(Map.ofEntries(
                                entry("1", new BuildingFloor("1", BuildingName.LOYOLA_CENTRAL_BUILDING, BuildingFloor.NO_FLOOR_ID))
                        )),
                        R.drawable.loyola_cc,
                        "7141 Sherbrooke St W, Montreal, Quebec H4B 2A7",
                        BuildingName.LOYOLA_CENTRAL_BUILDING,
                        "67f2d62b73bd2d000bfb169e"
                )},
                {
                    BuildingName.VANIER_LIBRARY.getResourceName(), new Building(
                        new MapCoordinates(45.45891, -73.63888, "Vanier library building"),
                        "The library is named after Major-General the Right Honourable Georges Philias Vanier, distinguished lawyer, soldier, diplomat, and Governor-General of Canada 1959-67. Vanier was a Loyola graduate (1906) and the recipient of the first Loyola Medal in 1963. In 1966 a 4.1-metre plaster replica of Michelangelo’s David was installed in the Vanier Library, a gift of Simpson's Department Store. It became a Loyola landmark and was the object of a number of student hi-jinks over the years, including painting it emerald green for St. Patrick‘s Day in 1967, and adorning it with fig leaves, hats, banana peels, and diapers.",
                        CampusName.LOYOLA,
                        new HashMap<>(Map.ofEntries(
                                entry("1", new BuildingFloor("1", BuildingName.VANIER_LIBRARY, "m_4ab988a52765f684")),
                                entry("2", new BuildingFloor("2", BuildingName.VANIER_LIBRARY, "m_3118f32d8d3dacac")),
                                entry("3", new BuildingFloor("3", BuildingName.VANIER_LIBRARY, BuildingFloor.NO_FLOOR_ID))
                        )),
                        R.drawable.loyola_vl,
                        "7141 Sherbrooke St W, Montreal, Quebec H4B 1R6",
                        BuildingName.VANIER_LIBRARY,
                        "67df46cc50cb29000b302af3"
                )},
                {
                    BuildingName.VANIER_EXTENSION.getResourceName(), new Building(
                        new MapCoordinates(45.45891, -73.63888, "Vanier Extension Building"),
                        "The original Vanier Library Building (1964) has been called the Vanier Extension since the new Vanier Library opened in 1989. During the spring and summer of 2005 the second and third floors (3,257 sq. metres) of the older building were renovated and refitted to accommodate the specific needs of the Department of Applied Human Sciences. The new facilities include digitally equipped multi-functional classrooms that can be set up for traditional teaching, workshops, or seminars.",
                        CampusName.LOYOLA,
                        new HashMap<>(Map.ofEntries(
                                entry("1", new BuildingFloor("1", BuildingName.VANIER_EXTENSION, BuildingFloor.NO_FLOOR_ID)),
                                entry("2", new BuildingFloor("2", BuildingName.VANIER_EXTENSION, BuildingFloor.NO_FLOOR_ID)),
                                entry("3", new BuildingFloor("3", BuildingName.VANIER_EXTENSION, BuildingFloor.NO_FLOOR_ID))
                        )),
                        R.drawable.loyola_ve,
                        "7141 Rue Sherbrooke O, Montréal, QC H4B 1R6",
                        BuildingName.VANIER_EXTENSION,
                        "67df46cc50cb29000b302af3"
                )},
                {
                    BuildingName.B_ANNEX.getResourceName(), new Building(
                        new MapCoordinates(45.49788f, -73.579453f, "B Annex"),
                        "The B Annex at Concordia University's Sir George Williams Campus",
                        CampusName.SGW,
                        new HashMap<>(),
                        R.drawable.sgw_b,
                        "2160 Bishop",
                        BuildingName.B_ANNEX,
                        null
                )},
                {
                    BuildingName.CI_ANNEX.getResourceName(), new Building(
                        new MapCoordinates(45.4974481f, -73.5798674f, "CI Annex"),
                        "The CI Annex at Concordia University's Sir George Williams Campus",
                        CampusName.SGW,
                        new HashMap<>(),
                        R.drawable.sgw_ci,
                        "2149 Mackay",
                        BuildingName.CI_ANNEX,
                        null
                )},
                {
                    BuildingName.CL_ANNEX.getResourceName(), new Building(
                        new MapCoordinates(45.4942421f, -73.5790083f, "CL Annex"),
                        "The CL Annex at Concordia University's Sir George Williams Campus",
                        CampusName.SGW,
                        new HashMap<>(),
                        R.drawable.sgw_cl,
                        "1665 Ste-Catherine St. W.",
                        BuildingName.CL_ANNEX,
                        null
                )},
                {
                    BuildingName.D_ANNEX.getResourceName(), new Building(
                        new MapCoordinates(45.4977463f, -73.5794727f, "D Annex"),
                        "The D Annex at Concordia University's Sir George Williams Campus",
                        CampusName.SGW,
                        new HashMap<>(),
                        R.drawable.sgw_d,
                        "2140 Bishop",
                        BuildingName.D_ANNEX,
                        null
                )},
                {
                    BuildingName.EN_ANNEX.getResourceName(), new Building(
                        new MapCoordinates(45.4969207f, -73.5797469f, "EN Annex"),
                        "The EN Annex at Concordia University's Sir George Williams Campus",
                        CampusName.SGW,
                        new HashMap<>(),
                        R.drawable.sgw_en,
                        "2070 Mackay",
                        BuildingName.EN_ANNEX,
                        null
                )},
                {
                    BuildingName.ER_BUILDING.getResourceName(), new Building(
                        new MapCoordinates(45.4961942f, -73.5801319f, "ER Building"),
                        "The ER Building at Concordia University's Sir George Williams Campus",
                        CampusName.SGW,
                        new HashMap<>(),
                        R.drawable.sgw_er,
                        "2155 Guy St.",
                        BuildingName.ER_BUILDING,
                        null
                )},
                {
                    BuildingName.ENGINEERING_COMPUTER_SCIENCE_AND_VISUAL_ARTS_INTEGRATED_COMPLEX.getResourceName(), new Building(
                        new MapCoordinates(45.4956206f, -73.5782531f, "EV Building"),
                        "The Engineering, Computer Science and Visual Arts Integrated Complex at Concordia University's Sir George Williams Campus",
                        CampusName.SGW,
                        new HashMap<>(),
                        R.drawable.sgw_ev,
                        "1515 St. Catherine W.",
                        BuildingName.ENGINEERING_COMPUTER_SCIENCE_AND_VISUAL_ARTS_INTEGRATED_COMPLEX,
                        null
                )},
                {
                    BuildingName.FA_ANNEX.getResourceName(), new Building(
                        new MapCoordinates(45.4968309f, -73.5794838f, "FA Annex"),
                        "The FA Annex at Concordia University's Sir George Williams Campus",
                        CampusName.SGW,
                        new HashMap<>(),
                        R.drawable.sgw_fa,
                        "2060 Mackay",
                        BuildingName.FA_ANNEX,
                        null
                )},
                {
                    BuildingName.FAUBOURG_BUILDING.getResourceName(), new Building(
                        new MapCoordinates(45.494724f, -73.5779688f, "Faubourg"),
                        "The Faubourg Building at Concordia University's Sir George Williams Campus",
                        CampusName.SGW,
                        new HashMap<>(),
                        R.drawable.sgw_fb,
                        "1250 Guy",
                        BuildingName.FAUBOURG_BUILDING,
                        null
                )},
                {
                    BuildingName.FAUBOURG_STE_CATHERINE_BUILDING.getResourceName(), new Building(
                        new MapCoordinates(45.4943435f, -73.5784543f, "Faubourg St. Cat"),
                        "The Faubourg Ste-Catherine Building at Concordia University's Sir George Williams Campus",
                        CampusName.SGW,
                        new HashMap<>(),
                        R.drawable.sgw_fg,
                        "1610 St. Catherine W.",
                        BuildingName.FAUBOURG_STE_CATHERINE_BUILDING,
                        null
                )},
                {
                    BuildingName.GREY_NUNS_ANNEX.getResourceName(), new Building(
                        new MapCoordinates(45.494194f, -73.5780449f, "Grey Nuns Annex"),
                        "The Grey Nuns Annex at Concordia University's Sir George Williams Campus",
                        CampusName.SGW,
                        new HashMap<>(),
                        R.drawable.sgw_ga,
                        "1211-1215 St-Mathieu St.",
                        BuildingName.GREY_NUNS_ANNEX,
                        null
                )},
                {
                    BuildingName.GUY_DE_MAISONNEUVE_BUILDING.getResourceName(), new Building(
                        new MapCoordinates(45.4956472f, -73.5787748f, "Guy-De Maisonneuve"),
                        "The Guy-De Maisonneuve Building at Concordia University's Sir George Williams Campus",
                        CampusName.SGW,
                        new HashMap<>(),
                        R.drawable.sgw_gm,
                        "1550 De Maisonneuve W.",
                        BuildingName.GUY_DE_MAISONNEUVE_BUILDING,
                        null
                )},
                {
                    BuildingName.GREY_NUNS_BUILDING.getResourceName(), new Building(
                        new MapCoordinates(45.4935904f, -73.5770025f, "Grey Nuns"),
                        "The Grey Nuns Building at Concordia University's Sir George Williams Campus",
                        CampusName.SGW,
                        new HashMap<>(),
                        R.drawable.sgw_gn,
                        "1190 Guy St. (main entrance) | 1175 St-Mathieu St. | 1185 St-Mathieu St.",
                        BuildingName.GREY_NUNS_BUILDING,
                        null
                )},
                {
                    BuildingName.GS_BUILDING.getResourceName(), new Building(
                        new MapCoordinates(45.4966331f, -73.5813884f, "GS Building"),
                        "The GS Building at Concordia University's Sir George Williams Campus",
                        CampusName.SGW,
                        new HashMap<>(),
                        R.drawable.sgw_gs,
                        "1538 Sherbrooke St. W.",
                        BuildingName.GS_BUILDING,
                        null
                )},
                {
                    BuildingName.K_ANNEX.getResourceName(), new Building(
                        new MapCoordinates(45.4977587f, -73.5795318f, "K Annex"),
                        "The K Annex at Concordia University's Sir George Williams Campus",
                        CampusName.SGW,
                        new HashMap<>(),
                        R.drawable.sgw_k,
                        "2150 Bishop",
                        BuildingName.K_ANNEX,
                        null
                )},
                {
                    BuildingName.JW_MCCONNELL_BUILDING.getResourceName(), new Building(
                        new MapCoordinates(45.4968739f, -73.5780374f, "J.W. McConnell"),
                        "The J.W. McConnell Building at Concordia University's Sir George Williams Campus",
                        CampusName.SGW,
                        new HashMap<>(),
                        R.drawable.sgw_lb,
                        "1400 De Maisonneuve Blvd. W.",
                        BuildingName.JW_MCCONNELL_BUILDING,
                        null
                )},
                {
                    BuildingName.LD_BUILDING.getResourceName(), new Building(
                        new MapCoordinates(45.4966907f, -73.5772787f, "LD Building"),
                        "The LD Building at Concordia University's Sir George Williams Campus",
                        CampusName.SGW,
                        new HashMap<>(),
                        R.drawable.sgw_ld,
                        "1424 Bishop Street",
                        BuildingName.LD_BUILDING,
                        null
                )},
                {
                    BuildingName.LEARNING_SQUARE.getResourceName(), new Building(
                        new MapCoordinates(45.4963506f, -73.579479f, "Learning Square"),
                        "The Learning Square (LS Building) at Concordia University's Sir George Williams Campus",
                        CampusName.SGW,
                        new HashMap<>(),
                        R.drawable.sgw_ls,
                        "1535 De Maisonneuve Blvd. W.",
                        BuildingName.LEARNING_SQUARE,
                        null
                )},
                {
                    BuildingName.M_ANNEX.getResourceName(), new Building(
                        new MapCoordinates(45.4973576f, -73.5797744f, "M Annex"),
                        "The M Annex at Concordia University's Sir George Williams Campus",
                        CampusName.SGW,
                        new HashMap<>(),
                        R.drawable.sgw_m,
                        "2135 Mackay",
                        BuildingName.M_ANNEX,
                        null
                )},
                {
                    BuildingName.MI_ANNEX.getResourceName(), new Building(
                        new MapCoordinates(45.4977781f, -73.5793243f, "MI Annex"),
                        "The MI Annex at Concordia University's Sir George Williams Campus",
                        CampusName.SGW,
                        new HashMap<>(),
                        R.drawable.sgw_mi,
                        "2130 Bishop",
                        BuildingName.MI_ANNEX,
                        null
                )},
                {
                    BuildingName.MU_ANNEX.getResourceName(), new Building(
                        new MapCoordinates(45.4979418f, -73.5795013f, "MU Annex"),
                        "The MU Annex at Concordia University's Sir George Williams Campus",
                        CampusName.SGW,
                        new HashMap<>(),
                        R.drawable.sgw_mu,
                        "2170 Bishop",
                        BuildingName.MU_ANNEX,
                        null
                )},
                {
                    BuildingName.P_ANNEX.getResourceName(), new Building(
                        new MapCoordinates(45.496722f, -73.5793737f, "P Annex"),
                        "The P Annex at Concordia University's Sir George Williams Campus",
                        CampusName.SGW,
                        new HashMap<>(),
                        R.drawable.sgw_p,
                        "2020 Mackay",
                        BuildingName.P_ANNEX,
                        null
                )},
                {
                    BuildingName.PR_ANNEX.getResourceName(), new Building(
                        new MapCoordinates(45.4969908f, -73.5801137f, "PR Annex"),
                        "The PR Annex at Concordia University's Sir George Williams Campus",
                        CampusName.SGW,
                        new HashMap<>(),
                        R.drawable.sgw_pr,
                        "2100 Mackay",
                        BuildingName.PR_ANNEX,
                        null
                )},
                {
                    BuildingName.Q_ANNEX.getResourceName(), new Building(
                        new MapCoordinates(45.4966269f, -73.579095f, "Q Annex"),
                        "The Q Annex at Concordia University's Sir George Williams Campus",
                        CampusName.SGW,
                        new HashMap<>(),
                        R.drawable.sgw_q,
                        "2010 Mackay",
                        BuildingName.Q_ANNEX,
                        null
                )},
                {
                    BuildingName.R_ANNEX.getResourceName(), new Building(
                        new MapCoordinates(45.4968138f, -73.5796195f, "R Annex"),
                        "The R Annex at Concordia University's Sir George Williams Campus",
                        CampusName.SGW,
                        new HashMap<>(),
                        R.drawable.sgw_r,
                        "2050 Mackay",
                        BuildingName.R_ANNEX,
                        null
                )},
                {
                    BuildingName.RR_ANNEX.getResourceName(), new Building(
                        new MapCoordinates(45.4967529f, -73.5793334f, "RR Annex"),
                        "The RR Annex at Concordia University's Sir George Williams Campus",
                        CampusName.SGW,
                        new HashMap<>(),
                        R.drawable.sgw_rr,
                        "2040 Mackay",
                        BuildingName.RR_ANNEX,
                        null
                )},
                {
                    BuildingName.S_ANNEX.getResourceName(), new Building(
                        new MapCoordinates(45.4974066f, -73.5798502f, "S Annex"),
                        "The S Annex at Concordia University's Sir George Williams Campus",
                        CampusName.SGW,
                        new HashMap<>(),
                        R.drawable.sgw_s,
                        "2145 Mackay St.",
                        BuildingName.S_ANNEX,
                        null
                )},
                {
                    BuildingName.SAMUEL_BRONFMAN_BUILDING.getResourceName(), new Building(
                        new MapCoordinates(45.4965899f, -73.5860887f, "Bronfman Building"),
                        "The Samuel Bronfman Building at Concordia University's Sir George Williams Campus",
                        CampusName.SGW,
                        new HashMap<>(),
                        R.drawable.sgw_sb,
                        "1590 Docteur Penfield",
                        BuildingName.SAMUEL_BRONFMAN_BUILDING,
                        null
                )},
                {
                    BuildingName.T_ANNEX.getResourceName(), new Building(
                        new MapCoordinates(45.4967522f, -73.5793631f, "T Annex"),
                        "The T Annex at Concordia University's Sir George Williams Campus",
                        CampusName.SGW,
                        new HashMap<>(),
                        R.drawable.sgw_t,
                        "2030 Mackay",
                        BuildingName.T_ANNEX,
                        null
                )},
                {
                    BuildingName.TORONTO_DOMINION_BUILDING.getResourceName(), new Building(
                        new MapCoordinates(45.494685f, -73.5787306f, "TD Building"),
                        "The Toronto-Dominion Building at Concordia University's Sir George Williams Campus",
                        CampusName.SGW,
                        new HashMap<>(),
                        R.drawable.sgw_td,
                        "1410 Guy",
                        BuildingName.TORONTO_DOMINION_BUILDING,
                        null
                )},
                {
                    BuildingName.V_ANNEX.getResourceName(), new Building(
                        new MapCoordinates(45.4970181f, -73.5799145f, "V Annex"),
                        "The V Annex at Concordia University's Sir George Williams Campus",
                        CampusName.SGW,
                        new HashMap<>(),
                        R.drawable.sgw_v,
                        "2110 Mackay",
                        BuildingName.V_ANNEX,
                        null
                )},
                {
                    BuildingName.VISUAL_ARTS_BUILDING.getResourceName(), new Building(
                        new MapCoordinates(45.4957332f, -73.5739144f, "Visual Arts"),
                        "The Visual Arts Building at Concordia University's Sir George Williams Campus",
                        CampusName.SGW,
                        new HashMap<>(),
                        R.drawable.sgw_va,
                        "1395 René Lévesque W.",
                        BuildingName.VISUAL_ARTS_BUILDING,
                        null
                )},
                {
                    BuildingName.X_ANNEX.getResourceName(), new Building(
                        new MapCoordinates(45.4969048f, -73.5796825f, "X Annex"),
                        "The X Annex at Concordia University's Sir George Williams Campus",
                        CampusName.SGW,
                        new HashMap<>(),
                        R.drawable.sgw_x,
                        "2080 Mackay",
                        BuildingName.X_ANNEX,
                        null
                )},
                {
                    BuildingName.Z_ANNEX.getResourceName(), new Building(
                        new MapCoordinates(45.4970181f, -73.5799145f, "Z Annex"),
                        "The Z Annex at Concordia University's Sir George Williams Campus",
                        CampusName.SGW,
                        new HashMap<>(),
                        R.drawable.sgw_z,
                        "2090 Mackay",
                        BuildingName.Z_ANNEX,
                        null
                )},
                {
                    BuildingName.ADMINISTRATION_BUILDING.getResourceName(), new Building(
                        new MapCoordinates(45.4585101f, -73.6418116f, "Administration"),
                        "The Administration Building at Concordia University's Loyola Campus",
                        CampusName.LOYOLA,
                        new HashMap<>(),
                        R.drawable.loyola_ad,
                        "7141 Sherbrooke St. W.",
                        BuildingName.ADMINISTRATION_BUILDING,
                        null
                )},
                {
                    BuildingName.BB_ANNEX.getResourceName(), new Building(
                        new MapCoordinates(45.45976f, -73.6391699f, "BB Annex"),
                        "The BB Annex at Concordia University's Loyola Campus",
                        CampusName.LOYOLA,
                        new HashMap<>(),
                        R.drawable.loyola_bb,
                        "3502 Belmore",
                        BuildingName.BB_ANNEX,
                        null
                )},
                {
                    BuildingName.BH_ANNEX.getResourceName(), new Building(
                        new MapCoordinates(45.4597654f, -73.6391464f, "BH Annex"),
                        "The BH Annex at Concordia University's Loyola Campus",
                        CampusName.LOYOLA,
                        new HashMap<>(),
                        R.drawable.loyola_bh,
                        "3500 Belmore",
                        BuildingName.BH_ANNEX,
                        null
                )},
                {
                    BuildingName.COMMUNICATION_STUDIES_AND_JOURNALISM_BUILDING.getResourceName(), new Building(
                        new MapCoordinates(45.4584934f, -73.6406396f, "Comm & Journalism"),
                        "The Communication Studies and Journalism Building at Concordia University's Loyola Campus",
                        CampusName.LOYOLA,
                        new HashMap<>(),
                        R.drawable.loyola_cj,
                        "7141 Sherbrooke W.",
                        BuildingName.COMMUNICATION_STUDIES_AND_JOURNALISM_BUILDING,
                        null
                )},
                {
                    BuildingName.STINGER_DOME.getResourceName(), new Building(
                        new MapCoordinates(45.4584934f, -73.6406396f, "Stinger Dome"),
                        "The Stinger Dome (seasonal) at Concordia University's Loyola Campus",
                        CampusName.LOYOLA,
                        new HashMap<>(),
                        R.drawable.loyola_do,
                        "7141 Sherbrooke W.",
                        BuildingName.STINGER_DOME,
                        null
                )},
                {
                    BuildingName.F_C_SMITH_BUILDING.getResourceName(), new Building(
                        new MapCoordinates(45.4584934f, -73.6406396f, "F. C. Smith"),
                        "The F. C. Smith Building at Concordia University's Loyola Campus",
                        CampusName.LOYOLA,
                        new HashMap<>(),
                        R.drawable.loyola_fc,
                        "7141 Sherbrooke W.",
                        BuildingName.F_C_SMITH_BUILDING,
                        null
                )},
                {
                    BuildingName.CENTRE_FOR_STRUCTURAL_AND_FUNCTIONAL_GENOMICS.getResourceName(), new Building(
                        new MapCoordinates(45.4584934f, -73.6406396f, "Genomics"),
                        "The Centre for Structural and Functional Genomics at Concordia University's Loyola Campus",
                        CampusName.LOYOLA,
                        new HashMap<>(),
                        R.drawable.loyola_ge,
                        "7141 Sherbrooke W.",
                        BuildingName.CENTRE_FOR_STRUCTURAL_AND_FUNCTIONAL_GENOMICS,
                        null
                )},
                {
                    BuildingName.HINGSTON_HALL_WING_HA.getResourceName(), new Building(
                        new MapCoordinates(45.4584934f, -73.6406396f, "Hingston HA"),
                        "The Hingston Hall, wing HA at Concordia University's Loyola Campus",
                        CampusName.LOYOLA,
                        new HashMap<>(),
                        R.drawable.loyola_ha,
                        "7141 Sherbrooke W.",
                        BuildingName.HINGSTON_HALL_WING_HA,
                        null
                )},
                {
                    BuildingName.HINGSTON_HALL_WING_HB.getResourceName(), new Building(
                        new MapCoordinates(45.4584934f, -73.6406396f, "Hingston HB"),
                        "The Hingston Hall, wing HB at Concordia University's Loyola Campus",
                        CampusName.LOYOLA,
                        new HashMap<>(),
                        R.drawable.loyola_hb,
                        "7141 Sherbrooke W.",
                        BuildingName.HINGSTON_HALL_WING_HB,
                        null
                )},
                {
                    BuildingName.HINGSTON_HALL_WING_HC.getResourceName(), new Building(
                        new MapCoordinates(45.4584934f, -73.6406396f, "Hingston HC"),
                        "The Hingston Hall, wing HC at Concordia University's Loyola Campus",
                        CampusName.LOYOLA,
                        new HashMap<>(),
                        R.drawable.loyola_hc,
                        "7141 Sherbrooke W.",
                        BuildingName.HINGSTON_HALL_WING_HC,
                        null
                )},
                {
                    BuildingName.APPLIED_SCIENCE_HUB.getResourceName(), new Building(
                        new MapCoordinates(45.4584934f, -73.6406396f, "Applied Sci"),
                        "The Applied Science Hub at Concordia University's Loyola Campus",
                        CampusName.LOYOLA,
                        new HashMap<>(),
                        R.drawable.loyola_hu,
                        "7141 Sherbrooke W.",
                        BuildingName.APPLIED_SCIENCE_HUB,
                        null
                )},
                {
                    BuildingName.JESUIT_RESIDENCE.getResourceName(), new Building(
                        new MapCoordinates(45.4584934f, -73.6406396f, "Jesuit Res"),
                        "The Jesuit Residence at Concordia University's Loyola Campus",
                        CampusName.LOYOLA,
                        new HashMap<>(),
                        R.drawable.loyola_jr,
                        "7141 Sherbrooke W.",
                        BuildingName.JESUIT_RESIDENCE,
                        null
                )},
                {
                    BuildingName.PERFORM_CENTRE.getResourceName(), new Building(
                        new MapCoordinates(45.4570815f, -73.6376985f, "Perform Centre"),
                        "The PERFORM Centre at Concordia University's Loyola Campus",
                        CampusName.LOYOLA,
                        new HashMap<>(),
                        R.drawable.loyola_pc,
                        "7200 Sherbrooke St. W.",
                        BuildingName.PERFORM_CENTRE,
                        null
                )},
                {
                    BuildingName.PHYSICAL_SERVICES_BUILDING.getResourceName(), new Building(
                        new MapCoordinates(45.4584934f, -73.6406396f, "Physical Services"),
                        "The Physical Services Building at Concordia University's Loyola Campus",
                        CampusName.LOYOLA,
                        new HashMap<>(),
                        R.drawable.loyola_ps,
                        "7141 Sherbrooke W.",
                        BuildingName.PHYSICAL_SERVICES_BUILDING,
                        null
                )},
                {
                    BuildingName.OSCAR_PETERSON_CONCERT_HALL.getResourceName(), new Building(
                        new MapCoordinates(45.4584934f, -73.6406396f, "Oscar Peterson"),
                        "The Oscar Peterson Concert Hall at Concordia University's Loyola Campus",
                        CampusName.LOYOLA,
                        new HashMap<>(),
                        R.drawable.loyola_pt,
                        "7141 Sherbrooke W.",
                        BuildingName.OSCAR_PETERSON_CONCERT_HALL,
                        null
                )},
                {
                    BuildingName.PSYCHOLOGY_BUILDING.getResourceName(), new Building(
                        new MapCoordinates(45.4584934f, -73.6406396f, "Psychology"),
                        "The Psychology Building at Concordia University's Loyola Campus",
                        CampusName.LOYOLA,
                        new HashMap<>(),
                        R.drawable.loyola_py,
                        "7141 Sherbrooke W.",
                        BuildingName.PSYCHOLOGY_BUILDING,
                        null
                )},
                {
                    BuildingName.QUADRANGLE.getResourceName(), new Building(
                        new MapCoordinates(45.4584934f, -73.6406396f, "Quadrangle"),
                        "The Quadrangle at Concordia University's Loyola Campus",
                        CampusName.LOYOLA,
                        new HashMap<>(),
                        R.drawable.loyola_qa,
                        "7141 Sherbrooke W.",
                        BuildingName.QUADRANGLE,
                        null
                )},
                {
                    BuildingName.RECREATION_AND_ATHLETICS_COMPLEX.getResourceName(), new Building(
                        new MapCoordinates(45.4567231f, -73.637654f, "Rec & Athletics"),
                        "The Recreation and Athletics Complex at Concordia University's Loyola Campus",
                        CampusName.LOYOLA,
                        new HashMap<>(),
                        R.drawable.loyola_ra,
                        "7200 Sherbrooke W.",
                        BuildingName.RECREATION_AND_ATHLETICS_COMPLEX,
                        null
                )},
                {
                    BuildingName.LOYOLA_JESUIT_HALL_AND_CONFERENCE_CENTRE.getResourceName(), new Building(
                        new MapCoordinates(45.4584934f, -73.6406396f, "Jesuit Hall"),
                        "The Loyola Jesuit Hall and Conference Centre at Concordia University's Loyola Campus",
                        CampusName.LOYOLA,
                        new HashMap<>(),
                        R.drawable.loyola_rf,
                        "7141 Sherbrooke W.",
                        BuildingName.LOYOLA_JESUIT_HALL_AND_CONFERENCE_CENTRE,
                        null
                )},
                {
                    BuildingName.STUDENT_CENTRE.getResourceName(), new Building(
                        new MapCoordinates(45.4584934f, -73.6406396f, "Student Centre"),
                        "The Student Centre at Concordia University's Loyola Campus",
                        CampusName.LOYOLA,
                        new HashMap<>(),
                        R.drawable.loyola_sc,
                        "7141 Sherbrooke W.",
                        BuildingName.STUDENT_CENTRE,
                        null
                )},
                {
                    BuildingName.SOLAR_HOUSE.getResourceName(), new Building(
                        new MapCoordinates(45.4584934f, -73.6406396f, "Solar House"),
                        "The Solar House at Concordia University's Loyola Campus",
                        CampusName.LOYOLA,
                        new HashMap<>(),
                        R.drawable.loyola_sh,
                        "7141 Sherbrooke W.",
                        BuildingName.SOLAR_HOUSE,
                        null
                )},
                {
                    BuildingName.ST_IGNATIUS_OF_LOYOLA_CHURCH.getResourceName(), new Building(
                        new MapCoordinates(45.457659f, -73.64252f, "St. Ignatius"),
                        "The St. Ignatius of Loyola Church at Concordia University's Loyola Campus",
                        CampusName.LOYOLA,
                        new HashMap<>(),
                        R.drawable.loyola_si,
                        "4455 Broadway",
                        BuildingName.ST_IGNATIUS_OF_LOYOLA_CHURCH,
                        null
                )
                },
                {BuildingName.RICHARD_J_RENAUD_SCIENCE_COMPLEX.getResourceName(), new Building(
                        new MapCoordinates(45.4584934f, -73.6406396f, "Renaud Science"),
                        "The Richard J. Renaud Science Complex at Concordia University's Loyola Campus",
                        CampusName.LOYOLA,
                        new HashMap<>(),
                        R.drawable.loyola_sp,
                        "7141 Sherbrooke W.",
                        BuildingName.RICHARD_J_RENAUD_SCIENCE_COMPLEX,
                        null
                )},
                {
                    BuildingName.TERREBONNE_BUILDING.getResourceName(), new Building(
                        new MapCoordinates(45.4600369f, -73.6408641f, "Terrebonne"),
                        "The Terrebonne Building at Concordia University's Loyola Campus",
                        CampusName.LOYOLA,
                        new HashMap<>(),
                        R.drawable.loyola_ta,
                        "7079 Terrebonne",
                        BuildingName.TERREBONNE_BUILDING,
                        null
                )
                },

        };
    }
}
