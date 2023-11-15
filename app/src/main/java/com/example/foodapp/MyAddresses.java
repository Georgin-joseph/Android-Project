package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class MyAddresses extends AppCompatActivity {
    private LinearLayout linearLayout2;
    private LinearLayout formLayout;

    private RecyclerView Recycle;
    private TextView  userName, userflat, usermobile,userlocation,userlandmark;

    MyAdapterAddresses Adapter;
    ArrayList<MyAddress> list;
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

        Recycle.setHasFixedSize(true);
        Recycle.setLayoutManager(new LinearLayoutManager(this));

        list=new ArrayList<>();
        Adapter=new MyAdapterAddresses(this,list);
        Recycle.setAdapter(Adapter);

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
                retrieveAndDisplayUserData2();

                formLayout.setVisibility(View.INVISIBLE);
                isFormVisible = false;
                Recycle.setVisibility(View.VISIBLE);
            }
        });
        retrieveAndDisplayUserData();
        retrieveAndDisplayUserData2();

    }
    private void storeUserDetails() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();

        // Reference to the user's document in Firestore
        DocumentReference userDocRef = db.collection("users").document(uid);

        // Get user input from EditText fields
        String receiverName = userName.getText().toString().trim();
        String receiverMobile = usermobile.getText().toString().trim();
        String building = userflat.getText().toString().trim();
        String location = userlocation.getText().toString().trim();
        String landmark = userlandmark.getText().toString().trim();

        // Check if any of the required fields is empty
        if (TextUtils.isEmpty(receiverName) || TextUtils.isEmpty(receiverMobile) ||
                TextUtils.isEmpty(building) || TextUtils.isEmpty(location)) {
            // Show a message to the user that they need to enter all required data
            Toast.makeText(getApplicationContext(), "Please enter all required data", Toast.LENGTH_SHORT).show();
            return; // Exit the method without proceeding to Firestore write
        }

        // Create a new address object with the details you want to store
        Map<String, Object> addressData = new HashMap<>();
        addressData.put("Receiver name", receiverName);
        addressData.put("Receiver mobile", receiverMobile);
        addressData.put("Building", building);
        addressData.put("Location", location);
        addressData.put("Landmark", landmark);

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
    private void retrieveAndDisplayUserData2() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String currentUserId = auth.getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Reference to the user's document in Firestore
        DocumentReference userDocRef = db.collection("users").document(currentUserId);

        // Reference to the "Address" sub-collection inside the user's document
        CollectionReference addressCollectionRef = userDocRef.collection("Address");

        addressCollectionRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        list.clear(); // Clear the previous data
                        for (DocumentSnapshot document : task.getResult()) {
                            // Retrieve data from each document in the "Address" sub-collection
                            String building = document.getString("Building");
                            String location = document.getString("Location");
                            String landmark = document.getString("Landmark");
                            String mobile = document.getString("Receiver mobile");

                            // Create a MyAddress object with the retrieved data
                            MyAddress myAddress = new MyAddress(building, location, landmark, mobile);

                            // Add the MyAddress object to the list for displaying in the RecyclerView
                            list.add(myAddress);
                        }
                        // Notify the Adapter of the data change
                        Adapter.notifyDataSetChanged();
                    } else {
                        // Handle the error if the query is not successful
                        Toast.makeText(MyAddresses.this, "Error retrieving user data.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}