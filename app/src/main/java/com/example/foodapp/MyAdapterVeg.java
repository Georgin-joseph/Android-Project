package com.example.foodapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapterVeg extends RecyclerView.Adapter<MyAdapterVeg.MyViewHolder> {

    Context context;
    ArrayList<manageitemsveg> list;

    public MyAdapterVeg(Context context, ArrayList<manageitemsveg> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.manage_item,parent,false);
        return new MyAdapterVeg.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        manageitemsveg item=list.get(position);
        holder.ItemName.setText(item.getItemName());
        holder.ItemId.setText(item.getItemId());
        holder.ItemQuantity.setText(item.getItemQuantity());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView ItemName,ItemId,ItemQuantity;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ItemName=itemView.findViewById(R.id.ItemName);
            ItemId=itemView.findViewById(R.id.ItemId);
            ItemQuantity=itemView.findViewById(R.id.ItemQuantity);
        }
    }
}
