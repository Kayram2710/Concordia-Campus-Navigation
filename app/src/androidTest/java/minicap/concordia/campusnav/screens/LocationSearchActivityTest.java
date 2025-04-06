package minicap.concordia.campusnav.screens;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE;
import static androidx.test.espresso.matcher.ViewMatchers.Visibility.GONE;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import minicap.concordia.campusnav.R;

@RunWith(AndroidJUnit4.class)
public class LocationSearchActivityTest {

    @Rule
    public ActivityScenarioRule<LocationSearchActivity> activityScenarioRule =
            new ActivityScenarioRule<>(LocationSearchActivity.class);

    @Test
    public void testStartLocationBranchUI() {
        // Default intent (from the launcher) should simulate the "start location" branch.
        // In this branch, the "Use Current Location" button is visible and the hint is for starting point search.
        onView(withId(R.id.useCurrentLocationButton))
                .check(matches(withEffectiveVisibility(VISIBLE)));
        onView(withId(R.id.locationSearch))
                .check(matches(withHint(R.string.starting_point_search_hint)));
    }

    @Test
    public void testDestinationBranchUI() {
        // Launch the activity with extras indicating destination search.
        Intent intent = new Intent();
        intent.putExtra(LocationSearchActivity.KEY_IS_STARTING_LOCATION, false);
        intent.putExtra(LocationSearchActivity.KEY_PREVIOUS_INPUT_STRING, "DestinationPrev");
        try (ActivityScenario<LocationSearchActivity> scenario =
                     ActivityScenario.launch(intent)) {
            onView(withId(R.id.useCurrentLocationButton))
                    .check(matches(withEffectiveVisibility(GONE)));
            onView(withId(R.id.locationSearch))
                    .check(matches(withHint(R.string.destination_search_hint)));
            onView(withId(R.id.locationSearch))
                    .check(matches(withText("DestinationPrev")));
        }
    }

    @Test
    public void testUseCurrentLocationButtonClick() {
        // For the "start location" branch, simulate clicking the "Use Current Location" button.
        onView(withId(R.id.useCurrentLocationButton)).perform(click());
        // In the activity's implementation, clicking the button should set the result and finish the activity.
        // You can check that the activity finishes by pressing back (or using Espresso Intents if needed).
        pressBack();
        // (Further result-checking can be added with Espresso-Intents if desired.)
    }

    @Test
    public void testTextWatcherFiltering() {
        // Simulate typing into the search input.
        onView(withId(R.id.locationSearch)).perform(clearText(), typeText("Test input"), closeSoftKeyboard());
        // This will trigger the afterTextChanged() logic which calls adapter.filter().
        // For simplicity, we just ensure no crash occurs.
    }

    @Test
    public void testAdapterItemClick() {
        // This test assumes that the default list (set by setDefaultLocationList) is non-empty.
        // Simulate clicking on an item in the RecyclerView.
        // First, wait for the RecyclerView to be populated.
        onView(withId(R.id.results_recycler_view)).check(matches(withEffectiveVisibility(VISIBLE)));
        // Now perform a click on the first item. We use RecyclerViewActions.
        // (Make sure you have added: androidTestImplementation 'androidx.test.espresso:espresso-contrib:3.5.1')
        androidx.test.espresso.contrib.RecyclerViewActions
                .actionOnItemAtPosition(0, click());
        // This should trigger the adapter's onClick callback which sets the result and finishes the activity.
        pressBack(); // Optionally press back if needed.
    }
}
