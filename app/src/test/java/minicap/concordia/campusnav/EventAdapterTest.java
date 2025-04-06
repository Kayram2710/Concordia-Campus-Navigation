package minicap.concordia.campusnav;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.api.client.util.DateTime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import java.util.ArrayList;
import java.util.List;

import minicap.concordia.campusnav.CalendarService.EventAdapter;
import minicap.concordia.campusnav.CalendarService.EventItem;
import minicap.concordia.campusnav.screens.MapsActivity;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {33}, manifest = "src/main/AndroidManifest.xml")
public class EventAdapterTest {

    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private Activity activity;

    @Before
    public void setUp() {
        // Build an Activity to provide an Activity context.
        activity = Robolectric.buildActivity(Activity.class).setup().get();
        recyclerView = new RecyclerView(activity);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new EventAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
    }

    @Test
    public void testSetData_filtersPastEvents() {
        long now = System.currentTimeMillis();
        long hour = 60 * 60 * 1000;

        // Create three events: past, ongoing, and future.
        EventItem pastEvent = new EventItem("Past", "Old location",
                new DateTime(now - 3 * hour),
                new DateTime(now - 2 * hour));
        EventItem ongoingEvent = new EventItem("Now", "Current Location",
                new DateTime(now - hour),
                new DateTime(now + hour));
        EventItem futureEvent = new EventItem("Future", "Next Location",
                new DateTime(now + hour),
                new DateTime(now + 2 * hour));

        List<EventItem> items = new ArrayList<>();
        items.add(pastEvent);
        items.add(ongoingEvent);
        items.add(futureEvent);

        adapter.setData(items);

        // Expect only ongoing and future events to remain.
        assertEquals("Adapter should have 2 events after filtering", 2, adapter.getItemCount());
    }

    @Test
    public void testOnBindViewHolder_displaysCorrectText() {
        long now = System.currentTimeMillis();
        long hour = 60 * 60 * 1000;

        EventItem event = new EventItem("Test Event", "FG 134.1",
                new DateTime(now + hour),
                new DateTime(now + 2 * hour));

        List<EventItem> list = new ArrayList<>();
        list.add(event);
        adapter.setData(list);

        EventAdapter.EventViewHolder holder = adapter.onCreateViewHolder(recyclerView, 0);
        adapter.onBindViewHolder(holder, 0);

        assertEquals("Test Event", holder.getTitleText().getText().toString());
        assertTrue(holder.getLocationText().getText().toString().contains("FG 134.1"));
        assertTrue(holder.getTimeText().getText().toString().contains("📅"));
        assertTrue(holder.getTimeText().getText().toString().contains("🕒"));
    }

    @Test
    public void testBackgroundColor_ongoingAndUpcoming() {
        long now = System.currentTimeMillis();
        long hour = 60 * 60 * 1000;

        // Ongoing event: current time is between start and end.
        EventItem ongoing = new EventItem("Now", "Location A",
                new DateTime(now - hour),
                new DateTime(now + hour));
        // Upcoming event: first event starting in the future.
        EventItem upcoming = new EventItem("Future", "Location B",
                new DateTime(now + 2 * hour),
                new DateTime(now + 3 * hour));

        List<EventItem> items = new ArrayList<>();
        items.add(ongoing);
        items.add(upcoming);

        adapter.setData(items);

        // Test ongoing event background color.
        EventAdapter.EventViewHolder ongoingHolder = adapter.onCreateViewHolder(recyclerView, 0);
        adapter.onBindViewHolder(ongoingHolder, 0);
        int ongoingBg = ((ColorDrawable) ongoingHolder.itemView.getBackground()).getColor();
        assertEquals("Ongoing event background should be light green", 0xFFDFF0D8, ongoingBg);

        // Test upcoming event background color.
        EventAdapter.EventViewHolder futureHolder = adapter.onCreateViewHolder(recyclerView, 0);
        adapter.onBindViewHolder(futureHolder, 1);
        int futureBg = ((ColorDrawable) futureHolder.itemView.getBackground()).getColor();
        assertEquals("Upcoming event background should be light red", 0xFFFFE5E5, futureBg);
    }

    @Test
    public void testGoToClassButtonStartsMapsActivity() {
        long now = System.currentTimeMillis();
        long hour = 60 * 60 * 1000;

        EventItem event = new EventItem("Test Event", "123 Main St",
                new DateTime(now + hour),
                new DateTime(now + 2 * hour));

        List<EventItem> list = new ArrayList<>();
        list.add(event);
        adapter.setData(list);

        // Create a ViewHolder and bind the event.
        EventAdapter.EventViewHolder holder = adapter.onCreateViewHolder(recyclerView, 0);
        adapter.onBindViewHolder(holder, 0);

        // Simulate clicking on the "Go to class" icon.
        holder.getGoToClassIV().performClick();

        // Verify that an Intent was started.
        ShadowActivity shadowActivity = Shadows.shadowOf(activity);
        Intent nextStartedIntent = shadowActivity.getNextStartedActivity();
        assertNotNull("An intent should have been started", nextStartedIntent);
        // Verify that the intent targets MapsActivity.
        assertEquals("MapsActivity", nextStartedIntent.getComponent().getShortClassName());
    }
}
