package minicap.concordia.campusnav.CalendarService;

import static org.junit.Assert.*;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.api.client.util.DateTime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.screens.MapsActivity;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class EventAdapterTest {

    private EventAdapter adapter;

    // Create a subclass of EventItem since it is a class.
    static class TestEventItem extends EventItem {
        public TestEventItem(String title, String location, long startMillis, long endMillis) {
            super(title, location, new DateTime(startMillis), new DateTime(endMillis));
        }
    }

    @Before
    public void setUp() {
        // Initialize adapter with an empty list; data will be provided via setData().
        adapter = new EventAdapter(new ArrayList<>());
    }

    @Test
    public void testSetDataFiltersPastEvents() {
        long now = System.currentTimeMillis();
        // Create a past event (should be filtered out) and a future event.
        EventItem past = new TestEventItem("Past Event", "Past Location", now - 3600000, now - 1800000);
        EventItem future = new TestEventItem("Future Event", "Future Location", now + 600000, now + 1200000);

        List<EventItem> list = new ArrayList<>();
        list.add(past);
        list.add(future);

        adapter.setData(list);
        // Past event should be filtered out; expect 1 event remaining.
        assertEquals("Filtered event count should be 1", 1, adapter.getItemCount());

        // Use onBindViewHolder to check the title of the remaining event.
        ViewGroup parent = new LinearLayout(RuntimeEnvironment.getApplication());
        EventAdapter.EventViewHolder holder = adapter.onCreateViewHolder(parent, 0);
        adapter.onBindViewHolder(holder, 0);
        assertEquals("Future Event", holder.getTitleText().getText().toString());
    }

    @Test
    public void testOnCreateViewHolder() {
        // Create a dummy parent view for inflation.
        ViewGroup parent = new LinearLayout(RuntimeEnvironment.getApplication());
        EventAdapter.EventViewHolder holder = adapter.onCreateViewHolder(parent, 0);
        // Verify that all view references are properly initialized.
        assertNotNull("Title TextView should not be null", holder.getTitleText());
        assertNotNull("Time TextView should not be null", holder.getTimeText());
        assertNotNull("Location TextView should not be null", holder.getLocationText());
        assertNotNull("Go To Class ImageView should not be null", holder.getGoToClassIV());
    }

    @Test
    public void testOnBindViewHolder_OngoingEvent() {
        long now = System.currentTimeMillis();
        // Create an ongoing event: started 10 minutes ago, ends in 10 minutes.
        EventItem ongoingEvent = new TestEventItem("Ongoing Event", "Ongoing Location", now - 600000, now + 600000);
        List<EventItem> list = new ArrayList<>();
        list.add(ongoingEvent);
        adapter.setData(list);

        ViewGroup parent = new LinearLayout(RuntimeEnvironment.getApplication());
        EventAdapter.EventViewHolder holder = adapter.onCreateViewHolder(parent, 0);
        adapter.onBindViewHolder(holder, 0);

        // Verify text values.
        assertEquals("Ongoing Event", holder.getTitleText().getText().toString());
        String timeText = holder.getTimeText().getText().toString();
        assertTrue("Time text should contain calendar emoji", timeText.contains("📅"));
        assertTrue("Location text should contain location emoji", holder.getLocationText().getText().toString().contains("📍"));

        // Verify background color for an ongoing event (light green: 0xFFDFF0D8).
        ColorDrawable drawable = (ColorDrawable) holder.itemView.getBackground();
        int actualColor = drawable.getColor();
        assertEquals("Ongoing event background should be light green", 0xFFDFF0D8, actualColor);
    }

    @Test
    public void testOnBindViewHolder_NextUpcomingEvent() {
        long now = System.currentTimeMillis();
        // Create two upcoming events so that the first is the next upcoming event.
        EventItem upcomingEvent1 = new TestEventItem("Upcoming Event 1", "Location 1", now + 600000, now + 1200000);
        EventItem upcomingEvent2 = new TestEventItem("Upcoming Event 2", "Location 2", now + 1800000, now + 2400000);
        List<EventItem> list = new ArrayList<>();
        list.add(upcomingEvent1);
        list.add(upcomingEvent2);
        adapter.setData(list);

        ViewGroup parent = new LinearLayout(RuntimeEnvironment.getApplication());
        // For position 0, should be the next upcoming event (light red: 0xFFFFE5E5).
        EventAdapter.EventViewHolder holder0 = adapter.onCreateViewHolder(parent, 0);
        adapter.onBindViewHolder(holder0, 0);
        ColorDrawable drawable0 = (ColorDrawable) holder0.itemView.getBackground();
        int colorPos0 = drawable0.getColor();
        assertEquals("Next upcoming event should have light red background", 0xFFFFE5E5, colorPos0);

        // For position 1, the background should remain white (0xFFFFFFFF).
        EventAdapter.EventViewHolder holder1 = adapter.onCreateViewHolder(parent, 0);
        adapter.onBindViewHolder(holder1, 1);
        ColorDrawable drawable1 = (ColorDrawable) holder1.itemView.getBackground();
        int colorPos1 = drawable1.getColor();
        assertEquals("Non-next-upcoming event should have white background", 0xFFFFFFFF, colorPos1);
    }

    @Test
    public void testGoToClassButtonStartsMapsActivity() {
        long now = System.currentTimeMillis();
        // Create an event with a specific location.
        EventItem event = new TestEventItem("Event", "123 Main St", now + 600000, now + 1200000);
        List<EventItem> list = new ArrayList<>();
        list.add(event);
        adapter.setData(list);

        ViewGroup parent = new LinearLayout(RuntimeEnvironment.getApplication());
        EventAdapter.EventViewHolder holder = adapter.onCreateViewHolder(parent, 0);
        adapter.onBindViewHolder(holder, 0);

        // Simulate a click on the "go to class" button.
        holder.getGoToClassIV().performClick();

        // Retrieve the started intent via ShadowApplication.
        ShadowApplication shadowApplication = ShadowApplication.getInstance();
        Intent startedIntent = shadowApplication.getNextStartedActivity();
        assertNotNull("An intent should have been started", startedIntent);
        assertEquals("Intent should target MapsActivity", MapsActivity.class.getName(), startedIntent.getComponent().getClassName());
        assertEquals("Intent extra should contain event location", "123 Main St", startedIntent.getStringExtra("EVENT_ADDRESS"));
    }
}
