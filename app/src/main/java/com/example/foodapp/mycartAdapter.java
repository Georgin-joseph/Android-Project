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
import android.text.TextUtils;
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
            Log.d("MyCartAdapter", "UserId: " + mycartdomain.getUserId() + ", ItemId: " + mycartdomain.getItemId());
            holder.itemName.setText(mycartdomain.getItemName());
            holder.itemPrice.setText(mycartdomain.getItemPrice());
            holder.totalEachItem.setText(mycartdomain.getItemPrice());
            holder.numberitemtxt.setText(String.valueOf(cartlist.get(position).getCount()));

            // Load and display the imageUrl using Picasso
            Picasso.get()
                    .load(mycartdomain.getImageUrl())
                    .placeholder(R.drawable.fast_1)
                    .error(R.drawable.fast_1)
                    .into(holder.imageUrl);

            int count = cartlist.get(position).getCount(); // Retrieve count from the list, not mycartdomain
            int originalPrice = Integer.parseInt(mycartdomain.getItemPrice());
            int newPrice = count * originalPrice;

// Assuming that you have a setCount method in mycartdomain, update the count in the mycartdomain object
            mycartdomain.setCount(count);

            Toast.makeText(context, "new price" + newPrice, Toast.LENGTH_SHORT).show();
            holder.totalEachItem.setText(String.valueOf(newPrice));




        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    private void retrieveCountFromFirestore(String itemId, String userId, int position, TextView numberitemtxt) {
//        // Query to find the document based on itemId and userId
//        Query query = firestore.collection("Cart").whereEqualTo("itemId", itemId).whereEqualTo("userId", userId);
//
//        // Execute the query
//        query.get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                for (QueryDocumentSnapshot document : task.getResult()) {
//                    // Found the document, retrieve the count
//                    int count = document.contains("count") ? document.getLong("count").intValue() : 0;
//
//                    // Update the count in the mycartdomain object
//                    cartlist.get(position).setCount(count);
//
//                    // Update the count in the numberitemtxt TextView
//                    numberitemtxt.setText(String.valueOf(count));
//
//                    // Notify the adapter of the data change
//                    notifyDataSetChanged();
//                }
//            } else {
//                Log.e("Firestore", "Error getting documents: ", task.getException());
//            }
//        });
//    }


    @Override
    public int getItemCount() {
        return cartlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView itemName, itemPrice, totalEachItem, numberitemtxt;
        ImageView imageUrl;
        ImageView plusCardBtn,cancel,minusCardBtn;
        Button textView25;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.titleTxt);
            itemPrice = itemView.findViewById(R.id.feeEachItem);
            imageUrl = itemView.findViewById(R.id.imageView19);
            plusCardBtn = itemView.findViewById(R.id.plusCardBtn);
            textView25 = itemView.findViewById(R.id.textView25);
            totalEachItem=itemView.findViewById(R.id.totalEachItem);
            plusCardBtn.setOnClickListener(this);
            numberitemtxt = itemView.findViewById(R.id.numberitemtxt);
            cancel=itemView.findViewById(R.id.cancel);
            cancel.setOnClickListener(view -> handleCancelImageClick());
            minusCardBtn = itemView.findViewById(R.id.minusCardBtn);
            minusCardBtn.setOnClickListener(view -> handleMinusImageClick());
        }
        private void handleMinusImageClick() {
            // Logic for minusCardBtn click

            // Get the item ID and user ID
            String itemId = cartlist.get(getAdapterPosition()).getItemId();
            String userId = cartlist.get(getAdapterPosition()).getUserId();

            // Call a method to decrement the count in Firestore
            decrementCountInFirestore(itemId, userId);
        }
        private void handleCancelImageClick() {
            // Logic for cancelImage click

            // Get the item ID and user ID
            String itemId = cartlist.get(getAdapterPosition()).getItemId();
            String userId = cartlist.get(getAdapterPosition()).getUserId();

            // Call a method to delete the item from Firestore
            deleteItemFromFirestore(itemId, userId);
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
            Toast.makeText(view.getContext(), "Item Added: ", Toast.LENGTH_LONG).show();
            retrieveCountFromFirestore(itemId, userId);
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

                        // Calculate the new count and price
                        int newCountValue = currentCount + newCount;
                        int originalPrice = document.contains("itemPrice") ? Integer.parseInt(document.getString("itemPrice")) : 0;
                        // Replace with the actual field name for the item price
                        int newPrice = newCountValue * originalPrice;

                        // Update the count and newPrice fields
                        document.getReference().update("count", newCountValue, "newPrice", newPrice)
                                .addOnSuccessListener(aVoid -> {
                                    Log.d("Firestore", "Count and newPrice updated successfully");

                                    // After successfully updating the count, retrieve it and display
                                    retrieveCountFromFirestore(itemId, userId);
                                })
                                .addOnFailureListener(e -> Log.e("Firestore", "Error updating count and newPrice", e));
                    }
                } else {
                    Log.e("Firestore", "Error getting documents: ", task.getException());
                }
            });
        }


        private void retrieveCountFromFirestore(String itemId, String userId) {
            // Query to find the document based on itemId and userId
            Query query = firestore.collection("Cart").whereEqualTo("itemId", itemId).whereEqualTo("userId", userId);

            // Execute the query
            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Found the document, retrieve the count
                        int count = document.contains("count") ? document.getLong("count").intValue() : 0;
                        displayCountToast(count);
                        cartlist.get(getAdapterPosition()).setCount(count);
                        notifyDataSetChanged();
                    }
                } else {
                    Log.e("Firestore", "Error getting documents: ", task.getException());
                }
            });
        }

        private void displayCountToast(int count) {
//            Toast.makeText(itemView.getContext(), "Item Count: " + count, Toast.LENGTH_SHORT).show();
            displayCountInTextView(count);
        }
        private void displayCountInTextView(int count) {
            // Display the count in the numberitemtxt TextView
            numberitemtxt.setText(String.valueOf(count));
        }

        private void deleteItemFromFirestore(String itemId, String userId) {
            // Query to find the document based on itemId and userId
            Query query = firestore.collection("Cart").whereEqualTo("itemId", itemId).whereEqualTo("userId", userId);

            // Execute the query
            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Found the document, delete it
                        document.getReference().delete()
                                .addOnSuccessListener(aVoid -> {
                                    Log.d("Firestore", "Item deleted successfully");

                                    // Optionally, update your local data and notify the adapter
                                    // to remove the item from the RecyclerView
                                    cartlist.remove(getAdapterPosition());
                                    notifyItemRemoved(getAdapterPosition());

                                    // Refresh the page by notifying the adapter
                                    notifyDataSetChanged();
                                })
                                .addOnFailureListener(e -> Log.e("Firestore", "Error deleting item", e));
                    }
                } else {
                    Log.e("Firestore", "Error getting documents: ", task.getException());
                }
            });
        }

        private void decrementCountInFirestore(String itemId, String userId) {
            // Query to find the document based on itemId and userId
            Query query = firestore.collection("Cart").whereEqualTo("itemId", itemId).whereEqualTo("userId", userId);

            // Execute the query
            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Found the document, retrieve the current count
                        int currentCount = document.contains("count") ? document.getLong("count").intValue() : 0;

                        // Ensure the count doesn't go below 0
                        int newCount = Math.max(0, currentCount - 1);

                        // Update the count in Firestore
                        document.getReference().update("count", newCount)
                                .addOnSuccessListener(aVoid -> {
                                    Log.d("Firestore", "Count decremented successfully");

                                    // After successfully updating the count, retrieve it and update the price
                                    retrievePriceAndUpdateAfterDecrement(itemId, userId);
                                })
                                .addOnFailureListener(e -> Log.e("Firestore", "Error updating count", e));
                    }
                } else {
                    Log.e("Firestore", "Error getting documents: ", task.getException());
                }
            });
        }


        private void retrievePriceAndUpdateAfterDecrement(String itemId, String userId) {
            // Query to find the document based on itemId and userId
            Query query = firestore.collection("Cart").whereEqualTo("itemId", itemId).whereEqualTo("userId", userId);

            // Execute the query
            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Found the document, retrieve the item price as a String
                        String itemPriceStr = document.getString("itemPrice");

                        // Check if itemPriceStr is not null and is a valid number
                        if (itemPriceStr != null && TextUtils.isDigitsOnly(itemPriceStr)) {
                            // Parse itemPrice as an integer
                            int itemPrice = Integer.parseInt(itemPriceStr);

                            // Calculate the new total price
                            int newTotalPrice = itemPrice * document.getLong("count").intValue();

                            // Update the new price directly in Firestore
                            document.getReference().update("newPrice", newTotalPrice)
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("Firestore", "New price updated successfully");

                                        // Refresh the UI with the updated count and new price
                                        retrieveCountFromFirestore(itemId, userId);
                                    })
                                    .addOnFailureListener(e -> Log.e("Firestore", "Error updating new price", e));
                        } else {
                            Log.e("Firestore", "Invalid or null itemPrice value");
                        }
                    }
                } else {
                    Log.e("Firestore", "Error getting documents: ", task.getException());
                }
            });
        }




    }
    }

