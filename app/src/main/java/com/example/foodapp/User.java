package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QuerySnapshot;

public class User extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // Assuming you have a TextView with ID "welcomeMessage" in your layout
        TextView welcomeMessageTextView = findViewById(R.id.textView1);

        // Retrieve the user's email
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        // Reference to the Firestore database
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");

        // Retrieve the user's name from Firestore based on their email
        db.collection("users")
                .whereEqualTo("email", userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Check if there is at least one document matching the query
                            if (!task.getResult().isEmpty()) {
                                // Get the first document (assuming there's only one)
                                DocumentSnapshot document = task.getResult().getDocuments().get(0);

                                // Retrieve the "name" field from the document
                                String userName = document.getString("name");
                                String firstLetter = userName.substring(0, 1);

                                // Update the UI with the welcome message and user's name
                                welcomeMessageTextView.setText("Welcome " + userName);
                            } else {
                                // Handle the case where no documents match the query
                                Toast.makeText(User.this, "User document does not exist for email: " + userEmail, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Handle any errors that occur during the query
                            Toast.makeText(User.this, "Error querying user data: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        Toast.makeText(this, "USER", Toast.LENGTH_SHORT).show();
    }
}
