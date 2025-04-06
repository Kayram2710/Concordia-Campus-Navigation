package minicap.concordia.campusnav.screens;

import android.app.Activity;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.view.WindowManager;
import minicap.concordia.campusnav.R;


import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.Root;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.action.ViewActions;

import com.google.api.client.util.DateTime;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Method;

import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ClassScheduleActivityTest {

    @Rule
    public ActivityScenarioRule<ClassScheduleActivity> activityScenarioRule =
            new ActivityScenarioRule<>(ClassScheduleActivity.class);

    /**
     * A custom ToastMatcher that looks for Toast windows.
     */
    public static class ToastMatcher extends TypeSafeMatcher<Root> {
        @Override
        public void describeTo(Description description) {
            description.appendText("is toast");
        }
        @Override
        public boolean matchesSafely(Root root) {
            int type = root.getWindowLayoutParams().get().type;
            if (type == WindowManager.LayoutParams.TYPE_TOAST) {
                IBinder windowToken = root.getDecorView().getWindowToken();
                IBinder appToken = root.getDecorView().getApplicationWindowToken();
                return windowToken == appToken;
            }
            return false;
        }
    }

    /**
     * Verify that the UI components (RecyclerView, menu button, import button) are present.
     */
    @Test
    public void testUiComponentsDisplayed() {
        Espresso.onView(withId(R.id.rv_event_list))
                .check(ViewAssertions.matches(isDisplayed()));
        Espresso.onView(withId(R.id.button_menu))
                .check(ViewAssertions.matches(isDisplayed()));
        Espresso.onView(withId(R.id.button_import_calendar))
                .check(ViewAssertions.matches(isDisplayed()));
    }

    /**
     * Verify that clicking the menu button calls showMainMenuDialog.
     * (Here we simply perform the click and ensure no crash occurs.)
     */
    @Test
    public void testShowMainMenuDialog() {
        Espresso.onView(withId(R.id.button_menu))
                .perform(ViewActions.click());
        // No explicit assertion here because the dialog layout is not specified.
    }

    /**
     * Test the onRequestPermissionsResult callback when permission is granted.
     * It should show a toast "Calendar permission granted!".
     */
    @Test
    public void testOnRequestPermissionsResultGranted() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            int[] grantResults = { PackageManager.PERMISSION_GRANTED };
            activity.onRequestPermissionsResult(101,
                    new String[]{Manifest.permission.READ_CALENDAR}, grantResults);
        });
        Espresso.onView(withText("Calendar permission granted!"))
                .inRoot(new ToastMatcher())
                .check(ViewAssertions.matches(isDisplayed()));
    }

    /**
     * Test the onRequestPermissionsResult callback when permission is denied.
     * It should show a toast "Calendar permission denied".
     */
    @Test
    public void testOnRequestPermissionsResultDenied() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            int[] grantResults = { PackageManager.PERMISSION_DENIED };
            activity.onRequestPermissionsResult(101,
                    new String[]{Manifest.permission.READ_CALENDAR}, grantResults);
        });
        Espresso.onView(withText("Calendar permission denied"))
                .inRoot(new ToastMatcher())
                .check(ViewAssertions.matches(isDisplayed()));
    }

    /**
     * Test the onActivityResult callback for RC_SIGN_IN.
     * Here we simulate a failure case by passing a null Intent,
     * which causes Google sign-in to fail and shows a toast.
     */
    @Test
    public void testOnActivityResultFailure() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            activity.onActivityResult(100, Activity.RESULT_OK, null);
        });
        Espresso.onView(withText(startsWith("Google Sign-In failed")))
                .inRoot(new ToastMatcher())
                .check(ViewAssertions.matches(isDisplayed()));
    }

    /**
     * Test fetchCalendarEvents() when no user is signed in.
     * This method is private so we invoke it via reflection.
     * In this branch, a toast "Not signed in!" is shown.
     */
    @Test
    public void testFetchCalendarEventsNotSignedIn() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            try {
                Method method = ClassScheduleActivity.class.getDeclaredMethod("fetchCalendarEvents");
                method.setAccessible(true);
                method.invoke(activity);
            } catch (Exception e) {
                fail("Exception invoking fetchCalendarEvents: " + e.getMessage());
            }
        });
        Espresso.onView(withText("Not signed in!"))
                .inRoot(new ToastMatcher())
                .check(ViewAssertions.matches(isDisplayed()));
    }

    /**
     * Test the private formatDateTime(DateTime) method using reflection.
     */
    @Test
    public void testFormatDateTime() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            try {
                Method method = ClassScheduleActivity.class.getDeclaredMethod("formatDateTime", DateTime.class);
                method.setAccessible(true);
                DateTime now = new DateTime(System.currentTimeMillis());
                String formatted = (String) method.invoke(activity, now);
                assertNotNull("Formatted date/time should not be null", formatted);
                assertFalse("Formatted date/time should not be empty", formatted.isEmpty());
            } catch (Exception e) {
                fail("Exception in testFormatDateTime: " + e.getMessage());
            }
        });
    }
}
