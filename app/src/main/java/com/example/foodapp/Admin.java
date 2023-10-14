package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodapp.Activity.Admin2;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class Admin extends AppCompatActivity {

    private TextView userCountTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        userCountTextView = findViewById(R.id.textview1);
        Toast.makeText(this, "Welcome Admin", Toast.LENGTH_SHORT).show();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int userCount = task.getResult().size();
                            // Display the user count in the TextView
                            userCountTextView.setText(String.valueOf(userCount));
                        } else {
                            // Handle errors
                            userCountTextView.setText("Error fetching user count");
                        }
                    }
                });
    }

    public void menu(View view) {
        Intent intent = new Intent(Admin.this, Additem.class);
        startActivity(intent);
    }

    public void manageitem(View view) {
        Intent intent = new Intent(Admin.this, Manageitem.class);
        startActivity(intent);
    }
}







