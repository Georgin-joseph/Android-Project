package com.example.foodapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog; // Add AlertDialog import
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;

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

        // Set a click listener for the "Delete" button
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the item's ID
                String itemId = item.getItemId();
                // Show a confirmation dialog
                showConfirmationDialog(itemId, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView ItemName, ItemId;
        Button deleteButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ItemName = itemView.findViewById(R.id.ItemName);
            ItemId = itemView.findViewById(R.id.ItemId);
            deleteButton = itemView.findViewById(R.id.delete);
        }
    }

    // Implement the method to delete the item from Firestore using the itemId
    private void deleteItemFromFirestore(String itemId, int position) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Specify the collection and document to delete using the provided itemId
        db.collection("items")
                .whereEqualTo("itemId", itemId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                        String docId = queryDocumentSnapshots.getDocuments().get(i).getId();
                        db.collection("items").document(docId)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        showToast("Item deleted successfully!");
                                        // Remove the item from the list and refresh the RecyclerView
                                        list.remove(position);
                                        notifyDataSetChanged();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        showToast("Failed to delete item. Error: " + e.getMessage());
                                    }
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    showToast("Error querying Firestore: " + e.getMessage());
                });
    }

    // Method to show a confirmation dialog
    private void showConfirmationDialog(String itemId, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete this item?");

        builder.setPositiveButton("Yes", (dialog, which) -> {
            // User confirmed, proceed with deletion
            deleteItemFromFirestore(itemId, position);
        });

        builder.setNegativeButton("No", (dialog, which) -> {
            // User canceled, do nothing
        });

        builder.show();
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
