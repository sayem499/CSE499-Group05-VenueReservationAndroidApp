package com.reservation.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reservation.app.R;
import com.reservation.app.databinding.VenueListItemBinding;
import com.reservation.app.model.Venue;
import com.squareup.picasso.Picasso;


import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SearchRecyclerAdapter  extends RecyclerView.Adapter<SearchRecyclerAdapter.SearchRecyclerHolder> {
    private  List<Venue> venueList;
    private  Context mContext;

    public SearchRecyclerAdapter( Context context){

        mContext = context;
    }

    @NonNull
    @NotNull
    @Override
    public SearchRecyclerAdapter.SearchRecyclerHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        SearchRecyclerHolder listHolder = new SearchRecyclerHolder(VenueListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

        return listHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SearchRecyclerAdapter.SearchRecyclerHolder holder, int position) {
        if(venueList != null) {
            Venue venue = venueList.get(position);
            holder.binding.title.setText(venue.getName());
            holder.binding.address.setText(venue.getAddress().getDisplayAddressForList());
            holder.binding.price.setText(venue.getPrice());
            holder.binding.capacity.setText(venue.getFormattedSeatCapacity());
            holder.binding.shortDescription.setText(venue.getDescription());

            Picasso.get()
                    .load(venue.getPhotoUrls().get(0))
                    .placeholder(R.drawable.progress_indicator)
                    .error(R.drawable.placeholder1)
                    .into(holder.binding.photo);
        }
    }

    public void setVenueList(List<Venue> data){
        venueList = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(venueList != null){
            return venueList.size();
        }
        else
            return 0;
    }


    protected static class SearchRecyclerHolder extends RecyclerView.ViewHolder{
        VenueListItemBinding binding;
        public SearchRecyclerHolder(VenueListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}