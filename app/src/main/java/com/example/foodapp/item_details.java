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
import android.widget.Toast;

import com.example.foodapp.Activity.Admin2;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class item_details extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        TextView itemNameTextView = findViewById(R.id.texttitle);
        TextView itemPriceTextView = findViewById(R.id.price);
        TextView Description=findViewById(R.id.textView22);
        Button addToCartButton = findViewById(R.id.addToCartButton);

        String itemId = getIntent().getStringExtra("item_id");

        // Reference to the Firestore collection
        CollectionReference itemsCollection = db.collection("items");

        // Create a query to get the specific item based on itemId
        Query itemQuery = itemsCollection.whereEqualTo("itemId", itemId);

        itemQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Retrieve the data fields from the document
                        String itemName = document.getString("itemName");
                        String itemPrice = document.getString("itemPrice");
                        String itemDescription = document.getString("itemDescription");
                        String imageUrl = document.getString("imageUrl");

                        // Set the item details in TextViews and ImageView
                        itemNameTextView.setText(itemName);
                        itemPriceTextView.setText(itemPrice);
                        Description.setText(itemDescription);

                        ImageView image = findViewById(R.id.foodPic);
                        Picasso.get().load(imageUrl).into(image);

                        addToCartButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Handle the "Add to Cart" button click event here
                                addToCart(itemId, itemName, itemPrice,imageUrl);
                            }
                        });

                        // Break the loop since you found the item
                        break;
                    }
                } else {
                    // Handle errors or document not found
                    Log.e("Firestore", "Error getting item document: " + task.getException());
                }
            }
        });


        mAuth = FirebaseAuth.getInstance();
    }

    private void addToCart(String itemId, String itemName, String itemPrice,String imageUrl) {
        // Check if the user is authenticated
        if (mAuth.getCurrentUser() != null) {
            String userId = mAuth.getCurrentUser().getUid();

            // Reference to the Firestore collection "Cart"
            CollectionReference cartCollection = db.collection("Cart");

            // Check if a document with the same itemId already exists in the user's cart
            Query query = cartCollection.whereEqualTo("itemId", itemId).whereEqualTo("userId", userId);

            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
                            // No document with the same itemId exists in the user's cart, so you can add the item
                            // Create a new document with a unique ID
                            DocumentReference cartItemRef = cartCollection.document();

                            // Create a Map to store the data you want to add to the cart
                            Map<String, Object> cartItem = new HashMap<>();
                            cartItem.put("itemId", itemId);
                            cartItem.put("itemName", itemName);
                            cartItem.put("itemPrice", itemPrice);
                            cartItem.put("imageUrl", imageUrl);
                            cartItem.put("userId", userId); // Include the user's ID

                            // Set the data to the Firestore document
                            cartItemRef.set(cartItem)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(), "Item added to cart.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Failed to add item to cart.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            // A document with the same itemId already exists in the user's cart
                            Toast.makeText(getApplicationContext(), "Item with the same ID already in the cart.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Handle errors
                        Toast.makeText(getApplicationContext(), "Error checking for duplicate items.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            // User is not authenticated, handle this case
            Toast.makeText(getApplicationContext(), "Please log in to add items to your cart.", Toast.LENGTH_SHORT).show();
        }
    }

    public void back(View view) {
        Intent i = new Intent(this, Admin2.class);
        Toast.makeText(getApplicationContext(), "Back to Home", Toast.LENGTH_SHORT).show();
        startActivity(i);
    }
}
