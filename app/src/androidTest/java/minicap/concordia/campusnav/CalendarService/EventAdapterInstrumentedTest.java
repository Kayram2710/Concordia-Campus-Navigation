package minicap.concordia.campusnav.CalendarService;

import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.google.api.client.util.DateTime;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.screens.MapsActivity;

/**
 * This instrumented test verifies the behavior of the EventAdapter.
 *
 * The production EventItem class is defined as:
 *
 *   public class EventItem {
 *       private final String title;
 *       private final String location;
 *       private final DateTime startTime;
 *       private final DateTime endTime;
 *
 *       public EventItem(String title, String location, DateTime startTime, DateTime endTime) {
 *           this.title = title;
 *           this.location = location;
 *           this.startTime = startTime;
 *           this.endTime = endTime;
 *       }
 *
 *       public String getTitle() { return title; }
 *       public String getLocation() { return location; }
 *       public DateTime getStartTime() { return startTime; }
 *       public DateTime getEndTime() { return endTime; }
 *   }
 *
 * We define a FakeEventItem subclass below to simplify test creation.
 */
@RunWith(AndroidJUnit4.class)
public class EventAdapterInstrumentedTest {

    private Context context;

    // A minimal Activity to host inflated views.
    public static class TestActivity extends Activity { }

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    /**
     * FakeEventItem is a convenience subclass of the production EventItem.
     * It passes a title, location, and two DateTime objects (constructed from millisecond values)
     * to the superclass constructor.
     */
    private static class FakeEventItem extends EventItem {
        public FakeEventItem(String title, String location, long startMillis, long endMillis) {
            super(title, location, new DateTime(startMillis), new DateTime(endMillis));
        }
    }

