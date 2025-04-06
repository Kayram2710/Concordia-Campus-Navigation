package minicap.concordia.campusnav;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.mappedin.sdk.MPIMapView;
import com.mappedin.sdk.listeners.MPIMapClickListener;
import com.mappedin.sdk.listeners.MPIMapViewListener;
import com.mappedin.sdk.models.MPIBlueDotPositionUpdate;
import com.mappedin.sdk.models.MPIBlueDotStateChange;
import com.mappedin.sdk.models.MPIData;
import com.mappedin.sdk.models.MPIMap;
import com.mappedin.sdk.models.MPINavigatable;
import com.mappedin.sdk.models.MPIState;
import com.mappedin.sdk.models.MPIMapClickEvent;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import minicap.concordia.campusnav.components.MappedInFragment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Instrumented test for MappedInFragment.
 *
 * This test verifies:
 * 1. The fragment inflates its layout and initializes the MPIMapView.
 * 2. The dummy MPIMapViewListener and MPIMapClickListener (provided via newInstance)
 *    are properly assigned to the MPIMapView.
 * 3. The getMapView() method returns a non-null view.
 *
 */
@RunWith(AndroidJUnit4.class)
public class MappedInFragmentInstrumentedTest {

    // Dummy implementation of MPIMapViewListener implementing all required methods.
    public static class DummyMPIMapViewListener implements MPIMapViewListener {
        @Override
        public void onBlueDotStateChange(MPIBlueDotStateChange blueDotStateChange) {
            // Empty implementation for testing.
        }
        @Override
        public void onBlueDotPositionUpdate(MPIBlueDotPositionUpdate blueDotPositionUpdate) {
            // Empty implementation for testing.
        }
        @Override
        public void onStateChanged(MPIState state) {
            // Empty implementation for testing.
        }
        @Override
        public void onFirstMapLoaded() {
            // Empty implementation for testing.
        }
        @Override
        public void onNothingClicked() {
            // Empty implementation for testing.
        }

        @Override
        public void onDataLoaded(@NonNull MPIData mpiData) {

        }

        @Override
        public void onMapChanged(@NonNull MPIMap mpiMap) {

        }

        @Override
        public void onPolygonClicked(@NonNull MPINavigatable.MPIPolygon mpiPolygon) {

        }
    }

    // Dummy implementation of MPIMapClickListener implementing all required methods.
    public static class DummyMPIMapClickListener implements MPIMapClickListener {
        @Override
        public void onClick(MPIMapClickEvent event) {
            // Empty implementation for testing.
        }
    }

    @Test
    public void testMappedInFragmentSetup() {
        //dummy listener instances.
        MPIMapViewListener dummyViewListener = new DummyMPIMapViewListener();
        MPIMapClickListener dummyClickListener = new DummyMPIMapClickListener();

        //instance of MappedInFragment using its factory method.
        MappedInFragment fragment = MappedInFragment.newInstance(dummyViewListener, dummyClickListener);

        // Launch the fragment using FragmentScenario with a custom FragmentFactory to inject our instance.
        FragmentScenario<MappedInFragment> scenario =
                FragmentScenario.launchInContainer(
                        MappedInFragment.class,
                        /* args= */ null,
                        // Use a valid theme; change to your app's theme if needed.
                        android.R.style.Theme_Material,
                        new FragmentFactory() {
                            @Override
                            public Fragment instantiate(ClassLoader classLoader, String className) {
                                if (className.equals(MappedInFragment.class.getName())) {
                                    return fragment;
                                }
                                return super.instantiate(classLoader, className);
                            }
                        }
                );

        scenario.onFragment(f -> {
            MPIMapView mapView = f.getMapView();
            assertNotNull("Map view should be initialized", mapView);
            try {
                // Access the private 'listener' field.
                Field listenerField = MPIMapView.class.getDeclaredField("listener");
                listenerField.setAccessible(true);
                Object actualViewListener = listenerField.get(mapView);
                assertEquals("MPIMapViewListener should be assigned", dummyViewListener, actualViewListener);

                // Access the private 'mapClickListener' field.
                Field clickListenerField = MPIMapView.class.getDeclaredField("mapClickListener");
                clickListenerField.setAccessible(true);
                Object actualClickListener = clickListenerField.get(mapView);
                assertEquals("MPIMapClickListener should be assigned", dummyClickListener, actualClickListener);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
