package minicap.concordia.campusnav.buildingmanager.entities.poi;

import minicap.concordia.campusnav.buildingmanager.entities.Location;
import minicap.concordia.campusnav.buildingmanager.enumerations.POIType;

public class POI extends Location {

    private String poiName;
    private POIType poiType;
    private boolean isAccessibilityFeature;

    //This constructor is protected because it should only be called deriving classes
    protected POI(String name, POIType type, boolean isAccessibilityFeature, float latitude, float longitude){
        super(latitude, longitude);
        this.poiName = name;
        this.poiType = type;
        this.isAccessibilityFeature = isAccessibilityFeature;
    }

    /**
     * Gets the type of the point of interest
     * @return The POIType of the POI
     */
    public POIType getPOIType(){
        return poiType;
    }

    /**
     * Gets the boolean to indicate if this point of interest is for accessibility
     * @return True if it is accessibility option, false otherwise
     */
    public boolean getIsAccessibilityFeature(){
        return isAccessibilityFeature;
    }

    /**
     * Gets the name for the point of interest
     * @return The string name of the POI
     */
    public String getPoiName(){
        return poiName;
    }
}
