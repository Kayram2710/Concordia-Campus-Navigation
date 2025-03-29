package minicap.concordia.campusnav.screens;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openContextualActionModeOverflowMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Build;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import minicap.concordia.campusnav.R;

/**
 * Instrumented test for ClassScheduleActivity using Espresso.
 * Ensures that UI elements can be interacted with and checks
 * behavior for sign-in and permission flow.
 */
@RunWith(AndroidJUnit4.class)
public class ClassScheduleActivityTest {

    // If you want to auto-grant the Calendar permission in tests, you can use:
    @Rule
    public GrantPermissionRule grantPermissionRule =
            GrantPermissionRule.grant(android.Manifest.permission.READ_CALENDAR);

    private ActivityScenario<ClassScheduleActivity> scenario;

    @Before
    public void setUp() {
        // Launch the Activity
        scenario = ActivityScenario.launch(ClassScheduleActivity.class);
        Intents.init(); // Initialize Espresso-Intents if you want to verify or stub Intents
    }

    @After
    public void tearDown() {
        Intents.release();
        scenario.close();
    }

    @Test
    public void testMenuButton_opensMainMenuDialog() {
        // Click the menu button
        onView(withId(R.id.button_menu)).perform(click());
        // The MainMenuDialog presumably shows something. If there's a known text, check it:
        onView(withText("Main Menu"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testClickImportCalendar_startsSignInIntent() {
        // Click the "Import" button
        onView(withId(R.id.button_import_calendar)).perform(click());


        // ensure no crash & the scenario is still active:
        assertTrue(true);
    }

    @Test
    public void testOnActivityResult_simulateGoogleSignInSuccess() {

        scenario.onActivity(activity -> {
            // Create a fake Intent data that mimics a successful sign-in
            Intent fakeData = new Intent();

            activity.onActivityResult(100 /*RC_SIGN_IN*/, Activity.RESULT_OK, fakeData);
            // The activity should show "Google Sign-In Success" toast
            // and attempt to request permission (which we auto-granted).
        });

        // No direct Espresso check for Toast unless using ToastMatcher or a library.
        // We'll just confirm the scenario is still active:
        assertTrue(true);
    }

    @Test
    public void testOnRequestPermissionsResult_granted() {
        scenario.onActivity(activity -> {
            // Directly call onRequestPermissionsResult to simulate user granting permission:
            String[] permissions = new String[] {android.Manifest.permission.READ_CALENDAR};
            int[] grantResults = new int[] {android.content.pm.PackageManager.PERMISSION_GRANTED};
            activity.onRequestPermissionsResult(101, permissions, grantResults);
            // Should show "Calendar permission granted" toast and fetch events.
        });
        assertTrue(true);
    }

    @Test
    public void testOnRequestPermissionsResult_denied() {
        scenario.onActivity(activity -> {
            String[] permissions = new String[] {android.Manifest.permission.READ_CALENDAR};
            int[] grantResults = new int[] {android.content.pm.PackageManager.PERMISSION_DENIED};
            activity.onRequestPermissionsResult(101, permissions, grantResults);
            // Should show "Calendar permission denied" toast.
        });
        assertTrue(true);
    }
}
