package minicap.concordia.campusnav.helpers;

import static org.junit.Assert.assertEquals;

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

@RunWith(RobolectricTestRunner.class)
public class CoordinateResHelperTest {

    private Context context;

    /**
     * A fake Resources subclass that overrides getValue() for our specific dimension IDs.
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
                outValue.data = Float.floatToIntBits(-73.0f); // dummy value for completeness
            } else {
                super.getValue(id, outValue, resolveRefs);
            }
        }
    }

    /**
     * A ContextWrapper that returns our FakeResources.
     */
    private static class FakeContext extends ContextWrapper {
        private Resources resources;

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
        // Get the Robolectric application context and wrap it.
        Context appContext = RuntimeEnvironment.getApplication();
        context = new FakeContext(appContext);
    }

    @Test
    public void testGetCoordsForGoogleMaps_validIds_returnsLatLng() {
        // Use the SGW hall building coordinates.
        int[] coords = new int[] {
                R.dimen.sgw_hall_building_lat,
                R.dimen.sgw_hall_building_lng
        };

        // This should now return the values we set in FakeResources.
        LatLng result = CoordinateResHelper.getCoordsForGoogleMaps(context, coords);

        assertEquals(45.5017, result.latitude, 0.0001);
        assertEquals(-73.5673, result.longitude, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCoordsForGoogleMaps_invalidArray_throwsException() {
        // Passing an array with only one element should throw an exception.
        CoordinateResHelper.getCoordsForGoogleMaps(context, new int[] { R.dimen.sgw_hall_building_lat });
    }

    @Test
    public void testGetFloat_validResource_returnsFloat() {
        // This should return the value we set for loy_hu_building_lat.
        float result = CoordinateResHelper.getFloat(context, R.dimen.loy_hu_building_lat);
        assertEquals(12.3456f, result, 0.0001f);
    }
}
