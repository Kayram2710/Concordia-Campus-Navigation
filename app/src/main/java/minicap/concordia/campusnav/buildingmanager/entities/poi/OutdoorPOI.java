package minicap.concordia.campusnav.buildingmanager.entities.poi;

import minicap.concordia.campusnav.buildingmanager.enumerations.POIType;
import minicap.concordia.campusnav.map.MapCoordinates;

public class OutdoorPOI extends POI{

    public OutdoorPOI(MapCoordinates coordinates, POIType type, boolean isAccessibilityFeature) {
        super(coordinates, type, isAccessibilityFeature);
    }
}
