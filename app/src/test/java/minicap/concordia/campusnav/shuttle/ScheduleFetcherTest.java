package minicap.concordia.campusnav.shuttle;

import org.junit.Test;
import java.util.List;

import static org.junit.Assert.*;

import minicap.concordia.campusnav.helpers.ScheduleFetcher;

public class ScheduleFetcherTest {

    @Test
    public void testListenerInterfaceExists() {
        // Just verify the callback interface exists and can be implemented
        ScheduleFetcher.ScheduleFetchListener listener = schedules -> {
            // No-op implementation
        };
        assertNotNull(listener);
    }

    @Test
    public void testCanCreateFetcher() {
        // Just verify we can call the fetch method without errors
        ScheduleFetcher.fetch(schedules -> {});
        // If we get here without exceptions, the test passes
        assertTrue(true);
    }
}
