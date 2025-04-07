package minicap.concordia.campusnav.buildingshape;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CampusBuildingShapes
{
    // polygon creation for buildings
    private static final List<PolygonOptions> sgwBuildingCoordinates = new ArrayList<>();
    private static final List<PolygonOptions> loyolaBuildingCoordinates = new ArrayList<>();

    private CampusBuildingShapes() {}

    // SGW Campus
    static {
        ResourceBundle bundle = ResourceBundle.getBundle("minicap.concordia.campusnav.buildingshape.SGWCoordinatesResource_en_CA");

        sgwBuildingCoordinates.add((PolygonOptions) bundle.getObject("bAnnex"));
        sgwBuildingCoordinates.add((PolygonOptions) bundle.getObject("ciAnnex"));
        sgwBuildingCoordinates.add((PolygonOptions) bundle.getObject("clAnnex"));
        sgwBuildingCoordinates.add((PolygonOptions) bundle.getObject("dAnnex"));
        sgwBuildingCoordinates.add((PolygonOptions) bundle.getObject("enAnnex"));
        sgwBuildingCoordinates.add((PolygonOptions) bundle.getObject("erBuilding"));
        sgwBuildingCoordinates.add((PolygonOptions) bundle.getObject("evBuilding"));
        sgwBuildingCoordinates.add((PolygonOptions) bundle.getObject("faAnnex"));
        sgwBuildingCoordinates.add((PolygonOptions) bundle.getObject("fbBuilding"));
        sgwBuildingCoordinates.add((PolygonOptions) bundle.getObject("fgBuilding"));
        sgwBuildingCoordinates.add((PolygonOptions) bundle.getObject("greyNunsAnnex"));
        sgwBuildingCoordinates.add((PolygonOptions) bundle.getObject("gmBuilding"));
        sgwBuildingCoordinates.add((PolygonOptions) bundle.getObject("gnBuilding"));
        sgwBuildingCoordinates.add((PolygonOptions) bundle.getObject("gsBuilding"));
        sgwBuildingCoordinates.add((PolygonOptions) bundle.getObject("Hall Building"));
        sgwBuildingCoordinates.add((PolygonOptions) bundle.getObject("kAnnex"));
        sgwBuildingCoordinates.add((PolygonOptions) bundle.getObject("lbBuilding"));
        sgwBuildingCoordinates.add((PolygonOptions) bundle.getObject("ldBuilding"));
        sgwBuildingCoordinates.add((PolygonOptions) bundle.getObject("lsBuilding"));
        sgwBuildingCoordinates.add((PolygonOptions) bundle.getObject("mAnnex"));
        sgwBuildingCoordinates.add((PolygonOptions) bundle.getObject("John Molson Building"));
        sgwBuildingCoordinates.add((PolygonOptions) bundle.getObject("miAnnex"));
        sgwBuildingCoordinates.add((PolygonOptions) bundle.getObject("muAnnex"));
        sgwBuildingCoordinates.add((PolygonOptions) bundle.getObject("pAnnex"));
        sgwBuildingCoordinates.add((PolygonOptions) bundle.getObject("prAnnex"));
        sgwBuildingCoordinates.add((PolygonOptions) bundle.getObject("qAnnex"));
        sgwBuildingCoordinates.add((PolygonOptions) bundle.getObject("rAnnex"));
        sgwBuildingCoordinates.add((PolygonOptions) bundle.getObject("rrAnnex"));
        sgwBuildingCoordinates.add((PolygonOptions) bundle.getObject("sAnnex"));
        sgwBuildingCoordinates.add((PolygonOptions) bundle.getObject("sbBuilding"));
        sgwBuildingCoordinates.add((PolygonOptions) bundle.getObject("tAnnex"));
        sgwBuildingCoordinates.add((PolygonOptions) bundle.getObject("tdBuilding"));
        sgwBuildingCoordinates.add((PolygonOptions) bundle.getObject("vAnnex"));
        sgwBuildingCoordinates.add((PolygonOptions) bundle.getObject("vaBuilding"));
        sgwBuildingCoordinates.add((PolygonOptions) bundle.getObject("xAnnex"));
        sgwBuildingCoordinates.add((PolygonOptions) bundle.getObject("zAnnex"));
    }

    // Loyola Campus
    static {
        ResourceBundle bundle = ResourceBundle.getBundle("minicap.concordia.campusnav.buildingshape.LoyolaCoordinatesResource_en_CA");

        loyolaBuildingCoordinates.add((PolygonOptions) bundle.getObject("adBuilding"));
        loyolaBuildingCoordinates.add((PolygonOptions) bundle.getObject("bbAnnex"));
        loyolaBuildingCoordinates.add((PolygonOptions) bundle.getObject("bhAnnex"));
        loyolaBuildingCoordinates.add((PolygonOptions) bundle.getObject("Loyola Central Building"));
        loyolaBuildingCoordinates.add((PolygonOptions) bundle.getObject("cjBuilding"));
        loyolaBuildingCoordinates.add((PolygonOptions) bundle.getObject("doDome"));
        loyolaBuildingCoordinates.add((PolygonOptions) bundle.getObject("fcBuilding"));
        loyolaBuildingCoordinates.add((PolygonOptions) bundle.getObject("geBuilding"));
        loyolaBuildingCoordinates.add((PolygonOptions) bundle.getObject("haBuilding"));
        loyolaBuildingCoordinates.add((PolygonOptions) bundle.getObject("hbBuilding"));
        loyolaBuildingCoordinates.add((PolygonOptions) bundle.getObject("hcBuilding"));
        loyolaBuildingCoordinates.add((PolygonOptions) bundle.getObject("huBuilding"));
        loyolaBuildingCoordinates.add((PolygonOptions) bundle.getObject("jrResidence"));
        loyolaBuildingCoordinates.add((PolygonOptions) bundle.getObject("pcBuilding"));
        loyolaBuildingCoordinates.add((PolygonOptions) bundle.getObject("psBuilding"));
        loyolaBuildingCoordinates.add((PolygonOptions) bundle.getObject("Vanier Library/Extension Building"));
        loyolaBuildingCoordinates.add((PolygonOptions) bundle.getObject("pyBuilding"));
        loyolaBuildingCoordinates.add((PolygonOptions) bundle.getObject("raBuilding"));
        loyolaBuildingCoordinates.add((PolygonOptions) bundle.getObject("rfBuilding"));
        loyolaBuildingCoordinates.add((PolygonOptions) bundle.getObject("siBuilding"));
        loyolaBuildingCoordinates.add((PolygonOptions) bundle.getObject("spBuilding"));
        loyolaBuildingCoordinates.add((PolygonOptions) bundle.getObject("taBuilding"));
    }

    /**
     * Gets the coordinates for the polygons that represent the SGW buildings
     * @return List of PolygonOptions that represent the shapes of the various buildings
     */
    public static List<PolygonOptions> getSgwBuildingCoordinates() {
        return sgwBuildingCoordinates;
    }

    /**
     * Gets the coordinates for the polygons that represent the Loyola buildings
     * @return List of PolygonOptions that represent the shapes of the various buildings
     */
    public static List<PolygonOptions> getLoyolaBuildingCoordinates() {
        return loyolaBuildingCoordinates;
    }

    /**
     * Method used to determine if you're inside a building based on location
     * @param location The user's current location
     * @return The key for the resource that represents the building
     */
    public static String getBuildingNameAtLocation(LatLng location) {
        ResourceBundle sgwBundle = ResourceBundle.getBundle("minicap.concordia.campusnav.buildingshape.SGWCoordinatesResource_en_CA");
        for (String key : sgwBundle.keySet()) {
            PolygonOptions polygon = (PolygonOptions) sgwBundle.getObject(key);
            if(PolyUtil.containsLocation(location, polygon.getPoints(), true)) {
                return key;
            }
        }

        ResourceBundle loyBundle = ResourceBundle.getBundle("minicap.concordia.campusnav.buildingshape.LoyolaCoordinatesResource_en_CA");
        for (String key : loyBundle.keySet()) {
            PolygonOptions polygon = (PolygonOptions) loyBundle.getObject(key);
            if(PolyUtil.containsLocation(location, polygon.getPoints(), true)) {
                return key;
            }
        }

        return null;
    }
}
