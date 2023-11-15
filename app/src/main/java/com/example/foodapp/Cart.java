//package com.example.foodapp;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.os.Bundle;
//import android.widget.ScrollView;
//import android.widget.TextView;
//
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;
//
//import java.util.ArrayList;
//
//public class Cart extends AppCompatActivity {
//    private RecyclerView recyclerViewList;
//    private TextView totalFeeTxt, taxTxt, deliveryTxt, totalTxt;
//    mycartAdapter myAdapter;
//    ArrayList<mycartdomain> list;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_cart);
//
//        recyclerViewList = findViewById(R.id.view);
//        recyclerViewList.setHasFixedSize(true);
//        recyclerViewList.setLayoutManager(new LinearLayoutManager(this));
//
//        list = new ArrayList<>();
//        myAdapter = new mycartAdapter(this, list);
//        recyclerViewList.setAdapter(myAdapter);
//
//        retrieveCartData();
//
//    }
//
//    private void retrieveCartData() {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        CollectionReference cartCollection = db.collection("Cart");
//
//        cartCollection.get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                QuerySnapshot querySnapshot = task.getResult();
//                if (querySnapshot != null) {
//                    for (QueryDocumentSnapshot document : querySnapshot) {
//                        // Access item data
//                        String itemName = document.getString("itemName");
//                        String itemPrice = document.getString("itemPrice");
//
//                        if (itemName != null && itemPrice != null) {
//                            // Create a mycartdomain object and add it to the list
//                            mycartdomain cartItem = new mycartdomain(itemName, itemPrice);
//                            list.add(cartItem);
//                        }
//                    }
//                    // Notify the adapter that the data has changed
//                    myAdapter.notifyDataSetChanged();
//                } else {
//                    // Handle the case where there's no data in the "Cart" collection
//
//                }
//            } else {
//                // Handle the error
//                // You can log the error or display an error message to the user
//                // Log.e(TAG, "Error getting documents: " + task.getException());
//                // Display an error message, for example:
//                // Toast.makeText(Cart.this, "Error retrieving cart data", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//
//}

package com.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

public class Cart extends AppCompatActivity {
    private RecyclerView recyclerViewList;
    private TextView totalFeeTxt, taxTxt, deliveryTxt, totalTxt;
    mycartAdapter myAdapter;
    private RecyclerView recyclerView;
    ArrayList<mycartdomain> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerViewList = findViewById(R.id.view);
        recyclerViewList.setHasFixedSize(true);
        recyclerViewList.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        myAdapter = new mycartAdapter(this, list);
        recyclerViewList.setAdapter(myAdapter);


        mycartAdapter adapter = new mycartAdapter(this, list);
        adapter.setPlusCardBtnClickListener(new mycartAdapter.PlusCardBtnClickListener() {
            @Override
            public void onPlusCardBtnClick(int position) {
                // Your implementation when the button is clicked
                // For example, show a toast message
                Toast.makeText(Cart.this, "Button clicked at position: " + position, Toast.LENGTH_SHORT).show();
            }
        });
//        recyclerViewList.setAdapter(myAdapter);


        retrieveCartData();
    }


    private void retrieveCartData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference cartCollection = db.collection("Cart");

        // Get the current user's UID
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String currentUserId = currentUser.getUid();

        cartCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        // Access item data
                        String itemName = document.getString("itemName");
                        String itemPrice = document.getString("itemPrice");
                        String imageUrl = document.getString("imageUrl");
                        String userId = document.getString("userId");
                        String itemId = document.getString("itemId");

                        // Handle the 'newPrice' field correctly based on its actual type
                        Object newPriceObj = document.get("newPrice");
                        int newPrice = 0;  // default value if 'newPrice' is not a number

                        if (newPriceObj instanceof Number) {
                            newPrice = ((Number) newPriceObj).intValue();
                        } else if (newPriceObj != null) {
                            // Handle other types if necessary
                            // You can log or display a message indicating an unexpected type
                        }

                        if (itemName != null && itemPrice != null && userId != null) {
                            // Check if the userId matches the current user's UID
                            if (userId.equals(currentUserId)) {
                                // Create a mycartdomain object and add it to the list
                                mycartdomain cartItem = new mycartdomain(itemName, itemPrice, imageUrl, itemId, userId, newPrice);
                                list.add(cartItem);
                            }
                        }
                    }

                    // Notify the adapter that the data has changed
                    myAdapter.notifyDataSetChanged();
                } else {
                    // Handle the case where there's no data in the "Cart" collection
                }
            } else {

            }
        });
    }
}


//    private void retrieveCartData() {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        CollectionReference cartCollection = db.collection("Cart");
//
//        // Get the current user's UID
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        String currentUserId = currentUser.getUid();
//
//        // Initialize an array to store the total sum of newPrice values
//        int[] totalNewPrice = {0}; // Array with one element
//
//        cartCollection.get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                QuerySnapshot querySnapshot = task.getResult();
//                if (querySnapshot != null) {
//                    for (QueryDocumentSnapshot document : querySnapshot) {
//                        // Access item data
//                        String itemName = document.getString("itemName");
//                        String itemPrice = document.getString("itemPrice");
//                        String imageUrl = document.getString("imageUrl");
//                        String userId = document.getString("userId");
//                        String itemId = document.getString("itemId");
//
//                        if (userId != null && itemName != null && itemPrice != null && imageUrl != null && itemId != null) {
//                            // Check if the userId matches the current user's UID
//                            if (userId.equals(currentUserId)) {
//                                // Retrieve the newPrice value from the document
//                                int newPrice = document.getLong("newPrice").intValue();
//
//                                // Add the newPrice to the total sum
//                                totalNewPrice[0] += newPrice;
//                                int totalNewPriceValue = 100;
//                                mycartdomain cartItem = new mycartdomain(itemName, itemPrice, imageUrl, itemId, userId);
//                                cartItem.setNewPrice(100); // Set the newPrice
//                                cartItem.setTotalNewPrice(500);
//                                list.add(cartItem);
//                            }
//                        }
//                    }
//
//                    // Display the total sum of newPrice values in a Toast
//                    Toast.makeText(Cart.this, "Total New Price: " + totalNewPrice[0], Toast.LENGTH_SHORT).show();
//
//                    // Notify the adapter that the data has changed
//                    myAdapter.notifyDataSetChanged();
//                } else {
//                    // Handle the case where there's no data in the "Cart" collection
//                }
//            } else {
//                // Handle the error
//                // You can log the error or display an error message to the user
//                // Log.e(TAG, "Error getting documents: " + task.getException());
//                // Display an error message, for example:
//                // Toast.makeText(Cart.this, "Error retrieving cart data", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}

