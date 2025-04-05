package minicap.concordia.campusnav.screens;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ImageButton;
import androidx.fragment.app.FragmentActivity;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import minicap.concordia.campusnav.CalendarService.EventAdapter;
import minicap.concordia.campusnav.CalendarService.EventItem;
import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.components.MainMenuDialog;
import minicap.concordia.campusnav.savedstates.States;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ClassScheduleActivity extends FragmentActivity implements MainMenuDialog.MainMenuListener {

    private static final int RC_SIGN_IN = 100;
    private static final int REQUEST_CALENDAR_PERMISSION = 101;

    private GoogleSignInClient googleSignInClient;
    private EventAdapter eventAdapter;

    private final States states = States.getInstance();

    @Override
    protected void onStart() {
        super.onStart();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            fetchCalendarEvents();
        } else {
            signIn();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        // 1. Setup the RecyclerView
        RecyclerView rvEventList = findViewById(R.id.rv_event_list);
        rvEventList.setLayoutManager(new LinearLayoutManager(this));

        // 2. Create an empty adapter initially
        List<EventItem> emptyList = new ArrayList<>();
        EventAdapter eventAdapter = new EventAdapter(emptyList);
        rvEventList.setAdapter(eventAdapter);

        // Keep a reference so we can update it after fetching
        this.eventAdapter = eventAdapter;

        // Main Menu dialog
        ImageButton menuButton = findViewById(R.id.button_menu);
        menuButton.setOnClickListener(v -> showMainMenuDialog());

        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new com.google.android.gms.common.api.Scope("https://www.googleapis.com/auth/calendar.readonly"))
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);


        // "Import" button to handle Google Calendar import
        Button importButton = findViewById(R.id.button_import_calendar);
        importButton.setOnClickListener(v -> signIn());

        // "Select Calendar" button to let user pick a different calendar
        Button selectCalendarButton = findViewById(R.id.button_select_calendar);
        selectCalendarButton.setOnClickListener(v -> {
            // Must ensure user is signed in & has calendar permission first
            if (GoogleSignIn.getLastSignedInAccount(this) == null) {
                Toast.makeText(this, "Please Sign-In first.", Toast.LENGTH_SHORT).show();
            } else {
                // Make sure we have READ_CALENDAR permission
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_CALENDAR}, REQUEST_CALENDAR_PERMISSION);
                } else {
                    showCalendarSelectionDialog(); // showing the list of user calendars
                }
            }
        });
    }

    public void showMainMenuDialog() {
        if (!states.isMenuOpen()) {
            MainMenuDialog dialog = new MainMenuDialog(this);
            dialog.show();
        }
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Toast.makeText(this, "Google Sign-In Success", Toast.LENGTH_SHORT).show();
                requestCalendarPermission();
            } catch (ApiException e) {
                Toast.makeText(this, "Google Sign-In failed", Toast.LENGTH_SHORT).show();
                int code = e.getStatusCode();
                Log.e("GOOGLE_SIGN_IN", "Sign-in failed. Code: " + code, e);
                Toast.makeText(this, "Google Sign-In failed: Code " + code, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void requestCalendarPermission() {
        // we are requesting the permission if it is not granted yet
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CALENDAR}, REQUEST_CALENDAR_PERMISSION);
        } else {
            // if already granted, we fetch the events from whichever calendar is selected
            fetchCalendarEvents();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALENDAR_PERMISSION && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Calendar permission granted!", Toast.LENGTH_SHORT).show();
            // we fetch the calendar events once permission is granted
            fetchCalendarEvents();
        } else {
            Toast.makeText(this, "Calendar permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Show the user a list of all their calendars, let them pick one,
     * then store the selection in States and fetch events again.
     */
    private void showCalendarSelectionDialog() {
        new Thread(() -> {
            try {
                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
                if (account == null) {
                    runOnUiThread(() ->
                            Toast.makeText(this, "Not signed in!", Toast.LENGTH_SHORT).show()
                    );
                    return;
                }

                // Build credential
                GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(
                        this,
                        Collections.singleton(CalendarScopes.CALENDAR_READONLY)
                );
                credential.setSelectedAccount(account.getAccount());

                // Create the Calendar service
                com.google.api.services.calendar.Calendar service =
                        new com.google.api.services.calendar.Calendar.Builder(
                                new NetHttpTransport(),
                                GsonFactory.getDefaultInstance(),
                                credential
                        )
                                .setApplicationName("CampusNav")
                                .build();

                // Get list of calendars
                CalendarList calendarList = service.calendarList().list().execute();
                List<CalendarListEntry> calendarEntries = calendarList.getItems();

                if (calendarEntries == null || calendarEntries.isEmpty()) {
                    runOnUiThread(() ->
                            Toast.makeText(this, "No calendars found.", Toast.LENGTH_SHORT).show()
                    );
                    return;
                }

                // Build arrays for calendar names & IDs
                String[] calendarNames = new String[calendarEntries.size()];
                String[] calendarIds = new String[calendarEntries.size()];

                for (int i = 0; i < calendarEntries.size(); i++) {
                    CalendarListEntry entry = calendarEntries.get(i);
                    calendarNames[i] = entry.getSummary();
                    calendarIds[i] = entry.getId();
                }

                runOnUiThread(() -> {
                    new androidx.appcompat.app.AlertDialog.Builder(this)
                            .setTitle("Choose a calendar")
                            .setItems(calendarNames, (dialog, which) -> {
                                // Store in States
                                states.setSelectedCalendarId(calendarIds[which]);
                                Toast.makeText(this,
                                        "Selected: " + calendarNames[which],
                                        Toast.LENGTH_SHORT).show();
                                fetchCalendarEvents();
                            })
                            .show();
                });

            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(this, "Error fetching calendar list: " + e.getMessage(),
                                Toast.LENGTH_LONG).show()
                );
            }
        }).start();
    }

    private void fetchCalendarEvents() {
        // Show a quick toast or progress
        Toast.makeText(this, "Fetching calendar events...", Toast.LENGTH_SHORT).show();

        new Thread(() -> {
            try {
                // 1. Check if user is signed in
                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
                if (account == null) {
                    runOnUiThread(() ->
                            Toast.makeText(this, "Not signed in!", Toast.LENGTH_SHORT).show()
                    );
                    return;
                }

                // 2. Build GoogleAccountCredential with the calendar scope
                GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(
                        this,
                        Collections.singleton(CalendarScopes.CALENDAR_READONLY)
                );
                credential.setSelectedAccount(account.getAccount());

                // 3. Create the Calendar service
                com.google.api.services.calendar.Calendar service =
                        new com.google.api.services.calendar.Calendar.Builder(
                                new NetHttpTransport(),
                                GsonFactory.getDefaultInstance(),
                                credential
                        )
                                .setApplicationName("CampusNav")
                                .build();

                // ADDED: Use the selected calendar ID if available, otherwise "primary"
                String calendarId = states.getSelectedCalendarId();
                if (calendarId == null) {
                    calendarId = "primary";
                }

                // 4. Fetch events from that calendar
                Events events = service.events().list(calendarId)
                        .setMaxResults(10)
                        .setTimeMin(new DateTime(System.currentTimeMillis()))
                        .setOrderBy("startTime")
                        .setSingleEvents(true)
                        .execute();

                List<Event> items = events.getItems();
                if (items == null || items.isEmpty()) {
                    runOnUiThread(() ->
                            Toast.makeText(this, "No upcoming events found.",
                                    Toast.LENGTH_SHORT).show()
                    );
                    return;
                }

                // 5. Convert them to EventItem objects
                List<EventItem> eventItemList = new ArrayList<>();
                for (Event event : items) {
                    String title = event.getSummary() != null ?
                            event.getSummary() : "Untitled";
                    String location = event.getLocation() != null ?
                            event.getLocation() : "No location";

                    // Handle date/time
                    DateTime start = event.getStart().getDateTime();
                    DateTime end   = event.getEnd().getDateTime();
                    if (start == null) {
                        start = event.getStart().getDate();
                    }
                    if (end == null) {
                        end = event.getEnd().getDate();
                    }

                    eventItemList.add(new EventItem(title, location, start, end));
                }

                // 6. Update the RecyclerView on the main thread
                runOnUiThread(() -> {
                    if (eventAdapter != null) {
                        eventAdapter.setData(eventItemList);
                    }
                });

            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(this, "Error fetching events: " + e.getMessage(),
                                Toast.LENGTH_LONG).show()
                );
            }
        }).start();
    }

    /**
     * Example method to format Google's DateTime object
     */
    private String formatDateTime(DateTime dateTime) {
        if (dateTime == null) return "";
        // dateTime.toStringRfc3339() will return "2025-03-23T13:00:00.000Z"
        return dateTime.toStringRfc3339();
    }

}
