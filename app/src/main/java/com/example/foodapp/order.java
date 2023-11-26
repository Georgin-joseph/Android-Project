package com.example.foodapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class order extends AppCompatActivity {
    RecyclerView recyclerView;
    orderlistAdapter orderlistAdapter;
    ArrayList<orderlistdomin> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        recyclerView = findViewById(R.id.view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        orderlistAdapter = new orderlistAdapter(this, list);
        recyclerView.setAdapter(orderlistAdapter);

        retrieveOrderDetails();
    }

//    private void retrieveOrderDetails() {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//
//        if (currentUser != null) {
//            String currentUserId = currentUser.getUid();
//            CollectionReference orderCollection = db.collection("Order");
//
//            orderCollection.whereEqualTo("userId", currentUserId)
//                    .get()
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            list.clear(); // Clear previous data
//
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                String itemName = document.getString("itemName");
//                                String status = document.getString("status");
//
//                                // Check if count and totalPrice are not null
//                                Integer count = document.getLong("count") != null ? document.getLong("count").intValue() : 0;
////                                Double totalPrice = document.getDouble("totalPrice") != null ? document.getDouble("totalPrice") : 0.0;
//                                Integer totalPrice=document.getLong("totalPrice")!=null ? document.getLong("totalPrice").intValue():0;
//                                Timestamp orderTimestamp = document.getTimestamp("orderTimestamp");
//                                Date date = orderTimestamp != null ? orderTimestamp.toDate() : null;
//
//                                String formattedDate = date != null ? DateFormat.getDateTimeInstance().format(date) : "N/A";
//                                // Create an orderlistdomin object with the retrieved data
//                                orderlistdomin orderItem = new orderlistdomin(itemName, status, count, totalPrice,date);
//
//                                // Add the orderlistdomin object to the list for displaying in the RecyclerView
//                                list.add(orderItem);
//
//                                Toast.makeText(order.this, "Order Date: " + formattedDate, Toast.LENGTH_SHORT).show();
//                            }
//
//                            // Notify the adapter that the data has changed
//                            orderlistAdapter.notifyDataSetChanged();
//                        } else {
//                            // Handle the case where there's an error retrieving order details
//                            Toast.makeText(order.this, "Error retrieving order details", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        }
//    }

    private void retrieveOrderDetails() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            CollectionReference orderCollection = db.collection("Order");

            orderCollection.whereEqualTo("userId", currentUserId)
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
                                        orderlistdomin orderItem = new orderlistdomin(itemName, status, count,totalPrice, new Date());

                                        list.add(orderItem);

                                        if ("Delivered".equals(status)) {
                                            checkFeedbackInOrder(orderId);
                                        }
                                    }
                                }
                            }

                            // Notify the adapter that the data has changed
                            orderlistAdapter.notifyDataSetChanged();
                        } else {
                            // Handle the case where there's an error retrieving order details
                            Toast.makeText(order.this, "Error retrieving order details", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    private boolean isFeedbackSubmitted = false;

    private void showFeedbackDialog(String orderId) {
        if (!isFeedbackSubmitted) {
            // Create a custom layout for the feedback form
            View feedbackView = getLayoutInflater().inflate(R.layout.feedback_form, null);

            // Find the EditText component in the layout
            EditText feedbackEditText = feedbackView.findViewById(R.id.feedbackEditText);

            // Create an AlertDialog to show the feedback form
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Provide Feedback")
                    .setView(feedbackView)
                    .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Handle the feedback submission
                            String feedbackText = feedbackEditText.getText().toString();

                            // TODO: Process the feedback (e.g., store it in Firebase)
                            Toast.makeText(order.this, "Feedback submitted ",Toast.LENGTH_SHORT).show();

                            // Update the user's document with the feedback
                            updateFeedbackInUserDocument(orderId, feedbackText);

                            // Mark feedback as submitted
                            isFeedbackSubmitted = true;

                            // Dismiss the dialog
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Cancel button clicked
                            // Dismiss the dialog
                            dialog.dismiss();
                        }
                    })
                    .create()
                    .show();
        } else {
            // Feedback has already been submitted
            Toast.makeText(order.this, "Feedback has already been submitted for a previous order.", Toast.LENGTH_SHORT).show();
        }

    }
    private void updateFeedbackInUserDocument(String orderId, String feedbackText) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String currentUserId = currentUser.getUid();

            // Update the feedback in the User collection
            db.collection("Users")
                    .document(currentUserId)
                    .update("feedback", feedbackText)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Feedback updated successfully in the User collection
                            Log.d(TAG, "Feedback updated successfully in User collection");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle failures
                            Log.e(TAG, "Error updating feedback in User collection", e);
                        }
                    });

            // Update the feedback in the Order collection
            db.collection("Order")
                    .document(orderId)
                    .update("feedback", feedbackText)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Feedback updated successfully in the Order collection
                            Log.d(TAG, "Feedback updated successfully in Order collection");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle failures
                            Log.e(TAG, "Error updating feedback in Order collection", e);
                        }
                    });
        }
    }
    private void checkFeedbackInOrder(String orderId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Order")
                .document(orderId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        // Check if the current order already has feedback
                        if (documentSnapshot.contains("feedback")) {
                            String feedback = documentSnapshot.getString("feedback");
                            if (TextUtils.isEmpty(feedback)) {
                                // Order has feedback, but it's empty, show the feedback dialog
                                showFeedbackDialog(orderId);
                            } else {
                                // Order has non-empty feedback, don't show the dialog
                                Toast.makeText(order.this, "Feedback already submitted for this order", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Order doesn't have feedback, show the feedback dialog
                            showFeedbackDialog(orderId);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failures
                        Log.e(TAG, "Error checking feedback in Order collection", e);
                    }
                });
    }


}
