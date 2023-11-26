package com.example.foodapp;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import java.util.ArrayList;
import java.util.Arrays;

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
        holder.ItemQty.setText(item.getItemQuantity());

        // Set a click listener for the "Edit" button
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the item's ID for editing
                String itemId = item.getItemId();
                showEditDialog(itemId, item);
            }
        });

        // Set a click listener for the "Delete" button
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the item's ID for deletion
                String itemId = item.getItemId();
                showConfirmationDialog(itemId, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView ItemName, ItemId,ItemQty;
        Button editButton, deleteButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ItemName = itemView.findViewById(R.id.ItemName);
            ItemId = itemView.findViewById(R.id.ItemId);
            ItemQty=itemView.findViewById(R.id.ItemQuantity);
            editButton = itemView.findViewById(R.id.edit);
            deleteButton = itemView.findViewById(R.id.delete);
        }
    }

    private void showEditDialog(String itemId, manageitems item) {
        // Create a DialogPlus instance
        DialogPlus dialog = DialogPlus.newDialog(context)
                .setGravity(Gravity.CENTER)
                .setContentHolder(new ViewHolder(R.layout.update_popup))
                .setExpanded(false)
                .setMargin(100, 100, 100, 100)
                .create();

        // Get the root view of the custom layout
        View rootView = dialog.getHolderView();

        // Retrieve the Spinner view
        Spinner spinnerCategory = rootView.findViewById(R.id.spinnerCategory);

        // Call the method to retrieve categories and populate the Spinner
        retrieveCategoriesForSpinner(spinnerCategory, item);

        // Retrieve other views from the layout and set data
        EditText itemIdEditText = rootView.findViewById(R.id.itemId);
        EditText itemNameEditText = rootView.findViewById(R.id.itemName);
        EditText itemPriceEditText = rootView.findViewById(R.id.itemPrice);
        EditText itemQuantityEditText = rootView.findViewById(R.id.ItemQuantity);
        EditText itemDescriptionEditText = rootView.findViewById(R.id.ItemDescription);

        // Set the existing item details in the EditText fields
        itemIdEditText.setText(item.getItemId());
        itemNameEditText.setText(item.getItemName());
        itemPriceEditText.setText(String.valueOf(item.getItemPrice()));
        itemQuantityEditText.setText(item.getItemQuantity());
        itemDescriptionEditText.setText(item.getItemDescription());


        ImageView itemImageView = rootView.findViewById(R.id.imageView);
        Glide.with(context)
                .load(item.getImageUrl())
                .into(itemImageView);

        // Show the DialogPlus dialog
        dialog.show();
        Button updateButton = rootView.findViewById(R.id.btnupdate);

        // Inside the updateButton click listener in MyAdapter
        // Inside the updateButton click listener in MyAdapter
        // Inside the updateButton click listener in MyAdapter
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve new data from the EditText fields and Spinner
                String newItemName = itemNameEditText.getText().toString();
                String newItemPrice = itemPriceEditText.getText().toString();
                String newItemCategory = spinnerCategory.getSelectedItem().toString();
                String newItemQuantity = itemQuantityEditText.getText().toString();
                String newItemDescription = itemDescriptionEditText.getText().toString();

                // Update the item's data
                item.setItemName(newItemName);
                item.setItemPrice(newItemPrice);
                item.setItemCategory(newItemCategory);
                item.setItemQuantity(newItemQuantity);
                item.setItemDescription(newItemDescription);

                // Now, you can use this updated 'item' to update the Firestore document
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("items")
                        .document(item.getItemId()) // Use the item's ID
                        .set(item) // Update the Firestore document with the new data
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                showToast("Item updated successfully!");
                                dialog.dismiss(); // Close the dialog after the update
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                showToast("Failed to update item. Error: " + e.getMessage());
                            }
                        });
            }
        });


    }
        private void retrieveCategoriesForSpinner(Spinner spinnerCategory, manageitems item) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("items")
                .orderBy("category") // Use "category" if that's the field name in your Firestore documents
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<String> categories = new ArrayList<>();
                    for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                        String category = queryDocumentSnapshots.getDocuments().get(i).getString("category");
                        if (category != null && !categories.contains(category)) {
                            categories.add(category);
                        }
                    }

                    // Create an ArrayAdapter for the Spinner and set the data
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, categories);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCategory.setAdapter(adapter);

                    // Set the existing item category using the Spinner
                    spinnerCategory.setSelection(categories.indexOf(item.getItemCategory()));
                })
                .addOnFailureListener(e -> {
                    showToast("Error querying Firestore for categories: " + e.getMessage());
                });



    }




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

    private void deleteItemFromFirestore(String itemId, int position) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

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

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
