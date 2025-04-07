package minicap.concordia.campusnav.map.helpers;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import minicap.concordia.campusnav.map.enums.MapColors;

public class MapColorConversionHelper {

    private MapColorConversionHelper() {}

    /**
     * Returns the float representing the desired color in google maps
     * @param color The color enum
     * @return float indicating the desired color
     */
    public static float getGoogleMapsColor(MapColors color) {
        if(color == MapColors.BLUE) {
            return BitmapDescriptorFactory.HUE_AZURE;
        }
        else {
            return BitmapDescriptorFactory.HUE_RED;
        }
    }
}
