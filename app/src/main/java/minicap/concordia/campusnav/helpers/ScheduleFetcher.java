package minicap.concordia.campusnav.helpers;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.os.Handler;
import android.os.Looper;

import minicap.concordia.campusnav.components.ShuttleSchedule;

// Handles async schedule fetching
public class ScheduleFetcher {
    // Callback interface for async fetch completion
    public interface ScheduleFetchListener {
        void onScheduleFetched(List<ShuttleSchedule> schedules);
    }
    // Fetches schedule in background thread
    public static void fetch(ScheduleFetchListener listener) {
        ExecutorService executor = Executors.newSingleThreadExecutor(); // Uses ExecutorService for threading
        Handler handler = new Handler(Looper.getMainLooper()); // Handler for main thread callback

        executor.execute(() -> {
            // Background work: Fetch the schedule
            List<ShuttleSchedule> schedules = ShuttleScraper.fetchSchedule();

            // Update the UI on the main thread
            handler.post(() -> listener.onScheduleFetched(schedules));
        });
    }
}