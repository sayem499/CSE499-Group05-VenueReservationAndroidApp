package com.reservation.app.ui.venue.list.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.reservation.app.R;
import com.reservation.app.databinding.VenueListItemBinding;
import com.reservation.app.model.Venue;
import com.reservation.app.ui.helper.ItemClickListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Fatema
 * since 8/25/21.
 */
public class VenueAdapter extends RecyclerView.Adapter<VenueAdapter.ViewHolder> {

    private final List<Venue> venues;
    private ItemClickListener<Venue> itemClickListener;

    public VenueAdapter(List<Venue> items) {
        venues = items;
    }

    public void updateList(List<Venue> items) {
        venues.clear();
        venues.addAll(items);
        notifyDataSetChanged();
    }

    public void setItemClickListener(ItemClickListener<Venue> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        VenueListItemBinding view = VenueListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(view, venues, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NotNull final ViewHolder holder, int position) {
        Venue venue = venues.get(position);
        holder.binding.title.setText(venue.getName());
        holder.binding.address.setText(venue.getAddress().getDisplayAddressForList());
        holder.binding.price.setText(venue.getPrice());
        holder.binding.capacity.setText(venue.getFormattedSeatCapacity());
        holder.binding.shortDescription.setText(venue.getDescription());

        String photoPath = venue.getPhotoUrls().get(0);

        if (!TextUtils.isEmpty(photoPath)) {
            Picasso.get()
                    .load(photoPath)
                    .placeholder(R.drawable.progress_indicator)
                    .error(R.drawable.placeholder1)
                    .into(holder.binding.photo);
        }
    }

    @Override
    public int getItemCount() {
        return venues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        VenueListItemBinding binding;

        public ViewHolder(VenueListItemBinding binding, List<Venue> venues, ItemClickListener<Venue> itemClickListener) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(v -> {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(venues.get(getAbsoluteAdapterPosition()));
                }
            });
        }
    }
}