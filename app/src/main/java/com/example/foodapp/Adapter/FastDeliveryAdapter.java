package com.example.foodapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.R;
import com.example.foodapp.fastDeliveryDomain;
import com.example.foodapp.item_details; // Import the item_details activity

import java.util.ArrayList;

public class FastDeliveryAdapter extends RecyclerView.Adapter<FastDeliveryAdapter.MyViewHolder> {

    Context context;
    ArrayList<fastDeliveryDomain> list;

    public FastDeliveryAdapter(Context context, ArrayList<fastDeliveryDomain> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.viewholder_fast_delivery, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        fastDeliveryDomain fastDeliveryDomain = list.get(position);
        holder.title.setText(fastDeliveryDomain.getItemName());
        holder.des.setText(fastDeliveryDomain.getItemDescription());
        holder.price.setText(fastDeliveryDomain.getItemPrice());

        // Set an OnClickListener for the card view
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch the item_details activity
                Intent intent = new Intent(context, item_details.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, des, price;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            des = itemView.findViewById(R.id.des);
            price = itemView.findViewById(R.id.price);
        }
    }
}
