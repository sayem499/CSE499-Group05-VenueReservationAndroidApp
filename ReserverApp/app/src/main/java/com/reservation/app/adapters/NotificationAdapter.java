package com.reservation.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reservation.app.R;
import com.reservation.app.databinding.NotificationListBinding;
import com.reservation.app.datasource.NotificationModel;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationHolder>{

    private List<NotificationModel> notificationList;
    private Context mContext;

    public NotificationAdapter(List<NotificationModel> notificationList, Context mContext) {
        this.notificationList = notificationList;
        this.mContext = mContext;
    }

    @NonNull
    @NotNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        NotificationHolder notificationHolder = new NotificationHolder(NotificationListBinding.inflate(LayoutInflater.from(mContext),parent,false));

        return notificationHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NotificationHolder holder, int position) {

        if(notificationList != null){
            NotificationModel notificationModel = notificationList.get(position);
            holder.binding.notificationTitleTextView.setText(notificationModel.getNotificationTitle());
            holder.binding.notificationBodyTextView.setText(notificationModel.getNotificationMessage());
            if(!notificationModel.getNotificationImageUrl().isEmpty()) {
                holder.binding.notificationImageView.setVisibility(View.VISIBLE);
                Picasso.get()
                        .load(notificationModel.getNotificationImageUrl())
                        .placeholder(R.drawable.progress_indicator)
                        .error(R.drawable.placeholder1)
                        .into(holder.binding.notificationImageView);
            }else
                holder.binding.notificationImageView.setVisibility(View.GONE);
        }

    }

    public void setNotificationList(List<NotificationModel> data){
        notificationList.clear();
        notificationList.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(notificationList != null){
            return notificationList.size();
        }
        return 0;
    }

    public static class NotificationHolder extends RecyclerView.ViewHolder {
        NotificationListBinding binding;
        public NotificationHolder(@NonNull @NotNull NotificationListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
