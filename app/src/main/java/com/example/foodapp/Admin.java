package com.example.foodapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Admin extends AppCompatActivity implements adminOrderAdapter.OnItemLongClickListener{

    private TextView userCountTextView;
adminOrderAdapter adapter;
ArrayList<adminOrderDomain> list;

    RecyclerView recyclerView;
    private TextView shortageItemCountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        userCountTextView = findViewById(R.id.textview8);
        shortageItemCountTextView = findViewById(R.id.textview);
//        textView11=findViewById(R.id.textView11);
        recyclerView=findViewById(R.id.view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list=new ArrayList<adminOrderDomain>();


        adapter=new adminOrderAdapter(this,list,this);
        recyclerView.setAdapter(adapter);


        Toast.makeText(this, "Welcome Admin", Toast.LENGTH_SHORT).show();

        retrieveTotalItemCount();
        retrieveShortageItemCount();
//        setContentView(R.layout.feedback_form);
        retrieveOrderDetails();

    }

    public void menu(View view) {
        Intent intent = new Intent(Admin.this, Additem.class);
        startActivity(intent);
    }

    public void manageitem(View view) {
        Intent intent = new Intent(Admin.this, Manageitem.class);
        startActivity(intent);
    }

    // Function to retrieve the total item count
    private void retrieveTotalItemCount() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("items")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int userCount = task.getResult().size();
                            userCountTextView.setText(String.valueOf(userCount));
                        } else {
                            userCountTextView.setText("Error fetching item count");
                        }
                    }
                });
    }

    // Function to retrieve the shortage item count
    private void retrieveShortageItemCount() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("items")
                .whereLessThan("itemQuantity", "5") // Adjust the value as per your definition of shortage
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int shortageCount = task.getResult().size();
                            shortageItemCountTextView.setText(String.valueOf(shortageCount));
                        } else {
                            shortageItemCountTextView.setText("Error fetching shortage item count");
                        }
                    }
                });
    }

//textView11.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            // Call the logoutUser method when the TextView is clicked
//            logoutUser();
//        }
//    });
//
//    private void logoutUser() {
//        // Perform any necessary logout actions here
//        // For example, you can clear session data, navigate to the login screen, etc.
//        clearSessionData();
//        navigateToLoginScreen();
//    }
//
//    private void clearSessionData() {
//        // Implement code to clear any session data or user preferences
//        // For example:
//        // SharedPreferences preferences = getSharedPreferences("your_preferences_name", Context.MODE_PRIVATE);
//        // SharedPreferences.Editor editor = preferences.edit();
//        // editor.clear();
//        // editor.apply();
//    }
//
//    private void navigateToLoginScreen() {
//        // Implement code to navigate to the login screen
//        // For example:
//         Intent loginIntent = new Intent(this, login.class);
//         startActivity(loginIntent);
//        // finish(); // Optional: Close the current activity if needed
//        Toast.makeText(this, "User logged out!", Toast.LENGTH_SHORT).show();
//    }


    private void retrieveOrderDetails() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.d(TAG, "Your log message here");

        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            CollectionReference orderCollection = db.collection("Order");

            orderCollection
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            list.clear(); // Clear previous data

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String orderId = document.getId();
                                Timestamp orderTimestamp = document.getTimestamp("orderTimestamp");
                                String status=document.getString("status");

                                List<Map<String, Object>> items = (List<Map<String, Object>>) document.get("items");
                                if (items != null) {
                                    for (Map<String, Object> item : items) {
                                        String itemName = (String) item.get("itemName");
//                                        String status = "status";  // You might get status from somewhere
                                        Integer count = ((Long) item.get("count")).intValue();
                                        Integer totalPrice=document.getLong("totalPrice")!=null ? document.getLong("totalPrice").intValue():0;
                                        // Create an orderlistdomin object with the retrieved data
                                        adminOrderDomain orderItem = new adminOrderDomain(itemName, status,count,totalPrice,new Date());
                                        orderItem.setOrderId(orderId);


                                        list.add(orderItem);
                                    }
                                }
                            }

                            // Notify the adapter that the data has changed
                            adapter.notifyDataSetChanged();
                        } else {
                            // Handle the case where there's an error retrieving order details
                            Toast.makeText(Admin.this, "Error retrieving order details", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    @Override
    public void onItemLongClick(int position) {
        // Handle the long click event here, show options
        // You can use position to get the clicked item's data from the list

        AlertDialog.Builder builder = new AlertDialog.Builder(Admin.this);
        builder.setTitle("Select an option")
                .setItems(new CharSequence[]{"Accepted", "Your Food is Cooking","Your Food is Cooked","Delivered","Rejected"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle the selected option
                        String selectedStatus = ""; // Initialize as needed
                        switch (which) {
                            case 0:
                                // Accept option selected
                                selectedStatus = "Accepted";
                                break;
                            case 1:
                                // Preparing option selected
                                selectedStatus = "Your Food is Cooking";
                                break;

                            case 2:
                                // Preparing option selected
                                selectedStatus = "Your Food is Cooked";
                                break;
                            case 3:
                                // Delivery option selected
                                selectedStatus = "Delivered";
                                break;

                            case 4:
                                // Delivery option selected
                                selectedStatus = "Rejected";
                                break;
                        }

                        // Update the status in the Firestore collection
                        updateOrderStatus(position, selectedStatus);
                    }
                })
                .create()
                .show();
    }


    private void updateOrderStatus(int position, String newStatus) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Assuming you have an Order document ID, replace "orderId" with the actual field name
        String orderId = list.get(position).getOrderId();

        // Reference to the Order document
        DocumentReference orderRef = db.collection("Order").document(orderId);

        // Update the "status" field
        orderRef.update("status", newStatus)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Update successful
                        Log.d(TAG, "Order status updated successfully");
                        // Refresh the order details after updating the status
                        retrieveOrderDetails();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failures
                        Log.e(TAG, "Error updating order status", e);
                    }
                });
    }
}








