package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import android.view.View;
import android.widget.Button;

public class User_profile extends AppCompatActivity {

    private LinearLayout formLayout;
    private LinearLayout linearLayout4, linearLayout7;
    private LinearLayout linearLayout5;
    private LinearLayout linearLayout6;
    private boolean isFormVisible = false;
    private Spinner spinnerGender;
    private Button Addmoney,saveButton;
    private TextView nameTextView, userName, userEmail, usermobile,Balance;
    private TextView emailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Find your views by ID
        formLayout = findViewById(R.id.formLayout);
        linearLayout4 = findViewById(R.id.linearLayout4);
        spinnerGender = findViewById(R.id.spinnerGender);
        Addmoney = findViewById(R.id.addmoney);
        linearLayout4 = findViewById(R.id.linearLayout4);
        linearLayout5 = findViewById(R.id.linearLayout5); // Add this line
        linearLayout6 = findViewById(R.id.linearLayout6); // Add this line
        spinnerGender = findViewById(R.id.spinnerGender);
        linearLayout7 = findViewById(R.id.linearLayout7);
        nameTextView = findViewById(R.id.textView14);
        emailTextView = findViewById(R.id.email);
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        usermobile = findViewById(R.id.usermobile);
        saveButton=findViewById(R.id.buttonSave);
        Balance=findViewById(R.id.balance);

        // Initialize the Spinner with gender options
        String[] genderOptions = {"Male", "Female", "Other"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genderOptions);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(genderAdapter);

        // Initially, set formLayout to be invisible
        formLayout.setVisibility(View.INVISIBLE);
        linearLayout5.setVisibility(View.VISIBLE);
        linearLayout6.setVisibility(View.VISIBLE);
        linearLayout7.setVisibility(View.VISIBLE);

        // Set a click listener for linearLayout4 to toggle the form's visibility
        linearLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle the visibility of formLayout
                if (isFormVisible) {
                    formLayout.setVisibility(View.INVISIBLE);
                    linearLayout5.setVisibility(View.VISIBLE);
                    linearLayout6.setVisibility(View.VISIBLE);
                    linearLayout7.setVisibility(View.VISIBLE);
                } else {
                    formLayout.setVisibility(View.VISIBLE);
                    linearLayout5.setVisibility(View.INVISIBLE);
                    linearLayout6.setVisibility(View.INVISIBLE);
                    linearLayout7.setVisibility(View.INVISIBLE);
                }
                // Update the visibility state flag
                isFormVisible = !isFormVisible;
            }
        });

        Addmoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(User_profile.this);
                View dialogview = getLayoutInflater().inflate(R.layout.add_moneydialogbox, null);
                EditText moneyBox = dialogview.findViewById(R.id.addmoney);

                builder.setView(dialogview);
                AlertDialog dialog = builder.create();
                // Handle the "Submit" button click
                dialogview.findViewById(R.id.btnsubmit).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String amount = moneyBox.getText().toString();

                        if (amount.isEmpty()) {
                            // Show an alert if the amount is empty
                            Toast.makeText(User_profile.this, "Please enter an amount.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Add the amount to Firebase Firestore
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            String uid = auth.getCurrentUser().getUid();

                            // Create a reference to the user's document in Firestore
                            DocumentReference userDocRef = db.collection("users").document(uid);

                            // Get the current balance from Firestore (if it exists)
                            userDocRef.get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()) {
                                                // Get the current balance
                                                Double currentBalance = documentSnapshot.getDouble("balance");
                                                if (currentBalance == null) {
                                                    currentBalance = 0.0;
                                                }

                                                // Update the balance by adding the entered amount
                                                double newBalance = currentBalance + Double.parseDouble(amount);

                                                // Create a data object with the updated balance
                                                Map<String, Object> userUpdate = new HashMap<>();
                                                userUpdate.put("balance", newBalance);

                                                // Update the balance in the Firestore document
                                                userDocRef.update(userUpdate)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                // Amount added successfully
                                                                Toast.makeText(User_profile.this, "Amount Successfully added: " + amount, Toast.LENGTH_SHORT).show();
                                                                dialog.dismiss();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                // Handle the error while updating the balance
                                                                Toast.makeText(User_profile.this, "Failed to update balance.", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            } else {
                                                // Handle the case where the user's document doesn't exist
                                                Toast.makeText(User_profile.this, "User document not found.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Handle the error while fetching the user's document
                                            Toast.makeText(User_profile.this, "Error retrieving user data.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });

                // Show the dialog to the user
                dialog.show();
            }
        });

        linearLayout7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement your logout logic here, such as clearing the session or any user data
                // Redirect to the login screen (LoginActivity) or perform any other actions as needed
                // Example: You might clear user data and then start the login activity
                // Clear user data (you need to implement this function)
                clearUserData();

                // Start the LoginActivity
                Intent intent = new Intent(User_profile.this, Login.class);
                startActivity(intent);

                // Finish the current activity (User_profile) to prevent the user from coming back via the back button
                finish();
            }

            private void clearUserData() {
                // Clear user data logic
            }
        });

        // Retrieve and display user data based on the current user's UID
        retrieveAndDisplayUserData();
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the function to store user details when the saveButton is clicked
                storeuserdetails();

                formLayout.setVisibility(View.INVISIBLE);
                isFormVisible = false;
                linearLayout5.setVisibility(View.VISIBLE);
                linearLayout6.setVisibility(View.VISIBLE);
                linearLayout7.setVisibility(View.VISIBLE);
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
                            String email = document.getString("email");
                            Double balance = document.getDouble("balance");

                            nameTextView.setText(name);
                            emailTextView.setText(email);
                            userName.setText(name);
                            userEmail.setText(email);
                            Balance.setText(String.valueOf(balance));

                        } else {
                            // Handle the case where the document doesn't exist
                            Toast.makeText(User_profile.this, "User document not found.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Handle the error if the query is not successful
                        Toast.makeText(User_profile.this, "Error retrieving user data.", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void storeuserdetails() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();

        // Reference to the user's document in Firestore
        DocumentReference userDocRef = db.collection("users").document(uid);

        // Retrieve the existing user document to preserve the "password" field
        userDocRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // Get the existing data
                            Map<String, Object> userData = documentSnapshot.getData();

                            // Update only the fields you want to change
                            userData.put("name", userName.getText().toString());
                            userData.put("mobile", usermobile.getText().toString());
                            userData.put("email", userEmail.getText().toString());

                            DatePicker datePicker = findViewById(R.id.userDob);
                            int year = datePicker.getYear();
                            int month = datePicker.getMonth();
                            int day = datePicker.getDayOfMonth();

                            Calendar calendar = Calendar.getInstance();
                            calendar.set(year, month, day);
                            Date dobDate = calendar.getTime();

                            userData.put("dob", dobDate);
                            userData.put("gender", spinnerGender.getSelectedItem().toString());

                            // Update the Firestore document with the new data
                            userDocRef.set(userData)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Document updated successfully
                                            String docId = userDocRef.getId(); // Get the document ID
                                            // You can store the docId as needed.
                                            Toast.makeText(getApplicationContext(), "User data saved", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Handle the error
                                            Toast.makeText(getApplicationContext(), "Failed to save user data", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            // Handle the case where the user's document doesn't exist
                            Toast.makeText(getApplicationContext(), "User document not found.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the error while retrieving the existing document
                        Toast.makeText(getApplicationContext(), "Failed to retrieve user data.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void Addresses(View view) {
        Intent intent = new Intent(User_profile.this, MyAddresses.class);
        startActivity(intent);

    }

    // Define and initialize the saveButton


// Set an OnClickListener for the saveButton



}