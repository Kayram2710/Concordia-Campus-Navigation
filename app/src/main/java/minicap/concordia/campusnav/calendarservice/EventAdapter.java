package minicap.concordia.campusnav.CalendarService;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.screens.MapsActivity;
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<EventItem> eventList;

    public EventAdapter(List<EventItem> eventList) {
        this.eventList = eventList;
    }

    /**
     * Sets the data used by the adapter
     * @param newData New list of EventItem that is displayed by the adapter
     */
    public void setData(List<EventItem> newData) {
        long now = System.currentTimeMillis();
        List<EventItem> filteredList = new ArrayList<>();
        for (EventItem item : newData) {
            // Only keep ongoing or future events
            if (item.getEndTime().getValue() >= now) {
                filteredList.add(item);
            }
        }
        this.eventList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Use our custom item_event.xml
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        EventItem item = eventList.get(position);

        // Format dates
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
        Date startDate = new Date(item.getStartTime().getValue());
        Date endDate = new Date(item.getEndTime().getValue());

        String formattedDate = dateFormat.format(startDate);
        String formattedTimeRange = timeFormat.format(startDate) + " – " + timeFormat.format(endDate);

        // Populate text fields
        holder.titleText.setText(item.getTitle());
        holder.timeText.setText("📅 " + formattedDate + "   🕒 " + formattedTimeRange);
        holder.locationText.setText("📍 " + item.getLocation());

        // Default background
        holder.itemView.setBackgroundColor(0xFFFFFFFF);
        long now = System.currentTimeMillis();

        // Ongoing class
        if (now >= startDate.getTime() && now <= endDate.getTime()) {
            holder.itemView.setBackgroundColor(0xFFDFF0D8); // light green
        }
        // Next upcoming class
        else if (isNextUpcomingEvent(position, now)) {
            holder.itemView.setBackgroundColor(0xFFFFE5E5); // light red
        }

        // "Go to class" button → open MapsActivity if there is a location, if not we hide the icon
        String location = item.getLocation();
        if (location.equals("No location")){
           holder.goToClassIV.setVisibility(View.GONE);
        } else {
            holder.goToClassIV.setVisibility(View.VISIBLE);
        holder.goToClassIV.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), MapsActivity.class);
            // Pass the event's address string
            intent.putExtra("EVENT_ADDRESS", item.getLocation());
            v.getContext().startActivity(intent);
        });}
    }

    /**
     * Determines which event comes next in the schedule
     * @param position The position of the item in the adapter
     * @param now Current time in Milliseconds
     * @return True if it's the next event, false otherwise
     */
    private boolean isNextUpcomingEvent(int position, long now) {
        for (int i = 0; i < eventList.size(); i++) {
            Date futureStart = new Date(eventList.get(i).getStartTime().getValue());
            if (futureStart.getTime() > now) {
                return i == position;
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView titleText;
        TextView timeText;
        TextView locationText;
        ImageView goToClassIV;

        /**
         * Gets The UI element for the titleText
         * @return TextView for title
         */
        public TextView getTitleText() {
            return titleText;
        }

        /**
         * Gets the UI element for the event's time
         * @return TextView for time
         */
        public TextView getTimeText() {
            return timeText;
        }

        /**
         * Gets the UI element for the event's location
         * @return TextView for the event's location
         */
        public TextView getLocationText() {
            return locationText;
        }

        /**
         * Gets the UI element for the event's "Go to class" image view
         * @return ImageView for "Go to class"
         */
        public ImageView getGoToClassIV() {
            return goToClassIV;
        }

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.titleText);
            timeText = itemView.findViewById(R.id.timeText);
            locationText = itemView.findViewById(R.id.locationText);
            goToClassIV = itemView.findViewById(R.id.goToClassIcon);
        }
    }
}
