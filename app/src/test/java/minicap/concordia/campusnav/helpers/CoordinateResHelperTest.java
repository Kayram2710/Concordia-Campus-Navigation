package minicap.concordia.campusnav.helpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.util.TypedValue;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import minicap.concordia.campusnav.R;

/**
 * Expanded test for CoordinateResHelper to achieve full coverage.
 */
@RunWith(RobolectricTestRunner.class)
public class CoordinateResHelperTest {

    private Context context;

    /**
     * A fake Resources subclass that overrides getValue() for our specific dimension IDs
     * and throws NotFoundException for any unknown ID.
     */
    private static class FakeResources extends Resources {
        public FakeResources(Resources original) {
            super(original.getAssets(), original.getDisplayMetrics(), original.getConfiguration());
        }

        @Override
        public void getValue(int id, TypedValue outValue, boolean resolveRefs) throws NotFoundException {
            if (id == R.dimen.sgw_hall_building_lat) {
                outValue.type = TypedValue.TYPE_FLOAT;
                outValue.data = Float.floatToIntBits(45.5017f);
            } else if (id == R.dimen.sgw_hall_building_lng) {
                outValue.type = TypedValue.TYPE_FLOAT;
                outValue.data = Float.floatToIntBits(-73.5673f);
            } else if (id == R.dimen.loy_hu_building_lat) {
                outValue.type = TypedValue.TYPE_FLOAT;
                outValue.data = Float.floatToIntBits(12.3456f);
            } else if (id == R.dimen.loy_hu_building_lng) {
                outValue.type = TypedValue.TYPE_FLOAT;
                outValue.data = Float.floatToIntBits(-73.0f);
            } else {
                // For any other resource ID, simulate a missing resource
                throw new NotFoundException("FakeResources could not find resource ID: " + id);
            }
        }
    }

    /**
     * A ContextWrapper that returns our FakeResources.
     */
    private static class FakeContext extends ContextWrapper {
        private final Resources resources;

        public FakeContext(Context base) {
            super(base);
            resources = new FakeResources(base.getResources());
        }

        @Override
        public Resources getResources() {
            return resources;
        }
    }

    @Before
    public void setUp() {
        // Wrap the Robolectric application context with our FakeContext
        Context appContext = RuntimeEnvironment.getApplication();
        context = new FakeContext(appContext);
    }

    // ------------------------------------------------------------------------
    // Tests for getCoordsForGoogleMaps
    // ------------------------------------------------------------------------

    @Test
    public void testGetCoordsForGoogleMaps_validIds_returnsLatLng() {
        int[] coords = {
                R.dimen.sgw_hall_building_lat,
                R.dimen.sgw_hall_building_lng
        };

        LatLng result = CoordinateResHelper.getCoordsForGoogleMaps(context, coords);
        assertEquals(45.5017, result.latitude, 0.0001);
        assertEquals(-73.5673, result.longitude, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCoordsForGoogleMaps_invalidArray_throwsException() {
        // Only one element => should throw IllegalArgumentException
        CoordinateResHelper.getCoordsForGoogleMaps(context, new int[] { R.dimen.sgw_hall_building_lat });
    }

    @Test
    public void testGetCoordsForGoogleMaps_nonExistentResource_throwsNotFound() {
        // Provide IDs that our FakeResources won't recognize => should throw NotFoundException
        int[] coords = { 999999, 888888 };

        try {
            CoordinateResHelper.getCoordsForGoogleMaps(context, coords);
            fail("Expected a Resources.NotFoundException for unknown resource IDs");
        } catch (Resources.NotFoundException e) {
            // success
        }
    }

    // ------------------------------------------------------------------------
    // Tests for getFloat
    // ------------------------------------------------------------------------

    @Test
    public void testGetFloat_validResource_returnsFloat() {
        float result = CoordinateResHelper.getFloat(context, R.dimen.loy_hu_building_lat);
        assertEquals(12.3456f, result, 0.0001f);
    }

    @Test
    public void testGetFloat_nonExistentResource_throwsNotFound() {
        try {
            CoordinateResHelper.getFloat(context, 777777);
            fail("Expected a Resources.NotFoundException for unknown resource ID");
        } catch (Resources.NotFoundException e) {
            // success
        }
    }

    // ------------------------------------------------------------------------
    // Test for static inner class BuildingNames to cover its lines
    // ------------------------------------------------------------------------

    @Test
    public void testBuildingNames_staticArrays() {
        int[] sgwArray = CoordinateResHelper.BuildingNames.SGWHallBuilding;
        int[] loyArray = CoordinateResHelper.BuildingNames.LoyHUBuilding;
        assertNotNull("SGWHallBuilding array should not be null", sgwArray);
        assertNotNull("LoyHUBuilding array should not be null", loyArray);
        assertEquals("SGWHallBuilding array should have 2 elements", 2, sgwArray.length);
        assertEquals("LoyHUBuilding array should have 2 elements", 2, loyArray.length);
    }
}
