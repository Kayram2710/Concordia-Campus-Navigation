package minicap.concordia.campusnav.helpers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mockStatic;

import android.os.Looper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.mockito.MockedStatic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import minicap.concordia.campusnav.components.ShuttleSchedule;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class ScheduleFetcherTest {

    @Test
    public void testFetchReturnsSchedules() throws Exception {
        // Prepare a dummy list of ShuttleSchedule objects.
        List<ShuttleSchedule> dummySchedules = new ArrayList<>();

        ShuttleSchedule dummy = new ShuttleSchedule("Dummy Shuttle", "12:00", new ArrayList<>());
        dummySchedules.add(dummy);

        CountDownLatch latch = new CountDownLatch(1);

        try (MockedStatic<ShuttleScraper> mockedScraper = mockStatic(ShuttleScraper.class)) {
            mockedScraper.when(ShuttleScraper::fetchSchedule).thenReturn(dummySchedules);

            ScheduleFetcher.fetch(schedules -> {
                try {
                    assertNotNull("Schedules should not be null", schedules);
                    assertEquals("Fetched schedule list should match dummy data", dummySchedules, schedules);
                } finally {
                    latch.countDown();
                }
            });

            // Wait for the asynchronous callback (timeout after 3 seconds)
            boolean callbackInvoked = latch.await(3, TimeUnit.SECONDS);
            assertTrue("Callback should be invoked", callbackInvoked);
        }
    }
}
