package minicap.concordia.campusnav.buildingmanager.resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListResourceBundle;

import minicap.concordia.campusnav.buildingmanager.entities.poi.IndoorPOI;
import minicap.concordia.campusnav.buildingmanager.enumerations.BuildingName;
import minicap.concordia.campusnav.buildingmanager.enumerations.POIType;
import minicap.concordia.campusnav.map.MapCoordinates;

/**
 * Notes for maintainability:
 *  The naming convention follows: <buildingResourceName>_FloorX
 *  I recommend using the BuildingName's getResourceName for the buildingResourceName
 */
public class IndoorPOIResource_en_CA extends ListResourceBundle {
        public static final String FLOOR_TAG = "_Floor";

    @Override
    protected Object[][] getContents() {
        return new Object[][]{
                {
                    BuildingName.HALL.getResourceName() + FLOOR_TAG + "1", new ArrayList<IndoorPOI>(Arrays.asList(
                        new IndoorPOI(new MapCoordinates(45.49724976, -73.57868389, "H196"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49704222, -73.57864411, "H103"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49714376, -73.57896729, "H110"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49707464, -73.57871833, "H102"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49721619, -73.57863867, "H196-2"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49710540, -73.57859145, "H102"), POIType.STAIRS, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49706757, -73.57861793, "H1 Accessible entrance 1"), POIType.CLASS_ROOM, BuildingName.HALL, "1", true),
                        new IndoorPOI(new MapCoordinates(45.49723764, -73.57869441, "H196-2"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49728026, -73.57860920, "H102-3"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49726193, -73.57867019, "H1 Elevator 1"), POIType.ELEVATOR, BuildingName.HALL, "1", true),
                        new IndoorPOI(new MapCoordinates(45.49727883, -73.57865142, "H1 Elevator 2"), POIType.ELEVATOR, BuildingName.HALL, "1", true),
                        new IndoorPOI(new MapCoordinates(45.49736189, -73.57843632, "H101"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49728355, -73.57860474, "H102-3"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49716298, -73.57880597, "H110-5"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49713155, -73.57881011, "H110-9"), POIType.STAIRS, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49705284, -73.57888821, "H110-8"), POIType.STAIRS, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49708153, -73.57883164, "H110-3"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49712433, -73.57878194, "H110-4"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49704018, -73.57886066, "H110-2"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49703349, -73.57900590, "H109"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49720661, -73.57882610, "H1 Men's washroom"), POIType.WASHROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49725589, -73.57889816, "H1 Women's washroom"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49705630, -73.57907820, "H109-1"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49707867, -73.57909740, "H190"), POIType.STAIRS, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49710393, -73.57918748, "H125"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49726267, -73.57875879, "H116-4"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49729717, -73.57883725, "H115"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49731209, -73.57875863, "H118-13"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49733545, -73.57872786, "H118-14"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49735508, -73.57871369, "H118-2"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49731020, -73.57871097, "H118-1"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49734868, -73.57867772, "H118"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49737560, -73.57869381, "H118-3"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49741135, -73.57865933, "H118-4"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49738661, -73.57864119, "H118-5"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49743385, -73.57860182, "H198"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49747083, -73.57867178, "H157"), POIType.STAIRS, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49741163, -73.57879104, "H150"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49738719, -73.57888822, "H150-1"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49749859, -73.57872772, "H155"), POIType.STAIRS, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49749926, -73.57878358, "H153"), POIType.STAIRS , BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49752388, -73.57878729, "H153-1"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49756242, -73.57880645, "H151"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49755325, -73.57886205, "H175"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49737836, -73.57898117, "H126"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49735273, -73.57901021, "H124"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49732751, -73.57896151, "H120"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49734966, -73.57889174, "H128"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49733237, -73.57891196, "H128-1"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49729555, -73.57895753, "H120-7"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49723730, -73.57902971, "H120-2"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49756242, -73.57880645, "H151"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49719033, -73.57905242, "H120-1"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49718455, -73.57919600, "H127-1"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49710898, -73.57918346, "H125"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49715562, -73.57929739, "H127"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49722279, -73.57929846, "H129-90"), POIType.STAIRS, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49740538, -73.57920270, "H131"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49737014, -73.57923938, "H131-1"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49741043, -73.57927377, "H131-4"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49744550, -73.57922313, "H131-2"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49746238, -73.57920659, "H131-3"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49749318, -73.57916366, "H133"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49747508, -73.57911296, "H133-2"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49749447, -73.57910300, "H133-1"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49751722, -73.57909584, "H135"), POIType.WASHROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49753705, -73.57906985, "H135-1"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49757014, -73.57905153, "H137-1"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49755742, -73.57903519, "H137"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49759312, -73.57904713, "H139"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49763441, -73.57907463, "H139-1"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49760079, -73.57901394, "H139-2"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49763007, -73.57900512, "H141"), POIType.CLASS_ROOM, BuildingName.HALL, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49723435, -73.57848873, "H102-90"), POIType.STAIRS, BuildingName.HALL, "1", false)
                    ))
                },
                {
                    BuildingName.HALL.getResourceName() + FLOOR_TAG + "2", new ArrayList<IndoorPOI>(Arrays.asList(
                        new IndoorPOI(new MapCoordinates(45.49716762, -73.57915985, "H283"), POIType.STAIRS, BuildingName.HALL, "2", false),
                        new IndoorPOI(new MapCoordinates(45.49743246, -73.57864003, "H260"), POIType.CLASS_ROOM, BuildingName.HALL, "2", false),
                        new IndoorPOI(new MapCoordinates(45.49738456, -73.57887807, "H217"), POIType.CLASS_ROOM, BuildingName.HALL, "2", false),
                        new IndoorPOI(new MapCoordinates(45.49743199, -73.57884187, "H222.00"), POIType.CLASS_ROOM, BuildingName.HALL, "2", false),
                        new IndoorPOI(new MapCoordinates(45.49748324, -73.57879244, "H281"), POIType.CLASS_ROOM, BuildingName.HALL, "2", false),
                        new IndoorPOI(new MapCoordinates(45.49749911, -73.57878502, "H280"), POIType.CLASS_ROOM, BuildingName.HALL, "2", false),
                        new IndoorPOI(new MapCoordinates(45.49754338, -73.57882015, "H275"), POIType.STAIRS, BuildingName.HALL, "2", false),
                        new IndoorPOI(new MapCoordinates(45.49736590, -73.57905224, "The Hive Cafe"), POIType.COFFEE_SHOP, BuildingName.HALL, "2", false),
                        new IndoorPOI(new MapCoordinates(45.49724879, -73.57909718, "H2 Washroom"), POIType.WASHROOM, BuildingName.HALL, "2", false),
                        new IndoorPOI(new MapCoordinates(45.49707830, -73.57909913, "H290"), POIType.STAIRS, BuildingName.HALL, "2", false),
                        new IndoorPOI(new MapCoordinates(45.49707140, -73.57905579, "H290-1 "), POIType.CLASS_ROOM, BuildingName.HALL, "2", false),
                        new IndoorPOI(new MapCoordinates(45.49705299, -73.57901801, "H205"), POIType.CLASS_ROOM, BuildingName.HALL, "2", false),
                        new IndoorPOI(new MapCoordinates(45.49700853, -73.57897341, "H209 "), POIType.CLASS_ROOM, BuildingName.HALL, "2", false)
                    ))
                },
                {
                    BuildingName.HALL.getResourceName() + FLOOR_TAG + "3", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.HALL.getResourceName() + FLOOR_TAG + "4", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.HALL.getResourceName() + FLOOR_TAG + "5", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.HALL.getResourceName() + FLOOR_TAG + "6", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.HALL.getResourceName() + FLOOR_TAG + "7", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.HALL.getResourceName() + FLOOR_TAG + "8", new ArrayList<IndoorPOI>(Arrays.asList(
                        new IndoorPOI(new MapCoordinates(45.49719339, -73.57941104, "H831"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49715650, -73.57932899, "H899.15"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49712581, -73.57925960, "H829"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49705911, -73.57912406, "H825"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49702734, -73.57906538, "H823"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49699239, -73.57899197, "H821"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49696161, -73.57892152, "H819"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49691697, -73.57884006, "H817"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49697822, -73.57878106, "H815"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49702424, -73.57874028, "H813"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49707031, -73.57869262, "H811"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49716449, -73.57859481, "H807"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49722179, -73.57858931, "H805.01"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49721046, -73.57855770, "H805.02"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49719843, -73.57851761, "H805.03"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49725943, -73.57850332, "H803"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49731394, -73.57845576, "H801"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49736903, -73.57838425, "H867"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49739494, -73.57842758, "H865"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49741698, -73.57848263, "H863"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49744839, -73.57855452, "H861"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49747764, -73.57861874, "H859"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49751226, -73.57869112, "H857"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49754251, -73.57875751, "H855"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49757522, -73.57883517, "H853"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49757822, -73.57891036, "H851.01"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49761115, -73.57887489, "H851.02"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49763322, -73.57890042, "H851.03 "), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49764553, -73.57898364, "H849"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49758569, -73.57903823, "H847"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49753753, -73.57908956, "H845"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49748725, -73.57913311, "H843"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49743654, -73.57917358, "H841"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49734460, -73.57926239, "H837"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49729772, -73.57930485, "H835"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49725062, -73.57934892, "H833"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49724695, -73.57917337, "H885"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49727019, -73.57914970, "H832.02"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49728842, -73.57914071, "H832"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49731120, -73.57915194, "H832.01"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49730038, -73.57912693, "H832.03"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49728392, -73.57909013, "H832.05"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49724589, -73.57905899, "H832.06"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49719325, -73.57911404, "H822"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49717343, -73.57896821, "H820"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49709270, -73.57893609, "H886"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49710258, -73.57887432, "H888"), POIType.STAIRS, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49719339, -73.57941104, "H831"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49719339, -73.57941104, "H831"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49715707, -73.57882844, "H8 Men's washroom"), POIType.WASHROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49721046, -73.57872837, "H806.01"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49722478, -73.57875685, "H806.02"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49723912, -73.57878706, "H806.03"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49727476, -73.57875402, "H8 Small washroom"), POIType.WASHROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49724350, -73.57872097, "H8 Accessible washroom"), POIType.WASHROOM, BuildingName.HALL, "8", true),
                        new IndoorPOI(new MapCoordinates(45.49729350, -73.57865741, "H8 Women's washroom"), POIType.WASHROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49734946, -73.57865811, "H898"), POIType.STAIRS, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49734672, -73.57882961, "H862"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49737797, -73.57877966, "H860"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49739963, -73.57882784, "H860.06"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49743732, -73.57879602, "H860.05"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49742202, -73.57876012, "H860.03"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49740380, -73.57872729, "H860.01"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49738279, -73.57906131, "H838"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49735324, -73.57895035, "H840"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49738843, -73.57892672, "H842"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49743815, -73.57892319, "H877"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49744215, -73.57886053, "H854"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49746903, -73.57889591, "H852"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49746626, -73.57896777, "H875"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false),
                        new IndoorPOI(new MapCoordinates(45.49744649, -73.57902267, "H881"), POIType.CLASS_ROOM, BuildingName.HALL, "8", false)
                        ))
                },
                {
                    BuildingName.HALL.getResourceName() + FLOOR_TAG + "9", new ArrayList<IndoorPOI>(Arrays.asList(
                        new IndoorPOI(new MapCoordinates(45.49718794, -73.57940242, "H929"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49721772, -73.57933934, "H931"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49713915, -73.57929882, "H927"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49708950, -73.57927903, "H927-1"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49707290, -73.57924676, "H927-3"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49712269, -73.57921443, "H927-4"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49710149, -73.57917725, "H925-3"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49707529, -73.57913049, "H925"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49701267, -73.57909014, "H923"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49697431, -73.57899774, "H921"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49693610, -73.57892868, "H919"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49689424, -73.57885296, "H917"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49695611, -73.57877915, "H915"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49701299, -73.57872259, "H913"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49707156, -73.57867869, "H911"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49712050, -73.57862788, "H909"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49719501, -73.57855984, "H907"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49729147, -73.57846793, "H903"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49739815, -73.57840082, "H967"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49746903, -73.57850388, "H965-4"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49748739, -73.57853870, "H965-2"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49744423, -73.57857907, "H965-1"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49742795, -73.57853980, "H965-3"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49748871, -73.57862214, "H963"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49754342, -73.57866571, "H961-1"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49750270, -73.57870883, "H961-2"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49751548, -73.57874048, "H961-4"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49756426, -73.57872000, "H961-3"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49754373, -73.57878532, "H961-6"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49759425, -73.57876992, "H961-7"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49761015, -73.57880332, "H961-9"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49756520, -73.57884873, "H961-10"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49757774, -73.57887473, "H961-12"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49754591, -73.57888859, "H961-8"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49758398, -73.57893178, "H961-14"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49761272, -73.57898791, "H961-26"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49762644, -73.57884259, "H961-11"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49764440, -73.57887526, "H961-13"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49765607, -73.57890961, "H961-15"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49767251, -73.57894538, "H961-17"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49769384, -73.57898412, "H961-19"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49767134, -73.57901188, "H961-21"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49764707, -73.57903962, "H961-23"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49762364, -73.57906154, "H961-25"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49759732, -73.57908867, "H961-27"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49757569, -73.57910555, "H961-29"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49755197, -73.57913194, "H961-31"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49753113, -73.57915563, "H961-33"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49756782, -73.57902320, "H961-28"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49754506, -73.57904785, "H961-30"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49751286, -73.57907881, "H961-32"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49751494, -73.57902069, "H945"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49748466, -73.57897271, "H975"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49745853, -73.57902478, "H981"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49747521, -73.57883578, "H968"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49739393, -73.57891107, "H966"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49736629, -73.57896489, "H966-1"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49735229, -73.57893007, "H966-2 "), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49740438, -73.57877809, "H964"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49735212, -73.57882185, "H962"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49733440, -73.57889204, "H962-1"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49731049, -73.57883741, "H960"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49742882, -73.57908516, "H983"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49737811, -73.57916796, "H937"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49733521, -73.57909287, "H937-1"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49747649, -73.57918887, "H941"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49742026, -73.57924660, "H928-25"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49735123, -73.57927126, "H933-11"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49733442, -73.57931037, "H933-1"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49737584, -73.57930427, "H933-2"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49729201, -73.57935334, "H933"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49724059, -73.57928239, "H928"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49726783, -73.57926109, "H932"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49724982, -73.57918739, "H985"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49728013, -73.57923031, "H932-1"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49717851, -73.57896064, "H920"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49709844, -73.57899614, "H918"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49708779, -73.57884511, "H990"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49715325, -73.57881490, "H9 Men's washroom"), POIType.WASHROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49723969, -73.57891346, "H980"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49723980, -73.57876411, "H914"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49721982, -73.57872830, "H908"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49720908, -73.57869726, "H906"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49724633, -73.57870723, "H9 Accessible washrooms"), POIType.WASHROOM, BuildingName.HALL, "9", true),
                        new IndoorPOI(new MapCoordinates(45.49725572, -73.57866968, "H902-1"), POIType.CLASS_ROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49729826, -73.57865918, "H9 Women's washroom"), POIType.WASHROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49727921, -73.57874334, "H9 Washroom"), POIType.WASHROOM, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49731015, -73.57871325, "H9 Elevator 1"), POIType.ELEVATOR, BuildingName.HALL, "9", true),
                        new IndoorPOI(new MapCoordinates(45.49733442, -73.57868427, "H9 Elevator 2"), POIType.ELEVATOR, BuildingName.HALL, "9", false),
                        new IndoorPOI(new MapCoordinates(45.49733442, -73.57868427, "H9 Stairs"), POIType.STAIRS, BuildingName.HALL, "9", false)
                    ))
                },
                {
                    BuildingName.HALL.getResourceName() + FLOOR_TAG + "10", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.HALL.getResourceName() + FLOOR_TAG + "11", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + FLOOR_TAG + "S2", new ArrayList<IndoorPOI>(Arrays.asList(
                        new IndoorPOI(new MapCoordinates(45.49517953, -73.57886376, "JMSB S2.210"), POIType.CLASS_ROOM, BuildingName.MOLSON_SCHOOL_OF_BUSINESS, "S2", false),
                        new IndoorPOI(new MapCoordinates(45.49504150, -73.57869781, "JMSB S2.275"), POIType.CLASS_ROOM, BuildingName.MOLSON_SCHOOL_OF_BUSINESS, "S2", false)
                ))
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + FLOOR_TAG + "S1", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + FLOOR_TAG + "1", new ArrayList<IndoorPOI>(Arrays.asList(
                        new IndoorPOI(new MapCoordinates(45.49515228, -73.57888482, "JMSB 1.210"), POIType.CLASS_ROOM, BuildingName.MOLSON_SCHOOL_OF_BUSINESS, "1", false),
                        new IndoorPOI(new MapCoordinates(45.49519640, -73.57879643, "JMSB 1.218"), POIType.CLASS_ROOM, BuildingName.MOLSON_SCHOOL_OF_BUSINESS, "1", false)

                        ))
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + FLOOR_TAG + "2", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + FLOOR_TAG + "3", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + FLOOR_TAG + "4", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + FLOOR_TAG + "5", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + FLOOR_TAG + "6", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + FLOOR_TAG + "7", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + FLOOR_TAG + "8", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + FLOOR_TAG + "9", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + FLOOR_TAG + "10", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + FLOOR_TAG + "11", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + FLOOR_TAG + "12", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + FLOOR_TAG + "13", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + FLOOR_TAG + "14", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.MOLSON_SCHOOL_OF_BUSINESS.getResourceName() + FLOOR_TAG + "15", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.LOYOLA_CENTRAL_BUILDING.getResourceName() + FLOOR_TAG + "1", new ArrayList<>(Arrays.asList(
                        new IndoorPOI(new MapCoordinates(45.45829172, -73.64040142, "CC 116"), POIType.CLASS_ROOM, BuildingName.LOYOLA_CENTRAL_BUILDING, "1", false),
                        new IndoorPOI(new MapCoordinates(45.45820843, -73.64045542, "CC 115"), POIType.CLASS_ROOM, BuildingName.LOYOLA_CENTRAL_BUILDING, "1", false)
                ))
                },
                {
                    BuildingName.VANIER_LIBRARY.getResourceName() + FLOOR_TAG + "1", new ArrayList<IndoorPOI>(Arrays.asList(
                        new IndoorPOI(new MapCoordinates(45.45876976, -73.63850569, "VL 128"), POIType.CLASS_ROOM, BuildingName.VANIER_LIBRARY, "1", false),
                        new IndoorPOI(new MapCoordinates(45.45879847, -73.63880083, "VL 121"), POIType.CLASS_ROOM, BuildingName.VANIER_LIBRARY, "1", false)

                ))
                },
                {
                    BuildingName.VANIER_LIBRARY.getResourceName() + FLOOR_TAG + "2", new ArrayList<IndoorPOI>(Arrays.asList(
                        new IndoorPOI(new MapCoordinates(45.45912088, -73.63866360, "VL 201"), POIType.CLASS_ROOM, BuildingName.VANIER_LIBRARY, "2", false),
                        new IndoorPOI(new MapCoordinates(45.45904643, -73.63877435, "VL 204"), POIType.CLASS_ROOM, BuildingName.VANIER_LIBRARY, "2", false)
                ))
                },
                {
                    BuildingName.VANIER_LIBRARY.getResourceName() + FLOOR_TAG + "3", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.VANIER_EXTENSION.getResourceName() + FLOOR_TAG + "1", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.VANIER_EXTENSION.getResourceName() + FLOOR_TAG + "2", new ArrayList<IndoorPOI>()
                },
                {
                    BuildingName.VANIER_EXTENSION.getResourceName() + FLOOR_TAG + "3", new ArrayList<IndoorPOI>()
                }
        };
    }
}
