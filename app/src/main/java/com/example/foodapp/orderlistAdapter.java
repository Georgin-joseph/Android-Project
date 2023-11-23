package com.example.foodapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class orderlistAdapter extends RecyclerView.Adapter<orderlistAdapter.MyViewHolder> {

    Context context;
    ArrayList<orderlistdomin>list;

    public orderlistAdapter(Context context, ArrayList<orderlistdomin> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.orderlist,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        orderlistdomin orderlistdomin=list.get(position);
        holder.itemName.setText(orderlistdomin.getItemName());
        // Convert count to String before setting it to the TextView
        holder.count.setText(String.valueOf(orderlistdomin.getCount()));

        // Set other TextViews
        holder.totalPrice.setText(String.valueOf(orderlistdomin.getTotalPrice()));
        holder.status.setText(orderlistdomin.getStatus());
        holder.date.setText(orderlistdomin.getFormattedDate());

        String status = orderlistdomin.getStatus();
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

    public  static  class MyViewHolder extends RecyclerView.ViewHolder{

        TextView itemName,status,count,totalPrice,date;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName=itemView.findViewById(R.id.item);
            status=itemView.findViewById(R.id.status);
            count=itemView.findViewById(R.id.quantity);
            totalPrice=itemView.findViewById(R.id.price);
            date = itemView.findViewById(R.id.date);
        }
    }
}
