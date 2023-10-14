package com.example.foodapp.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.R;
import com.example.foodapp.fastDeliveryDomain;

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
        View v= LayoutInflater.from(context).inflate(R.layout.viewholder_fast_delivery,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        fastDeliveryDomain fastDeliveryDomain=list.get(position);
        holder.title.setText(fastDeliveryDomain.getItemName());
        holder.des.setText(fastDeliveryDomain.getItemDescription());
        holder.price.setText(fastDeliveryDomain.getItemPrice());
//        holder.pic.setImageDrawable(fastDeliveryDomain.getImageUrl());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title,des,price;
        ImageView pic;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.title);
            des=itemView.findViewById(R.id.des);
            price=itemView.findViewById(R.id.price);
            pic=itemView.findViewById(R.id.pic);
        }
    }
}
