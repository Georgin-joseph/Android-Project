package com.example.foodapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class addressAdapter extends RecyclerView.Adapter<addressAdapter.MyViewHolder> {

    Context context;
    ArrayList<addressDomin> list;
    private Map<String, Boolean> checkboxStates;
    private boolean isAnyCheckboxChecked;

    public addressAdapter(Context context, ArrayList<addressDomin> list) {
        this.context = context;
        this.list = list;
        this.checkboxStates = new HashMap<>();
        this.isAnyCheckboxChecked = false;
    }
    public boolean isAnyCheckboxChecked() {
        return isAnyCheckboxChecked;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.address,parent,false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        addressDomin addressDomin = list.get(position);
        holder.Building.setText(addressDomin.getBuilding());
        holder.Location.setText(addressDomin.getLocation());
        holder.Landmark.setText(addressDomin.getLandmark());
        holder.Receiver_name.setText(addressDomin.getReceiver_name());
        holder.Receiver_mobile.setText(addressDomin.getReceiver_mobile());

        CheckBox checkBox = holder.itemView.findViewById(R.id.checkBox);
        checkBox.setChecked(addressDomin.isChecked());
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Update the mycartdomain object with the new checkbox state
            addressDomin.setChecked(isChecked);

            // Update the checkbox state in Firestore
            updateCheckboxStatusInFirestore( isChecked);

            // Show a toast message when the checkbox is checked
            if (isChecked) {
                Toast.makeText(context, "Address is Selected", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(context, "Please select Address", Toast.LENGTH_SHORT).show();
            }

            isAnyCheckboxChecked = isChecked;
        });
    }
    private void updateCheckboxStatusInFirestore(boolean isChecked) {
        // Update the 'isChecked' field in the Firestore document
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference cartCollection = db.collection("Cart");

        // Get the current user's UID
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String currentUserId = currentUser.getUid();

        // Find the documents where the "userId" field matches the current user's UID
        Query query = cartCollection.whereEqualTo("userId", currentUserId);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Log for debugging
                    Log.d("FirestoreUpdate", "Updating document: " + document.getId());

                    // Update the 'isChecked' field in the document
                    document.getReference().update("isChecked", isChecked)
                            .addOnSuccessListener(aVoid -> Log.d("FirestoreUpdate", "Update successful"))
                            .addOnFailureListener(e -> Log.e("FirestoreUpdate", "Error updating document", e));
                }
            } else {
                // Handle the error
                // You can log the error or display an error message to the user
                Log.e("FirestoreQuery", "Error querying Firestore", task.getException());
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends  RecyclerView.ViewHolder{
        TextView Building,Location,Landmark,Receiver_mobile,Receiver_name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Building=itemView.findViewById(R.id.Building);
            Location=itemView.findViewById(R.id.location);
            Landmark=itemView.findViewById(R.id.landmark);
            Receiver_mobile=itemView.findViewById(R.id.number);
            Receiver_name=itemView.findViewById(R.id.name);
        }
    }

}
