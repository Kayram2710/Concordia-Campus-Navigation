package minicap.concordia.campusnav.helpers;

import static org.junit.Assert.*;

import android.content.Context;

import com.google.api.services.calendar.Calendar;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

public class GoogleCalendarServiceTest {

    @Test
    public void testGetCalendarServiceCoversAllLines() throws Exception {
        // Mock a simple Context (no Robolectric needed)
        Context mockContext = Mockito.mock(Context.class);

        // 1) Call the service method
        Calendar calendar = GoogleCalendarService.getCalendarService(mockContext);
        assertNotNull("Calendar should not be null", calendar);
        assertEquals("Campus Navigation App", calendar.getApplicationName());

        // 2) Reflectively access the private static fields to ensure coverage of their lines
        Field transportField = GoogleCalendarService.class.getDeclaredField("HTTP_TRANSPORT");
        transportField.setAccessible(true);
        Object transportVal = transportField.get(null);
        assertNotNull("HTTP_TRANSPORT should not be null", transportVal);

        Field jsonFactoryField = GoogleCalendarService.class.getDeclaredField("JSON_FACTORY");
        jsonFactoryField.setAccessible(true);
        Object jsonFactoryVal = jsonFactoryField.get(null);
        assertNotNull("JSON_FACTORY should not be null", jsonFactoryVal);
    }
}
