package minicap.concordia.campusnav;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBackUnconditionally;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;

import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import minicap.concordia.campusnav.components.MainMenuDialog;
import minicap.concordia.campusnav.screens.ClassScheduleActivity;
import minicap.concordia.campusnav.screens.MainActivity;
import minicap.concordia.campusnav.screens.MapsActivity;

/**
 * End-to-end test for MainMenuDialog.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainMenuDialogE2ETest {

    // Grant location permission so the test doesn't fail on permission dialogs.
    @Rule
    public GrantPermissionRule grantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    private ActivityScenario<MainActivity> scenario;
    private MainMenuDialog mainMenuDialog;

    @Before
    public void setUp() {
        Intents.init();
        scenario = activityScenarioRule.getScenario();
    }

    @After
    public void tearDown() {
        Intents.release();
        if (scenario != null) {
            scenario.close();
        }
    }

    @Test
    public void testMainMenuDialogFlow() {
        // Display the MainMenuDialog
        scenario.onActivity(activity -> {
            mainMenuDialog = new MainMenuDialog(activity);
            mainMenuDialog.show();
        });

        // Wait longer (2000ms) to allow the dialog to fully appear and get focus.
        onView(isRoot()).perform(waitFor(2000));

        // Verify that the dialog is displayed by checking for the close button.
        onView(withId(R.id.closeMenu))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));

        // Click on the 'classScheduleRedirect' button in the dialog.
        onView(withId(R.id.classScheduleRedirect))
                .inRoot(isDialog())
                .perform(click());

        // Verify that the ClassScheduleActivity is launched.
        intended(hasComponent(ClassScheduleActivity.class.getName()));

        // Press back to return to MainActivity.
        pressBackUnconditionally();

        // Show the dialog menu again.
        scenario.onActivity(activity -> {
            mainMenuDialog = new MainMenuDialog(activity);
            mainMenuDialog.show();
        });

        // Wait again for the dialog to get focus.
        onView(isRoot()).perform(waitFor(2000));

        // Click on the 'campusMapRedirect' button.
        onView(withId(R.id.campusMapRedirect))
                .inRoot(isDialog())
                .perform(click());

        // Verify that MapsActivity is launched.
        intended(hasComponent(MapsActivity.class.getName()));

        // Press back to return to MainActivity.
        pressBackUnconditionally();

        // Close the dialog if it's still visible.
        scenario.onActivity(activity -> {
            if (mainMenuDialog.isShowing()) {
                mainMenuDialog.close();
            }
        });
    }

    /**
     * A helper ViewAction to wait for a specific amount of time on the main thread.
     * This allows Espresso to wait before performing the next action.
     */
    private ViewAction waitFor(final long ms) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "Wait for " + ms + " milliseconds.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                uiController.loopMainThreadForAtLeast(ms);
            }
        };
    }
}
