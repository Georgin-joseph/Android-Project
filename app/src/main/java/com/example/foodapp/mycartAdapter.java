package com.example.foodapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class mycartAdapter extends RecyclerView.Adapter<mycartAdapter.MyViewHolder> {

    public interface PlusCardBtnClickListener {
        void onPlusCardBtnClick(int position);
    }
        Context context;
        ArrayList<mycartdomain>cartlist;


    private PlusCardBtnClickListener clickListener;

    public mycartAdapter(Context context, ArrayList<mycartdomain> list) {
        this.context = context;
        this.cartlist = list;
    }

    public void setPlusCardBtnClickListener(PlusCardBtnClickListener listener) {
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.viewholder_cart,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {
            mycartdomain mycartdomain = cartlist.get(position);
            holder.itemName.setText(mycartdomain.getItemName());
            holder.itemPrice.setText(mycartdomain.getItemPrice());
            holder.totalEachItem.setText(mycartdomain.getItemPrice());

            // Load and display the imageUrl using Picasso
            Picasso.get()
                    .load(mycartdomain.imageUrl)
                    .placeholder(R.drawable.fast_1) // Use a placeholder drawable
                    .error(R.drawable.fast_1) // Use an error image
                    .into(holder.imageUrl);

            // Set a single click listener for plusCardBtn
            holder.plusCardBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Check if a click listener is set
                    if (clickListener != null) {
                        // Notify the listener that plusCardBtn is clicked for this item
                        clickListener.onPlusCardBtnClick(position);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public int getItemCount() {
        return cartlist.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView itemName,itemPrice,totalEachItem;
        ImageView imageUrl;
        ImageView plusCardBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName=itemView.findViewById(R.id.titleTxt);
            itemPrice=itemView.findViewById(R.id.feeEachItem);
            imageUrl = itemView.findViewById(R.id.imageView19);
            plusCardBtn=itemView.findViewById(R.id.plusCardBtn);


        }
    }

}
