package com.sururiana.bukatoko.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sururiana.bukatoko.R;
import com.sururiana.bukatoko.data.model.Notification;

import java.util.List;

public class NotifAdapter extends RecyclerView.Adapter<NotifAdapter.ViewHolder> {

    private List<Notification.Data> notifications;
    Context context;

    public NotifAdapter (Context context, List<Notification.Data> notifications){
        this.context = context;
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_notif, viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        Notification.Data data = notifications.get(i);


        viewHolder.txtMsg.setText( data.getMessage() );
        viewHolder.txtDate.setText( data.getDate() );
    }

    @Override
    public int getItemCount() {
        return notifications.size();
//        return notifications !=null ? notifications.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtMsg, txtDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtMsg = itemView.findViewById(R.id.txtMsg);
            txtDate = itemView.findViewById(R.id.txtDate);

        }
    }
}