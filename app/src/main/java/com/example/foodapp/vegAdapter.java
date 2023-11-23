package com.example.foodapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class vegAdapter extends RecyclerView.Adapter<vegAdapter.MyViewHolder> {

    Context context;
    ArrayList<vegDomain>list;

    public vegAdapter(Context context, ArrayList<vegDomain> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.viewholder_fast_delivery, parent, false);
        return new vegAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        vegDomain vegDomain=list.get(position);
        holder.title.setText(vegDomain.getItemName());
        holder.des.setText(vegDomain.getItemDescription());
        holder.price.setText(vegDomain.getItemPrice());

        String documentId = vegDomain.getItemId();
        String imageUrl = vegDomain.getImageUrl();

        // Load and display the image using Glide
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.fast_1) // Optional placeholder image
                .into(holder.pic);

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
