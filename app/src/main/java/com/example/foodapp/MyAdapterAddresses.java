package com.example.foodapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

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
        View v = LayoutInflater.from(context).inflate(R.layout.myaddresses, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MyAddress myAddress = list.get(position);
        holder.Building.setText(myAddress.getBuilding());
        holder.Location.setText(myAddress.getLocation());
        holder.Landmark.setText(myAddress.getLandmark());
        holder.Receiver_mobile.setText(myAddress.getReceiver_mobile());

        // delete
        holder.address_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the current user's UID
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                if (currentUser != null) {
                    String currentUid = currentUser.getUid();

                    // Now 'currentUid' can be used as the itemId
                    showConfirmationDialog(currentUid, position);
                } else {
                    // Handle the case where there is no authenticated user
                    // You might want to redirect the user to the login screen or take appropriate action
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Building, Location, Landmark, Receiver_mobile;
        ImageView address_delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Building = itemView.findViewById(R.id.flat);
            Location = itemView.findViewById(R.id.location);
            Landmark = itemView.findViewById(R.id.landmark);
            Receiver_mobile = itemView.findViewById(R.id.mobile);
            address_delete = itemView.findViewById(R.id.address_delete);
        }
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    private void showConfirmationDialog(String currentUid, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete this item?");

        builder.setPositiveButton("Yes", (dialog, which) -> {
            // User confirmed, proceed with deletion
            deleteAddressFromFirestore(currentUid, position);
        });

        builder.setNegativeButton("No", (dialog, which) -> {
            // User canceled, do nothing
        });

        builder.show();
    }

//    private void deleteItemFromFirestore(String currentUid, int position) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        db.collection("items")
//                .whereEqualTo("itemId", currentUid)
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
//                        String docId = queryDocumentSnapshots.getDocuments().get(i).getId();
//                        db.collection("items").document(docId)
//                                .delete()
//                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//                                        showToast("Item deleted successfully!");
//                                        list.remove(position);
//                                        notifyDataSetChanged();
//                                    }
//                                })
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        showToast("Failed to delete item. Error: " + e.getMessage());
//                                    }
//                                });
//                    }
//                })
//                .addOnFailureListener(e -> {
//                    showToast("Error querying Firestore: " + e.getMessage());
//                });
//    }
private void deleteAddressFromFirestore(String currentUid, int position) {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    db.collection("users")
            .document(currentUid)
            .collection("Address")
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                if (!queryDocumentSnapshots.isEmpty()) {
                    String addressId = queryDocumentSnapshots.getDocuments().get(position).getId();

                    db.collection("users")
                            .document(currentUid)
                            .collection("Address")
                            .document(addressId)
                            .delete()
                            .addOnSuccessListener(aVoid -> {
                                showToast("Address deleted successfully!");
                                // If you have a list of addresses, you might want to remove the item from the list
                                // list.remove(position);
                                list.remove(position);
                                // notifyDataSetChanged();
                                notifyDataSetChanged();
                            })
                            .addOnFailureListener(e ->
                                    showToast("Failed to delete address. Error: " + e.getMessage()));
                } else {
                    showToast("No addresses found for deletion.");
                }
            })
            .addOnFailureListener(e -> showToast("Error querying Firestore: " + e.getMessage()));
}

}
