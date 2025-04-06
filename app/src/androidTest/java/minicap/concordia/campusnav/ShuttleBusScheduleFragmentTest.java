package minicap.concordia.campusnav;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.fragment.app.FragmentFactory;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import minicap.concordia.campusnav.helpers.ScheduleFetcher;
import minicap.concordia.campusnav.helpers.ScheduleFetcher.ScheduleFetchListener;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import minicap.concordia.campusnav.components.placeholder.ShuttleBusScheduleFragment;
import minicap.concordia.campusnav.components.ShuttleSchedule;
import minicap.concordia.campusnav.R;

@RunWith(AndroidJUnit4.class)
public class ShuttleBusScheduleFragmentTest {

    @Test
    public void testDefaultUISetup() {
        try (MockedStatic<ScheduleFetcher> mockedFetcher = Mockito.mockStatic(ScheduleFetcher.class)) {
            // Stub ScheduleFetcher.fetch(...) to immediately return dummy schedules.
            mockedFetcher.when(() -> ScheduleFetcher.fetch(any(ScheduleFetchListener.class)))
                    .thenAnswer(invocation -> {
                        ScheduleFetchListener listener = invocation.getArgument(0);
                        // Create dummy schedules for SGW and Loyola (day "Monday-Thursday")
                        List<ShuttleSchedule> dummySchedules = new ArrayList<>();
                        dummySchedules.add(new ShuttleSchedule("SGW", "Monday-Thursday", Arrays.asList("10:00", "11:00")));
                        dummySchedules.add(new ShuttleSchedule("Loyola", "Monday-Thursday", Arrays.asList("10:00", "11:00")));
                        listener.onScheduleFetched(dummySchedules);
                        return null;
                    });

            FragmentScenario<ShuttleBusScheduleFragment> scenario =
                    FragmentScenario.launchInContainer(
                            ShuttleBusScheduleFragment.class,
                            null,
                            android.R.style.Theme_Material,
                            (FragmentFactory) null
                    );

            scenario.onFragment(fragment -> {
                View root = fragment.getView();
                assertNotNull("Fragment view should not be null", root);

                // In onViewCreated the fragment calls filterSchedule for both campuses.
                // For our dummy schedules, updateUI adds 2 departure times plus 2 empty slots,
                // so each GridLayout should have 4 children.
                GridLayout sgwGrid = root.findViewById(R.id.sgwGridLayout);
                GridLayout loyGrid = root.findViewById(R.id.loyGridLayout);
                assertNotNull("SGW GridLayout should be present", sgwGrid);
                assertNotNull("Loyola GridLayout should be present", loyGrid);
                assertEquals("SGW GridLayout should have 4 children", 4, sgwGrid.getChildCount());
                assertEquals("Loyola GridLayout should have 4 children", 4, loyGrid.getChildCount());
            });
        }
    }

    @Test
    public void testButtonClickUpdatesUIAndSelection() {
        try (MockedStatic<ScheduleFetcher> mockedFetcher = Mockito.mockStatic(ScheduleFetcher.class)) {
            // Stub ScheduleFetcher.fetch(...) to return dummy schedules for "Friday".
            mockedFetcher.when(() -> ScheduleFetcher.fetch(any(ScheduleFetchListener.class)))
                    .thenAnswer(invocation -> {
                        ScheduleFetchListener listener = invocation.getArgument(0);
                        // For Friday, create dummy schedules with 2 departure times.
                        List<ShuttleSchedule> dummySchedules = new ArrayList<>();
                        dummySchedules.add(new ShuttleSchedule("SGW", "Friday", Arrays.asList("12:00", "12:30")));
                        dummySchedules.add(new ShuttleSchedule("Loyola", "Friday", Arrays.asList("12:00", "12:30")));
                        listener.onScheduleFetched(dummySchedules);
                        return null;
                    });

            FragmentScenario<ShuttleBusScheduleFragment> scenario =
                    FragmentScenario.launchInContainer(
                            ShuttleBusScheduleFragment.class,
                            null,
                            android.R.style.Theme_Material,
                            (FragmentFactory) null
                    );

            // Use Espresso to click the Friday buttons.
            androidx.test.espresso.Espresso.onView(
                            androidx.test.espresso.matcher.ViewMatchers.withId(R.id.sgwFridayButton))
                    .perform(androidx.test.espresso.action.ViewActions.click());

            androidx.test.espresso.Espresso.onView(
                            androidx.test.espresso.matcher.ViewMatchers.withId(R.id.loyFridayButton))
                    .perform(androidx.test.espresso.action.ViewActions.click());

            scenario.onFragment(fragment -> {
                View root = fragment.getView();
                assertNotNull("Fragment view should not be null", root);

                // For Friday schedules, updateUI should add 2 departure times and 1 empty slot = 3 views.
                GridLayout sgwGrid = root.findViewById(R.id.sgwGridLayout);
                GridLayout loyGrid = root.findViewById(R.id.loyGridLayout);
                assertNotNull("SGW GridLayout should be present", sgwGrid);
                assertNotNull("Loyola GridLayout should be present", loyGrid);
                assertEquals("SGW GridLayout should have 3 children after Friday filter", 3, sgwGrid.getChildCount());
                assertEquals("Loyola GridLayout should have 3 children after Friday filter", 3, loyGrid.getChildCount());

                // Verify button selection states.
                Button sgwMonThursButton = root.findViewById(R.id.sgwMonThursButton);
                Button sgwFridayButton = root.findViewById(R.id.sgwFridayButton);
                Button loyMonThursButton = root.findViewById(R.id.loyMonThursButton);
                Button loyFridayButton = root.findViewById(R.id.loyFridayButton);
                assertNotNull(sgwMonThursButton);
                assertNotNull(sgwFridayButton);
                assertNotNull(loyMonThursButton);
                assertNotNull(loyFridayButton);
                // For each campus, the Friday button should be selected.
                assertFalse("SGW Mon-Thurs button should not be selected", sgwMonThursButton.isSelected());
                assertTrue("SGW Friday button should be selected", sgwFridayButton.isSelected());
                assertFalse("Loyola Mon-Thurs button should not be selected", loyMonThursButton.isSelected());
                assertTrue("Loyola Friday button should be selected", loyFridayButton.isSelected());
            });
        }
    }

    @Test
    public void testOnDestroyViewCleansUpBinding() throws Exception {
        FragmentScenario<ShuttleBusScheduleFragment> scenario =
                FragmentScenario.launchInContainer(
                        ShuttleBusScheduleFragment.class,
                        null,
                        android.R.style.Theme_Material,
                        (FragmentFactory) null
                );
        scenario.onFragment(fragment -> {
            // Call onDestroyView() explicitly.
            fragment.onDestroyView();
            try {
                // Use reflection to access the private _binding field.
                java.lang.reflect.Field bindingField = ShuttleBusScheduleFragment.class.getDeclaredField("_binding");
                bindingField.setAccessible(true);
                Object bindingValue = bindingField.get(fragment);
                assertEquals("Binding should be null after onDestroyView", null, bindingValue);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