    @Test
    public void testSetDataFiltersPastEvents() {
        long now = System.currentTimeMillis();
        // Create one event that ended in the past and one that is in the future.
        FakeEventItem pastEvent = new FakeEventItem("Past Event", "Past Location", now - 5000, now - 1000);
        FakeEventItem futureEvent = new FakeEventItem("Future Event", "Future Location", now + 1000, now + 5000);
        List<EventItem> inputList = new ArrayList<>();
        inputList.add(pastEvent);
        inputList.add(futureEvent);

        // Launch a TestActivity to run UI code.
        ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class);
        scenario.onActivity(activity -> {
            EventAdapter adapter = new EventAdapter(new ArrayList<>());
            adapter.setData(inputList);
            // Only the future event should be kept.
            assertEquals(1, adapter.getItemCount());
        });
    }

    @Test
    public void testOnBindViewHolderOngoingEvent() {
        long now = System.currentTimeMillis();
        // Create an event that is ongoing: started before now and ending after now.
        FakeEventItem ongoingEvent = new FakeEventItem("Ongoing Event", "Ongoing Location", now - 1000, now + 1000);
        List<EventItem> list = new ArrayList<>();
        list.add(ongoingEvent);

        ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class);
        scenario.onActivity(activity -> {
            EventAdapter adapter = new EventAdapter(list);
            // Inflate the view.
            LinearLayout parent = new LinearLayout(activity);
            RecyclerView.ViewHolder vh = adapter.onCreateViewHolder(parent, 0);
            EventAdapter.EventViewHolder holder = (EventAdapter.EventViewHolder) vh;
            adapter.onBindViewHolder(holder, 0);

            // Verify that text fields are populated correctly.
            assertEquals("Ongoing Event", holder.titleText.getText().toString());
            String timeText = holder.timeText.getText().toString();
            // Check that the formatted time contains the expected emojis.
            assertTrue(timeText.contains("📅"));
            assertTrue(timeText.contains("🕒"));
            assertEquals("📍 Ongoing Location", holder.locationText.getText().toString());

            // Ongoing events should have a light green background (0xFFDFF0D8).
            ColorDrawable background = (ColorDrawable) holder.itemView.getBackground();
            assertEquals(0xFFDFF0D8, background.getColor());
        });
    }

    @Test
    public void testOnBindViewHolderNextUpcomingEvent() {
        long now = System.currentTimeMillis();
        // Create two future events.
        FakeEventItem firstUpcoming = new FakeEventItem("First Upcoming", "Location1", now + 2000, now + 4000);
        FakeEventItem secondUpcoming = new FakeEventItem("Second Upcoming", "Location2", now + 5000, now + 7000);
        List<EventItem> list = new ArrayList<>();
        list.add(firstUpcoming);
        list.add(secondUpcoming);

        ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class);
        scenario.onActivity(activity -> {
            EventAdapter adapter = new EventAdapter(list);
            LinearLayout parent = new LinearLayout(activity);

            // The very next upcoming event (position 0) should be highlighted with light red (0xFFFFE5E5).
            EventAdapter.EventViewHolder holder0 = (EventAdapter.EventViewHolder) adapter.onCreateViewHolder(parent, 0);
            adapter.onBindViewHolder(holder0, 0);
            ColorDrawable background0 = (ColorDrawable) holder0.itemView.getBackground();
            assertEquals(0xFFFFE5E5, background0.getColor());

            // The second upcoming event (position 1) should retain the default white background (0xFFFFFFFF).
            EventAdapter.EventViewHolder holder1 = (EventAdapter.EventViewHolder) adapter.onCreateViewHolder(parent, 0);
            adapter.onBindViewHolder(holder1, 1);
            ColorDrawable background1 = (ColorDrawable) holder1.itemView.getBackground();
            assertEquals(0xFFFFFFFF, background1.getColor());
        });
    }

    @Test
    public void testOnBindViewHolderDefaultBackgroundForNonSpecialEvent() {
        long now = System.currentTimeMillis();
        // Create an ongoing event and two future events.
        FakeEventItem ongoingEvent = new FakeEventItem("Ongoing", "Loc1", now - 2000, now + 2000);
        FakeEventItem nextUpcoming = new FakeEventItem("Next Upcoming", "Loc2", now + 3000, now + 5000);
        FakeEventItem laterEvent = new FakeEventItem("Later", "Loc3", now + 6000, now + 8000);
        List<EventItem> list = new ArrayList<>();
        list.add(ongoingEvent);
        list.add(nextUpcoming);
        list.add(laterEvent);

        ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class);
        scenario.onActivity(activity -> {
            EventAdapter adapter = new EventAdapter(list);
            LinearLayout parent = new LinearLayout(activity);
            // Bind the later event (position 2) which should have the default white background.
            EventAdapter.EventViewHolder holder = (EventAdapter.EventViewHolder) adapter.onCreateViewHolder(parent, 0);
            adapter.onBindViewHolder(holder, 2);
            ColorDrawable background = (ColorDrawable) holder.itemView.getBackground();
            assertEquals(0xFFFFFFFF, background.getColor());
        });
    }

    @Test
    public void testGoToClassButtonLaunchesMapsActivity() {
        long now = System.currentTimeMillis();
        // Create a future event.
        FakeEventItem event = new FakeEventItem("Test Event", "Test Location", now + 2000, now + 4000);
        List<EventItem> list = new ArrayList<>();
        list.add(event);

        ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class);
        scenario.onActivity(activity -> {
            EventAdapter adapter = new EventAdapter(list);
            LinearLayout parent = new LinearLayout(activity);
            EventAdapter.EventViewHolder holder = (EventAdapter.EventViewHolder) adapter.onCreateViewHolder(parent, 0);
            adapter.onBindViewHolder(holder, 0);

            // Simulate a click on the "go to class" ImageView.
            holder.goToClassIV.performClick();
            // Verify that an intent is launched targeting MapsActivity with the proper extra.
            intended(hasComponent(MapsActivity.class.getName()));
            intended(hasExtra("EVENT_ADDRESS", "Test Location"));
        });
    }
}
