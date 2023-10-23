package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MyAddresses extends AppCompatActivity {
    private LinearLayout linearLayout2;
    private LinearLayout formLayout;

    private RecyclerView Recycle;
    private TextView  userName, userflat, usermobile,userlocation,userlandmark;

    private boolean isFormVisible = false;
    private Button Saveaddress;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_addresses);
        linearLayout2=findViewById(R.id.linearLayout2);
        formLayout = findViewById(R.id.formLayout);
        Recycle=findViewById(R.id.Recycle);
        userName=findViewById(R.id.userName);
        userflat=findViewById(R.id.userflat);
        usermobile=findViewById(R.id.usermobile);
        userlocation=findViewById(R.id.userlocation);
        userlandmark=findViewById(R.id.userlandmark);
        Saveaddress=findViewById(R.id.Saveaddress);

        formLayout.setVisibility(View.INVISIBLE);
        Recycle.setVisibility(View.VISIBLE);

        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle the visibility of formLayout
                if (isFormVisible) {
                    formLayout.setVisibility(View.INVISIBLE);
                    Recycle.setVisibility(View.VISIBLE);
                } else {
                    formLayout.setVisibility(View.VISIBLE);
                    Recycle.setVisibility(View.INVISIBLE);
                }
                // Update the visibility state flag
                isFormVisible = !isFormVisible;
            }
        });

//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // Request the permission.
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
//        } else {
//            // You have the permission, so proceed with your app's logic.
//            // For example, start a location-related feature or load a map.
//        }
//        linearLayout2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Create a URI for the location you want to show on Google Maps
//                String location = "Latitude,Longitude"; // Replace with the actual latitude and longitude
//                Uri gmmIntentUri = Uri.parse("geo:" + location + "?q=" + Uri.encode("Your Location Name"));
//
//                // Create an Intent to launch Google Maps
//                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//                mapIntent.setPackage("com.google.android.apps.maps"); // Specify the Google Maps app
//
//                // Check if the Google Maps app is installed
//                if (mapIntent.resolveActivity(getPackageManager()) != null) {
//                    // Open Google Maps
//                    startActivity(mapIntent);
//                } else {
//                    // Handle the case where Google Maps is not installed
//                    Toast.makeText(MyAddresses.this, "Google Maps app is not installed.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
        Saveaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the function to store user details when the saveButton is clicked
                storeUserDetails();

                formLayout.setVisibility(View.INVISIBLE);
                isFormVisible = false;
                Recycle.setVisibility(View.VISIBLE);
            }
        });
        retrieveAndDisplayUserData();

    }
    private void storeUserDetails() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();

        // Reference to the user's document in Firestore
        DocumentReference userDocRef = db.collection("users").document(uid);

        // Create a new address object with the details you want to store
        Map<String, Object> addressData = new HashMap<>();
        addressData.put("Receiver name", userName.getText().toString());
        addressData.put("Receiver mobile", usermobile.getText().toString());
        addressData.put("Building", userflat.getText().toString());
        addressData.put("Location", userlocation.getText().toString());
        addressData.put("Landmark", userlandmark.getText().toString());

        // Reference to the "Address" sub-collection inside the user's document
        CollectionReference addressCollectionRef = userDocRef.collection("Address");

        // Add the address data to the "Address" sub-collection
        addressCollectionRef.add(addressData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Address data added successfully
                        String addressDocumentId = documentReference.getId(); // Get the document ID
                        // You can store the addressDocumentId as needed.
                        Toast.makeText(getApplicationContext(), "Address saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the error
                        Toast.makeText(getApplicationContext(), "Failed to save address", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void retrieveAndDisplayUserData() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String currentUserId = auth.getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .document(currentUserId) // Use .document() to specify the document by its ID
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Retrieve data and set it in the TextView elements
                            String name = document.getString("name");
                            String mobile = document.getString("mobile");

                            userName.setText(name);
                            usermobile.setText(mobile);
                        } else {
                            // Handle the case where the document doesn't exist
                            Toast.makeText(MyAddresses.this, "User document not found.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Handle the error if the query is not successful
                        Toast.makeText(MyAddresses.this, "Error retrieving user data.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}