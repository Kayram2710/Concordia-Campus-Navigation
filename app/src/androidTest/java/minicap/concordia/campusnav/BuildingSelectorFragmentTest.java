package minicap.concordia.campusnav;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.contrib.RecyclerViewActions;

import org.junit.Test;
import org.junit.runner.RunWith;

import minicap.concordia.campusnav.components.BuildingSelectorFragment;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertNotNull;

/**
 * Instrumented test for BuildingSelectorFragment.
 *
 * This test:
 * 1. Launches BuildingSelectorFragment in isolation using FragmentScenario.
 * 2. Checks that both SGW and Loyola RecyclerViews are displayed.
 * 3. Performs a click on an item in the SGW RecyclerView.
 * 4. Verifies that the BuildingInfoBottomSheetFragment is shown by checking
 *    for its tag ("BuildingInfoBottomSheet") in the parent fragment manager.
 */
@RunWith(AndroidJUnit4.class)
public class BuildingSelectorFragmentTest {

    @Test
    public void testFragmentLifecycleAndInteractions() {
        // Launch the BuildingSelectorFragment in a container with a valid theme.
        // Cast null as FragmentFactory to resolve ambiguous overload.
        FragmentScenario<BuildingSelectorFragment> scenario =
                FragmentScenario.launchInContainer(
                        BuildingSelectorFragment.class,
                        /* args= */ null,
                        android.R.style.Theme_Material,
                        (FragmentFactory) null
                );

        // Verify that both SGW and Loyola RecyclerViews are displayed.
        onView(withId(R.id.sgwRecyclerView))
                .check(matches(isDisplayed()));
        onView(withId(R.id.loyRecyclerView))
                .check(matches(isDisplayed()));

        // Perform a click on the first item in the SGW RecyclerView.
        onView(withId(R.id.sgwRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Verify that the BuildingInfoBottomSheetFragment is displayed.
        // We do this by checking that a fragment with the tag "BuildingInfoBottomSheet" exists.
        scenario.onFragment(fragment -> {
            FragmentManager fm = fragment.getParentFragmentManager();
            Fragment bottomSheet = fm.findFragmentByTag("BuildingInfoBottomSheet");
            assertNotNull("BuildingInfoBottomSheetFragment should be displayed", bottomSheet);
        });
    }
}
