package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodapp.Activity.Admin2;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.squareup.picasso.Picasso;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class item_details extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String itemName; // Declare itemName as a global variable
    private String itemPrice; // Declare itemPrice as a global variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        TextView itemIdTextView = findViewById(R.id.price);
        Button addToCartButton = findViewById(R.id.addToCartButton);

        String itemId = getIntent().getStringExtra("item_id");
        itemIdTextView.setText(itemId);

        // Reference to the Firestore collection
        CollectionReference itemsCollection = db.collection("items");

        // Create a query to get the specific item based on itemId
        Query itemQuery = itemsCollection.whereEqualTo("itemId", itemId);

        // Perform the query
        itemQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Retrieve the data fields from the document
                        itemName = document.getString("itemName");
                        itemPrice = document.getString("itemPrice");
                        String itemDescription = document.getString("itemDescription");
                        String imageUrl = document.getString("imageUrl");

                        // Now you have the item details, and you can use them as needed
                        // You can set these values in TextViews, ImageViews, etc.

                        // Example: Set itemName, itemPrice, and itemDescription in TextViews
                        TextView itemNameTextView = findViewById(R.id.texttitle);
                        TextView itemPriceTextView = findViewById(R.id.price);
                        TextView itemDescriptionTextView = findViewById(R.id.textView22);
                        ImageView image = findViewById(R.id.foodPic);

                        itemNameTextView.setText(itemName);
                        itemPriceTextView.setText(itemPrice);
                        itemDescriptionTextView.setText(itemDescription);

                        Picasso.get().load(imageUrl).into(image);
                    }
                } else {
                    // Handle errors or document not found
                    Log.e("Firestore", "Error getting documents: " + task.getException());
                }
            }
        });

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the "Add to Cart" button click event here
                addToCart(itemId, itemName, itemPrice);
            }
        });
    }

    private void addToCart(String itemId, String itemName, String itemPrice) {
        // Reference to the Firestore collection "Cart"
        CollectionReference cartCollection = db.collection("Cart");

        // Check if a document with the same itemId already exists
        Query query = cartCollection.whereEqualTo("itemId", itemId);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().isEmpty()) {
                        // No document with the same itemId exists, so you can add the item
                        // Create a new document with a unique ID
                        DocumentReference cartItemRef = cartCollection.document();

                        // Create a Map to store the data you want to add to the cart
                        Map<String, Object> cartItem = new HashMap<>();
                        cartItem.put("itemId", itemId);
                        cartItem.put("itemName", itemName);
                        cartItem.put("itemPrice", itemPrice);

                        // Set the data to the Firestore document
                        cartItemRef.set(cartItem)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(), "Item added to cart.", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Failed to add item to cart.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        // A document with the same itemId already exists
                        Toast.makeText(getApplicationContext(), "Item with the same ID already in the cart.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Handle errors
                    Toast.makeText(getApplicationContext(), "Error checking for duplicate items.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void back(View view) {
        Intent i=new Intent(this, Admin2.class);
        Toast.makeText(getApplicationContext(),"Back to Home",Toast.LENGTH_SHORT).show();
        startActivity(i);
    }
}
