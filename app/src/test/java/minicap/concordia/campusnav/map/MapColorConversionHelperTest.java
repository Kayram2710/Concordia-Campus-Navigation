package minicap.concordia.campusnav.map;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import org.junit.Assert;
import org.junit.Test;

import minicap.concordia.campusnav.map.enums.MapColors;
import minicap.concordia.campusnav.map.helpers.MapColorConversionHelper;

public class MapColorConversionHelperTest {

    @Test
    public void testGetGoogleMapsColor_DefaultColor() {
        MapColors color = MapColors.DEFAULT;
        float expectedColor = BitmapDescriptorFactory.HUE_RED;

        float actual = MapColorConversionHelper.getGoogleMapsColor(color);

        Assert.assertEquals(expectedColor, actual, 0.0001);
    }

    @Test
    public void testGetGoogleMapsColor_NonDefaultColor() {
        MapColors color = MapColors.BLUE;
        float expectedColor = BitmapDescriptorFactory.HUE_AZURE;

        float actual = MapColorConversionHelper.getGoogleMapsColor(color);

        Assert.assertEquals(expectedColor, actual, 0.0001);
    }

}
