package com.example.foodapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<manageitems> list;

    public MyAdapter(Context context, ArrayList<manageitems> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.manage_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        manageitems item = list.get(position);
        holder.ItemName.setText(item.getItemName());
        holder.ItemId.setText(item.getItemId());
        holder.ItemQuantity.setText(item.getItemQuantity());

        // Set a click listener for the "Delete" button
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete the item from Firebase
                deleteItemFromFirebase(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView ItemName, ItemId, ItemQuantity;
        Button deleteButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ItemName = itemView.findViewById(R.id.ItemName);
            ItemId = itemView.findViewById(R.id.ItemId);
            ItemQuantity = itemView.findViewById(R.id.ItemQuantity);
            deleteButton = itemView.findViewById(R.id.delete);
        }
    }

    // Implement the method to delete the item from Firestore
    private void deleteItemFromFirebase(manageitems item) {
        // Assuming 'itemId' is the unique identifier for your Firestore documents
        String itemId = item.getItemId();

        // Get an instance of Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Specify the collection and document to delete
        // Construct DocumentReference with the item's ID
        DocumentReference docRef = db.collection("items").document(itemId);
        docRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Document successfully deleted
                        list.remove(item);
                        notifyDataSetChanged();
                        showToast("Item deleted successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the error
                        e.printStackTrace(); // Print the error message to the console
                        showToast("Failed to delete item.");
                    }
                });

    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
