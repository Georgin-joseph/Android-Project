package com.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class nonVeg extends AppCompatActivity {

    RecyclerView recyclerView;
    nonVegAdapter adapter;
    ArrayList<nonVegDomain> list;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_veg);

        recyclerView = findViewById(R.id.view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new nonVegAdapter(this, list);
        recyclerView.setAdapter(adapter);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Call the method to fetch Non-Veg items
        fetchNonVegItems();
    }

    private void fetchNonVegItems() {
        // Reference to the "items" collection
        db.collection("items")
                .whereEqualTo("category", "Non-Veg") // Query for Non-Veg items
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Clear the existing list before adding new data
                        list.clear();

                        // Iterate through the documents and add them to the list
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            nonVegDomain item = document.toObject(nonVegDomain.class);
                            list.add(item);
                        }

                        // Notify the adapter that the data set has changed
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.w("Firestore", "Error getting documents.", task.getException());
                    }
                });
    }
}
