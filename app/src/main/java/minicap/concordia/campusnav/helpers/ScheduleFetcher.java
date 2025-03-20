package minicap.concordia.campusnav.helpers;

import android.os.AsyncTask;
import java.util.List;

import minicap.concordia.campusnav.components.ShuttleSchedule;

public class ScheduleFetcher {

    public interface ScheduleFetchListener {
        void onScheduleFetched(List<ShuttleSchedule> schedules);
    }

    public static void fetch(ScheduleFetchListener listener) {
        new FetchScheduleTask(listener).execute();
    }

    private static class FetchScheduleTask extends AsyncTask<Void, Void, List<ShuttleSchedule>> {
        private ScheduleFetchListener listener;

        public FetchScheduleTask(ScheduleFetchListener listener) {
            this.listener = listener;
        }

        @Override
        protected List<ShuttleSchedule> doInBackground(Void... voids) {
            return ShuttleScraper.fetchSchedule();
        }

        @Override
        protected void onPostExecute(List<ShuttleSchedule> schedules) {
            if (listener != null) {
                listener.onScheduleFetched(schedules);
            }
        }
    }
}