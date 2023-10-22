package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class Admin extends AppCompatActivity {

    private TextView userCountTextView;
    private TextView shortageItemCountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        userCountTextView = findViewById(R.id.textview8);
        shortageItemCountTextView = findViewById(R.id.textview);
        Toast.makeText(this, "Welcome Admin", Toast.LENGTH_SHORT).show();

        retrieveTotalItemCount();
        retrieveShortageItemCount();
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
}








