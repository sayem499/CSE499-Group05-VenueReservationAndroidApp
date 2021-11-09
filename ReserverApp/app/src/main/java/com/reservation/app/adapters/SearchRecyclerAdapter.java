package com.reservation.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reservation.app.R;
import com.reservation.app.databinding.VenueListItemBinding;
import com.reservation.app.model.Venue;
import com.squareup.picasso.Picasso;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SearchRecyclerAdapter  extends RecyclerView.Adapter<SearchRecyclerAdapter.SearchRecyclerHolder> implements Filterable {
    private  List<Venue> venueList;
    private List<Venue> venueListAll;
    private  Context mContext;

    public SearchRecyclerAdapter( Context context,List<Venue> venueList){
        this.venueList = venueList;
        this.venueListAll = new ArrayList<>();
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
        venueList.clear();
        venueListAll.clear();
        venueList.addAll(data);
        venueListAll.addAll(data);
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

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Venue> filteredList = new ArrayList<>();

            if(constraint.toString().isEmpty()){
                filteredList.addAll(venueListAll);
            }else {
                for (Venue venue: venueListAll) {
                    if(venue.getName().toLowerCase().trim().contains(constraint.toString().toLowerCase().trim())){
                        filteredList.add(venue);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            venueList.clear();
            venueList.addAll((Collection<? extends Venue>) results.values);
            notifyDataSetChanged();
        }
    };

    protected static class SearchRecyclerHolder extends RecyclerView.ViewHolder{
        VenueListItemBinding binding;
        public SearchRecyclerHolder(VenueListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}