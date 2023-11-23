package com.example.foodapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodapp.Adapter.FastDeliveryAdapter;

import java.util.ArrayList;

public class nonVegAdapter extends RecyclerView.Adapter<nonVegAdapter.MyViewHolder>{

    Context context;
    ArrayList<nonVegDomain>list;

    public nonVegAdapter(Context context, ArrayList<nonVegDomain> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.viewholder_fast_delivery, parent, false);
        return new nonVegAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        nonVegDomain nonVegDomain=list.get(position);
        holder.title.setText(nonVegDomain.getItemName());
        holder.des.setText(nonVegDomain.getItemDescription());
        holder.price.setText(nonVegDomain.getItemPrice());

        String documentId = nonVegDomain.getItemId();
        String imageUrl = nonVegDomain.getImageUrl();

        // Load and display the image using Glide
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.fast_1) // Optional placeholder image
                .into(holder.pic);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch the item_details activity
                Intent intent = new Intent(context, item_details.class);
                intent.putExtra("item_id", documentId);
                context.startActivity(intent);
                Toast.makeText(context, "Item ID: " + documentId, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title, des, price;
        ImageView pic;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            des = itemView.findViewById(R.id.des);
            price = itemView.findViewById(R.id.price);
            pic=itemView.findViewById(R.id.pic);
        }
    }
}
