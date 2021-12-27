package com.reservation.app.ui.venue.booking.adapter;

import static com.reservation.app.util.DateTimeUtils.dateToStr;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.reservation.app.R;
import com.reservation.app.databinding.BookingLilstItemBinding;
import com.reservation.app.model.BookingInfo;
import com.reservation.app.model.Venue;
import com.reservation.app.ui.helper.ItemClickListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Fatema
 * @since 12/15/21
 */
public class BookingListAdapter extends RecyclerView.Adapter<BookingListAdapter.ViewHolder> {

    private final List<BookingInfo> bookingList;
    private ItemClickListener<Venue> itemClickListener;

    public BookingListAdapter(List<BookingInfo> items) {
        bookingList = items;
    }

    public void updateList(List<BookingInfo> items) {
        bookingList.clear();
        bookingList.addAll(items);
        notifyDataSetChanged();
    }

    @NotNull
    @Override
    public BookingListAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        BookingLilstItemBinding view = BookingLilstItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new BookingListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull final BookingListAdapter.ViewHolder holder, int position) {
        BookingInfo bookingInfo = bookingList.get(position);
        holder.binding.title.setText(bookingInfo.getTitle());

        holder.binding.bookingDate.setText(dateToStr(bookingInfo.getDate()) + ", "+ bookingInfo.getSlot());

        String photoPath = bookingInfo.getPhotoUrl();

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
        return bookingList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        BookingLilstItemBinding binding;

        public ViewHolder(BookingLilstItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
