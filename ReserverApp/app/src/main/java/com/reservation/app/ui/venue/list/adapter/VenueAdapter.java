package com.reservation.app.ui.venue.list.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.reservation.app.databinding.VenueListItemBinding;
import com.reservation.app.model.Venue;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Fatema
 * since 8/25/21.
 */
public class VenueAdapter extends RecyclerView.Adapter<VenueAdapter.ViewHolder> {

    private final List<Venue> values;

    public VenueAdapter(List<Venue> items) {
        values = items;
    }

    public void updateList(List<Venue> items) {
        values.clear();
        values.addAll(items);
        notifyDataSetChanged();
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(VenueListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NotNull final ViewHolder holder, int position) {
        Venue venue = values.get(position);
        holder.title.setText(venue.getName());
        holder.address.setText(venue.getAddress().getDisplayAddressForList());
        holder.price.setText(venue.getPrice());
        holder.capacity.setText(venue.getFormattedSeatCapacity());
        holder.description.setText(venue.getDescription());

        //Photo will be bind here with the 3rd party library
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView photo;
        TextView title;
        TextView address;
        TextView price;
        TextView capacity;
        TextView description;

        public ViewHolder(VenueListItemBinding binding) {
            super(binding.getRoot());
        }
    }
}