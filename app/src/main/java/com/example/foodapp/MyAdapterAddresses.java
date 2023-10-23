package com.example.foodapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapterAddresses extends RecyclerView.Adapter<MyAdapterAddresses.MyViewHolder> {

    Context context;

    ArrayList<MyAddress> list;

    public MyAdapterAddresses(Context context, ArrayList<MyAddress> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.myaddresses,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        MyAddress MyAddress=list.get(position);
        holder.Building.setText(MyAddress.getBuilding());
        holder.Location.setText(MyAddress.getLocation());
        holder.Landmark.setText(MyAddress.getLandmark());
        holder.Receiver_mobile.setText(MyAddress.getReceiver_mobile());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView Building,Location,Landmark,Receiver_mobile;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Building=itemView.findViewById(R.id.flat);
            Location=itemView.findViewById(R.id.location);
            Landmark=itemView.findViewById(R.id.landmark);
            Receiver_mobile=itemView.findViewById(R.id.mobile);
        }
    }
}
