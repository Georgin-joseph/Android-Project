package com.example.foodapp;//package com.example.foodapp;
//
//import android.app.Activity;
//import android.content.Context;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.squareup.picasso.Picasso;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.ArrayList;
//
//public class mycartAdapter extends RecyclerView.Adapter<mycartAdapter.MyViewHolder> {
//
//    public interface PlusCardBtnClickListener {
//        void onPlusCardBtnClick(int position);
//    }
//
//    private Context context;
//    private ArrayList<mycartdomain> cartlist;
//    private PlusCardBtnClickListener clickListener;
//
//    public mycartAdapter(Context context, ArrayList<mycartdomain> list) {
//        this.context = context;
//        this.cartlist = list;
//    }
//
//    public void setPlusCardBtnClickListener(PlusCardBtnClickListener listener) {
//        this.clickListener = listener;
//    }
//
//    @NonNull
//    @Override
//    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(context).inflate(R.layout.viewholder_cart, parent, false);
//        return new MyViewHolder(v);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        try {
//            mycartdomain mycartdomain = cartlist.get(position);
//            holder.itemName.setText(mycartdomain.getItemName());
//            holder.itemPrice.setText(mycartdomain.getItemPrice());
//            holder.totalEachItem.setText(mycartdomain.getItemPrice());
//
//
//            // Load and display the imageUrl using Picasso
//            Picasso.get()
//                    .load(mycartdomain.getImageUrl())  // Use getImageUrl() instead of imageUrl
//                    .placeholder(R.drawable.fast_1)   // Use a placeholder drawable
//                    .error(R.drawable.fast_1)         // Use an error image
//                    .into(holder.imageUrl);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return cartlist.size();
//    }
//
//    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//
//        TextView itemName, itemPrice, totalEachItem;
//        ImageView imageUrl;
//        ImageView plusCardBtn;
//        Button textView25;
//
//        public MyViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            itemName = itemView.findViewById(R.id.titleTxt);
//            itemPrice = itemView.findViewById(R.id.feeEachItem);
//            imageUrl = itemView.findViewById(R.id.imageView19);
//            plusCardBtn = itemView.findViewById(R.id.plusCardBtn);
//            textView25=itemView.findViewById(R.id.textView25);
//            plusCardBtn.setOnClickListener(this);
//
//        }
//        public   int clickCount=0;
//        @Override
//        public void onClick(View view){
//            clickCount++;
//            int toastMessage = clickCount;
//            Toast.makeText(view.getContext(), "Click Count: " + toastMessage, Toast.LENGTH_LONG).show();
//
//        }
//    }
//}

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodapp.R;
import com.example.foodapp.mycartdomain;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class mycartAdapter extends RecyclerView.Adapter<mycartAdapter.MyViewHolder> {

    public interface PlusCardBtnClickListener {
        void onPlusCardBtnClick(int position);
    }

    private Context context;
    private ArrayList<mycartdomain> cartlist;
    private PlusCardBtnClickListener clickListener;

    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    public mycartAdapter(Context context, ArrayList<mycartdomain> list) {
        this.context = context;
        this.cartlist = list;
        this.firestore = FirebaseFirestore.getInstance();
        this.auth = FirebaseAuth.getInstance();
    }

    public void setPlusCardBtnClickListener(PlusCardBtnClickListener listener) {
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.viewholder_cart, parent, false);
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
                    .load(mycartdomain.getImageUrl())
                    .placeholder(R.drawable.fast_1)
                    .error(R.drawable.fast_1)
                    .into(holder.imageUrl);

            retrieveCountAndDisplay(holder.numberitemtxt, mycartdomain.getUserId(),mycartdomain.getItemId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void retrieveCountAndDisplay(TextView numberitemtxt, String userId, String itemId) {
        // Query to find the document based on userId and itemId
        Query query = firestore.collection("Cart").whereEqualTo("userId", userId).whereEqualTo("itemId", itemId);

        // Execute the query
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Found the document, retrieve the count and display it
                    Long count = document.getLong("count");
                    if (count != null) {
                        numberitemtxt.setText(String.valueOf(count));
                        Toast.makeText(context, "Count retrieved successfully: " + count, Toast.LENGTH_SHORT).show();
                    }
                }
                // If no document is found
                if (task.getResult().isEmpty()) {
                    numberitemtxt.setText("0"); // Or handle it as needed
                    Toast.makeText(context, "No document found for userId: " + userId + " and itemId: " + itemId, Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e("Firestore", "Error getting documents: ", task.getException());
            }
        });
    }




    @Override
    public int getItemCount() {
        return cartlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView itemName, itemPrice, totalEachItem, numberitemtxt;
        ImageView imageUrl;
        ImageView plusCardBtn;
        Button textView25;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.titleTxt);
            itemPrice = itemView.findViewById(R.id.feeEachItem);
            imageUrl = itemView.findViewById(R.id.imageView19);
            plusCardBtn = itemView.findViewById(R.id.plusCardBtn);
            textView25 = itemView.findViewById(R.id.textView25);
            plusCardBtn.setOnClickListener(this);
            numberitemtxt = itemView.findViewById(R.id.numberitemtxt);
        }

        public int clickCount = 0;

        @Override
        public void onClick(View view) {
            clickCount++;

            // Get the current user's UID
            String currentUserUid = auth.getCurrentUser().getUid();

            // Get the item ID and user ID
            String itemId = cartlist.get(getAdapterPosition()).getItemId();
            String userId = cartlist.get(getAdapterPosition()).getUserId();

            // Update the count in Firestore only if the user UID and current UID are the same
            if (currentUserUid.equals(userId)) {
                updateCountInFirestore(itemId, userId, 1);
            }

            int toastMessage = clickCount;
            Toast.makeText(view.getContext(), "Click Count: " + toastMessage, Toast.LENGTH_LONG).show();
        }

        private void updateCountInFirestore(String itemId, String userId, int newCount) {
            // Query to find the document based on itemId and userId
            Query query = firestore.collection("Cart").whereEqualTo("itemId", itemId).whereEqualTo("userId", userId);

            // Execute the query
            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Found the document, update the count
                        int currentCount = document.contains("count") ? document.getLong("count").intValue() : 0;
                        document.getReference().update("count", currentCount + newCount)
                                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Count updated successfully"))
                                .addOnFailureListener(e -> Log.e("Firestore", "Error updating count", e));
                    }
                } else {
                    Log.e("Firestore", "Error getting documents: ", task.getException());
                }
            });
        }

        }
    }
