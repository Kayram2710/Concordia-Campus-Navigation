package minicap.concordia.campusnav.components;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.buildingmanager.entities.Building;

public class BuildingAdapter extends RecyclerView.Adapter<BuildingAdapter.BuildingViewHolder> {

    private List<Building> buildingList;

    /**
     * Callback interface for building item clicks.
     */
    public interface OnBuildingClickListener {
        void onBuildingClick(Building building);
    }

    /**
     * Provide a listener that can be set from outside.
     * The fragment or activity sets this to get notified of clicks.
     */
    private OnBuildingClickListener onBuildingClickListener;

    public BuildingAdapter(List<Building> buildingList) {
        this.buildingList = buildingList;
    }

    /**
     * Allows external code (e.g., a fragment) to set the click listener.
     */
    public void setOnBuildingClickListener(OnBuildingClickListener listener) {
        this.onBuildingClickListener = listener;
    }

    @NonNull
    @Override
    public BuildingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each building item (ensure this matches your layout file)
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_building, parent, false);
        return new BuildingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BuildingViewHolder holder, int position) {
        Building building = buildingList.get(position);
        holder.tvBuildingName.setText(building.getBuildingName());

        // Attach a click listener to the entire row
        holder.itemView.setOnClickListener(v -> {
            if (onBuildingClickListener != null) {
                onBuildingClickListener.onBuildingClick(building);
            }
        });
    }

    @Override
    public int getItemCount() {
        return buildingList.size();
    }

    public void updateBuildings(List<Building> newList) {
        this.buildingList = newList;
        notifyDataSetChanged();
    }

    static class BuildingViewHolder extends RecyclerView.ViewHolder {
        TextView tvBuildingName;

        public BuildingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBuildingName = itemView.findViewById(R.id.tvBuildingName);
        }
    }
}
