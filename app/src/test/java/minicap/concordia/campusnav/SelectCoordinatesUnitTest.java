package minicap.concordia.campusnav;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;
import com.google.android.gms.maps.model.LatLng;
import minicap.concordia.campusnav.screens.MapsActivity;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
/*
 * Unit test for MapsActivity
 */
public class SelectCoordinatesUnitTest {

    @Test
    /*
     * Test if coordinates retrieved matches coordinates clicked
     */
    public void isValidCoordinatesRouted() {
        //creates object of MapsActivity
        MapsActivity activity = new MapsActivity();
        LatLng testLatLng = new LatLng(1,1);
        activity.setLastClickedMapLocation(testLatLng);
        assertEquals(testLatLng,activity.getLastClickedMapLocation());

    }

}
