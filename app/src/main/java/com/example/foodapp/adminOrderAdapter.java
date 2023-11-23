package com.example.foodapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class adminOrderAdapter extends RecyclerView.Adapter<adminOrderAdapter.MyViewHolder>{

    OnItemLongClickListener onItemLongClickListener;
    Context context;
    ArrayList<adminOrderDomain>list;

    public adminOrderAdapter(Context context, ArrayList<adminOrderDomain> list,OnItemLongClickListener onItemLongClickListener) {
        this.context = context;
        this.list = list;
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.adminorder,parent,false);
        return new MyViewHolder(v, onItemLongClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        adminOrderDomain adminOrderDomain=list.get(position);
        holder.itemName.setText(adminOrderDomain.getItemName());
        holder.status.setText(adminOrderDomain.getStatus());
        holder.totalPrice.setText(String.valueOf(adminOrderDomain.getTotalPrice()));
        holder.date.setText(adminOrderDomain.getFormattedDate());
        holder.count.setText(String.valueOf(adminOrderDomain.getCount()));

        String status = adminOrderDomain.getStatus();
        int color;
        if ("Accepted".equals(status) || "Delivery".equals(status)) {
            // Set text color to green for Accepted and Delivery statuses
            color = ContextCompat.getColor(context, android.R.color.holo_green_dark); // Use your green color resource
        } else if ("Your Food is Cooking".equals(status) || "Your Food is Cooked".equals(status)) {
            // Set text color to yellow for specific statuses
            color = ContextCompat.getColor(context, R.color.yellow); // Use your yellow color resource
        } else if ("Rejected".equals(status)) {
            // Set text color to red for Rejected status
            color = ContextCompat.getColor(context, android.R.color.holo_red_dark); // Use your red color resource
        } else {
            // Set text color to default color for other statuses
            color = ContextCompat.getColor(context, android.R.color.black); // Use default text color
        }
        holder.status.setTextColor(color);


    }

    @Override
    public int getItemCount() {
        return list.size();
                }

//public static class MyViewHolder extends RecyclerView.ViewHolder{
//
//    TextView itemName,status,count,totalPrice,date;
//    public MyViewHolder(@NonNull View itemView) {
//        super(itemView);
//
//        itemName=itemView.findViewById(R.id.itemName);
//        status=itemView.findViewById(R.id.status);
//        count=itemView.findViewById(R.id.quantity);
//        totalPrice=itemView.findViewById(R.id.price);
//        date = itemView.findViewById(R.id.date);
//    }
//}

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        TextView itemName, status, count, totalPrice, date;
        OnItemLongClickListener onItemLongClickListener;

        public MyViewHolder(@NonNull View itemView, adminOrderAdapter.OnItemLongClickListener onItemLongClickListener) {
            super(itemView);

            this.onItemLongClickListener = onItemLongClickListener;

            itemName = itemView.findViewById(R.id.itemName);
            status = itemView.findViewById(R.id.status);
            count = itemView.findViewById(R.id.quantity);
            totalPrice = itemView.findViewById(R.id.price);
            date = itemView.findViewById(R.id.date);

            // Set the long click listener for the itemView
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            // Handle the long click event and show options
            if (onItemLongClickListener != null) {
                onItemLongClickListener.onItemLongClick(getAdapterPosition());
                return true; // consume the long click event
            }
            return false;
        }
    }
    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }


}
