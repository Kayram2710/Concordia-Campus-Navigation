package minicap.concordia.campusnav.helpers;

import android.content.Context;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;

public class GoogleCalendarService {
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private GoogleCalendarService() {}

    /**
     * Gets the Google calendar service
     * @param context The context for who wants the calendar
     * @return Calendar instance for this app
     */
    public static Calendar getCalendarService(Context context) {
        GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(
                context, java.util.Collections.singleton("https://www.googleapis.com/auth/calendar.readonly"));

        return new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName("Campus Navigation App")
                .build();
    }
}
