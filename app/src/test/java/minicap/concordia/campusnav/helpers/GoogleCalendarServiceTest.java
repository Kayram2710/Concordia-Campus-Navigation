package minicap.concordia.campusnav.helpers;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.google.api.services.calendar.Calendar;

import org.junit.Test;

public class GoogleCalendarServiceTest {

    @Test
    public void testGetCalendarService() {
        // Get an application context using AndroidX Test Core
        Context context = ApplicationProvider.getApplicationContext();

        // Call the static method to get the Calendar service.
        Calendar calendar = GoogleCalendarService.getCalendarService(context);

        // Verify that the returned Calendar service is not null.
        assertNotNull("Calendar service should not be null", calendar);

        // Verify that the Calendar service has the expected application name.
        // (The Calendar object extends AbstractGoogleJsonClient which provides getApplicationName())
        assertEquals("Campus Navigation App", calendar.getApplicationName());
    }
}
