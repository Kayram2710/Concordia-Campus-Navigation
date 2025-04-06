package minicap.concordia.campusnav.helpers;

import android.os.Looper;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import minicap.concordia.campusnav.components.ShuttleSchedule;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ScheduleFetcherInstrumentedTest {

    @Test
    public void testFetchSchedule() throws InterruptedException {
        // Use a CountDownLatch to wait for the asynchronous callback.
        final CountDownLatch latch = new CountDownLatch(1);
        final List<ShuttleSchedule>[] resultHolder = new List[1];

        // Create a listener that captures the schedules and releases the latch.
        ScheduleFetcher.ScheduleFetchListener listener = new ScheduleFetcher.ScheduleFetchListener() {
            @Override
            public void onScheduleFetched(List<ShuttleSchedule> schedules) {
                resultHolder[0] = schedules;
                latch.countDown();
            }
        };

        // Ensure a Looper exists; ScheduleFetcher uses the main thread's Looper.
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }

        // Optionally, you can initialize any dummy context or resources if needed.
        InstrumentationRegistry.getInstrumentation().getTargetContext();

        // Call the fetch method, which starts the background work.
        ScheduleFetcher.fetch(listener);

        // Wait for the callback with a timeout (5 seconds).
        boolean callbackInvoked = latch.await(5, TimeUnit.SECONDS);
        assertTrue("Callback was not invoked within the timeout", callbackInvoked);
        assertNotNull("The schedule list should not be null", resultHolder[0]);

    }
}
