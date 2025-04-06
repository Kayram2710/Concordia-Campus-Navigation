package minicap.concordia.campusnav.helpers;

import android.content.Context;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.calendar.Calendar;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class GoogleCalendarServiceInstrumentedTest {

    @Test
    public void testGetCalendarService() {
        // Get the application context for the test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        // Call the helper method to obtain a Calendar instance.
        Calendar calendar = GoogleCalendarService.getCalendarService(appContext);

        // Verify that the returned Calendar is not null.
        assertNotNull("The Calendar instance should not be null", calendar);

        // Use reflection to verify that the Calendar's application name is set correctly.
        try {
            java.lang.reflect.Field appNameField = calendar.getClass().getSuperclass().getDeclaredField("applicationName");
            appNameField.setAccessible(true);
            String applicationName = (String) appNameField.get(calendar);
            assertEquals("Application name should be 'Campus Navigation App'",
                    "Campus Navigation App", applicationName);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Reflection failed while accessing applicationName: " + e.getMessage());
        }

        // verify that the HttpRequestInitializer used in the Calendar is a GoogleAccountCredential.
        try {
            // The HttpRequestInitializer is stored in a field named "httpRequestInitializer" in the superclass.
            java.lang.reflect.Field initializerField = calendar.getClass().getSuperclass().getDeclaredField("httpRequestInitializer");
            initializerField.setAccessible(true);
            Object initializer = initializerField.get(calendar);
            assertNotNull("HttpRequestInitializer should not be null", initializer);
            assertTrue("HttpRequestInitializer should be an instance of GoogleAccountCredential",
                    initializer instanceof GoogleAccountCredential);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Reflection failed while accessing httpRequestInitializer: " + e.getMessage());
        }
    }
}
