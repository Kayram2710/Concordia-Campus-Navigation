package minicap.concordia.campusnav;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.api.client.util.DateTime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import minicap.concordia.campusnav.CalendarService.EventAdapter;
import minicap.concordia.campusnav.CalendarService.EventItem;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 33)
public class EventAdapterTest {

    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private Context context;

    @Before
    public void setUp() {
        context = RuntimeEnvironment.getApplication();
        recyclerView = new RecyclerView(context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new EventAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
    }

    @Test
    public void testSetData_filtersPastEvents() {
        long now = System.currentTimeMillis();
        long hour = 60 * 60 * 1000;

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

        assertEquals(2, adapter.getItemCount());
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

        EventItem ongoing = new EventItem("Now", "Location A",
                new DateTime(now - hour),
                new DateTime(now + hour));
        EventItem upcoming = new EventItem("Future", "Location B",
                new DateTime(now + 2 * hour),
                new DateTime(now + 3 * hour));

        List<EventItem> items = new ArrayList<>();
        items.add(ongoing);
        items.add(upcoming);

        adapter.setData(items);

        EventAdapter.EventViewHolder ongoingHolder = adapter.onCreateViewHolder(recyclerView, 0);
        adapter.onBindViewHolder(ongoingHolder, 0);
        int ongoingBg = ((ColorDrawable) ongoingHolder.itemView.getBackground()).getColor();
        assertEquals(0xFFDFF0D8, ongoingBg); // light green color

        EventAdapter.EventViewHolder futureHolder = adapter.onCreateViewHolder(recyclerView, 0);
        adapter.onBindViewHolder(futureHolder, 1);
        int futureBg = ((ColorDrawable) futureHolder.itemView.getBackground()).getColor();
        assertEquals(0xFFFFE5E5, futureBg); // light red color
    }
}
