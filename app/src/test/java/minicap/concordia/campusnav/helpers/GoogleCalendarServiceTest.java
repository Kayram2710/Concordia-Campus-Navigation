package minicap.concordia.campusnav.helpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.google.api.services.calendar.Calendar;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import minicap.concordia.campusnav.helpers.GoogleCalendarService;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28) // or another supported Robolectric API level
public class GoogleCalendarServiceTest {

    private Context context;

    @Before
    public void setUp() {
        // Robolectric-provided context
        context = ApplicationProvider.getApplicationContext();
    }

    @Test
    public void testGetCalendarService_returnsNonNullCalendar() {
        // Call the static method
        Calendar calendarService = GoogleCalendarService.getCalendarService(context);

        // Verify it's not null
        assertNotNull("Calendar service should not be null", calendarService);

        // Check that the application name is set
        assertEquals("Campus Navigation App", calendarService.getApplicationName());
    }
}
