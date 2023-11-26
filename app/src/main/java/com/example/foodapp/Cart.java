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

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart extends AppCompatActivity {
    private RecyclerView recyclerViewList;
    private TextView totalFeeTxt, taxTxt, deliveryTxt, totalTxt,textView24,textView25,tax,totalprice;
    mycartAdapter myAdapter;
    private Map<String, Boolean> checkboxStates;
    addressAdapter myaddresadapter;
    private RecyclerView recyclerView;
    ArrayList<mycartdomain> list;
    ArrayList<addressDomin> list1;
    TextView textView28;
    private boolean orderPlaced = false;
    private int totalPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        textView24 = findViewById(R.id.textView24);
        textView25 = findViewById(R.id.textView25);
        tax=findViewById(R.id.tax);
        totalprice=findViewById(R.id.totalprice);
        textView28=findViewById(R.id.textView28);
        recyclerViewList = findViewById(R.id.view);
        recyclerViewList.setHasFixedSize(true);
        recyclerViewList.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        myAdapter = new mycartAdapter(this, list);
        recyclerViewList.setAdapter(myAdapter);

        recyclerView=findViewById(R.id.address);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list1=new ArrayList<>();
        myaddresadapter=new addressAdapter(this,list1);
        recyclerView.setAdapter(myaddresadapter);

        checkboxStates = new HashMap<>();

        retrieveAndDisplayUserData2();

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
        sumNewPriceValues();
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
                        for (DocumentSnapshot document : task.getResult()) {
                            // Retrieve data from each document in the "Address" sub-collection
                            String Building = document.getString("Building");
                            String Location = document.getString("Location");
                            String Landmark = document.getString("Landmark");
                            String Receiver_mobile = document.getString("Receiver mobile");
                            String Receiver_name=document.getString(" Receiver name");
                            Boolean isChecked= Boolean.valueOf(document.getString("isChecked"));

                            // Create a MyAddress object with the retrieved data
                            addressDomin addressDomin = new addressDomin(Building, Location, Landmark, Receiver_mobile, Receiver_name,isChecked);

                            // Add the MyAddress object to the list for displaying in the RecyclerView
                            list1.add(addressDomin);
                        }
                        // Notify the Adapter of the data change
                        myaddresadapter.notifyDataSetChanged();
                    } else {
                        // Handle the error if the query is not successful
//                        Toast.makeText(my.this, "Error retrieving user data.", Toast.LENGTH_SHORT).show();
                    }
                });
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
                        Long countLong = document.getLong("count");
                        int count = (countLong != null) ? countLong.intValue() : 0;

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
                                mycartdomain cartItem = new mycartdomain(itemName, itemPrice, imageUrl, itemId, userId, newPrice,count);
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
//    private void sumNewPriceValues() {
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
//                        String userId = document.getString("userId");
//
//                        if (userId != null && userId.equals(currentUserId)) {
//                            // Retrieve the newPrice value from the document
//                            Object newPriceObj = document.get("newPrice");
//
//                            // Handle the 'newPrice' field correctly based on its actual type
//                            if (newPriceObj instanceof Number) {
//                                int newPrice = ((Number) newPriceObj).intValue();
//                                // Add the newPrice to the total sum
//                                totalNewPrice[0] += newPrice;
//                            } else if (newPriceObj != null) {
//                                // Handle other types if necessary
//                                // You can log or display a message indicating an unexpected type
//                            }
//                        }
//                    }
//
//                    // Display the total sum of newPrice values in a Toast
//                    textView24.setText(String.valueOf(totalNewPrice[0]));
////                    myAdapter.notifyDataSetChanged();
//
//                } else {
//                    // Handle the case where there's no data in the "Cart" collection
//                    Toast.makeText(Cart.this, "No data in the Cart collection", Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                // Handle the error
//                // You can log the error or display an error message to the user
//                Toast.makeText(Cart.this, "Error retrieving cart data", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
private void sumNewPriceValues() {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference cartCollection = db.collection("Cart");

    // Get the current user's UID
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    String currentUserId = currentUser.getUid();

    // Initialize an array to store the total sum of newPrice values
    int[] totalNewPrice = {0}; // Array with one element

    cartCollection.get().addOnCompleteListener(task -> {
        if (task.isSuccessful()) {
            QuerySnapshot querySnapshot = task.getResult();
            if (querySnapshot != null) {
                for (QueryDocumentSnapshot document : querySnapshot) {
                    // Access item data
                    String userId = document.getString("userId");

                    if (userId != null && userId.equals(currentUserId)) {
                        // Retrieve the newPrice value from the document
                        Object newPriceObj = document.get("newPrice");

                        // Handle the 'newPrice' field correctly based on its actual type
                        if (newPriceObj instanceof Number) {
                            int newPrice = ((Number) newPriceObj).intValue();
                            // Add the newPrice to the total sum
                            totalNewPrice[0] += newPrice;
                        } else if (newPriceObj != null) {
                            // Handle other types if necessary
                            // You can log or display a message indicating an unexpected type
                        }
                    }
                }

                // Display the total sum of newPrice values in a Toast
                textView24.setText(String.valueOf(totalNewPrice[0]));
                int updatedValueForTextView25 = (totalNewPrice[0] > 200) ? 35 : 15;
                if (totalNewPrice[0] == 0) {
                    updatedValueForTextView25 = 0;
                }
                textView25.setText(String.valueOf(updatedValueForTextView25));

                int updatedValueFortax = (totalNewPrice[0] > 200) ? 40 : 25;
                if (totalNewPrice[0] == 0) {
                    updatedValueFortax = 0;
                }
                tax.setText(String.valueOf(updatedValueFortax));

                this.totalPrice = totalNewPrice[0] + updatedValueForTextView25 + updatedValueFortax;
                totalprice.setText(String.valueOf(totalPrice));

                TextView placeOrderTextView = findViewById(R.id.Placeorder);
                placeOrderTextView.setOnClickListener(v -> {

//                    if (!orderPlaced) {
                        if (currentUser != null) {
                            String currentUserIdPlaceOrder = currentUser.getUid();

                            // Call the method to retrieve user balance
                            retrieveUserBalance(currentUserIdPlaceOrder, totalPrice);
//                            orderPlaced = true;
                        } else {
                            // Handle the case where the current user is null
                            Toast.makeText(Cart.this, "Current user not found", Toast.LENGTH_SHORT).show();
                        }

//                    mycartAdapter.notifyDataSetChanged();
//                    }
                });

//                retrieveUserItemTotalPrice(currentUserId);
//                // Store the totalNewPrice[0] value in the users collection
//                updateUserItemTotalPrice(currentUserId, totalNewPrice[0]);
            } else {
                // Handle the case where there's no data in the "Cart" collection
                Toast.makeText(Cart.this, "No data in the Cart collection", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Handle the error
            // You can log the error or display an error message to the user
            Toast.makeText(Cart.this, "Error retrieving cart data", Toast.LENGTH_SHORT).show();
        }
    });
}
//    private void retrieveUserBalance(String userId, int totalPrice) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        db.collection("users")
//                .document(userId)
//                .get()
//                .addOnSuccessListener(documentSnapshot -> {
//                    if (documentSnapshot.exists()) {
//                        // Retrieve the balance from the document
//                        int balance = documentSnapshot.getLong("balance").intValue();
//
//                        // Check if totalPrice is less than or equal to balance
//                        if (totalPrice <= balance) {
//                            // Sufficient balance, proceed with placing the order
//                            // Retrieve and remove items from the cart
//                            retrieveCartItemDetails();
//                            retrieveCartItemDetails1();
//                            if (isPlaceOrderButtonEnabled()) {
//                                removeAllItemsFromCart(userId);
//                                list.clear();
//
//                            // Update UI or perform other actions
//                                int newBalance = balance - totalPrice;
//                                updateBalanceInUserDocument(userId, newBalance);
//                            // TODO: Optionally update the user's balance in the Users collection
//                            // updateBalanceInUserDocument(userId, balance - totalPrice);
//
//                            // Notify the user that the order has been placed
//                            Toast.makeText(Cart.this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
//                            Intent intent=new Intent(getApplicationContext(),order.class);
//                            startActivity(intent);
//                            }
//                        } else {
//                            // Insufficient balance, show an alert message
//                            showLowBalanceAlert();
//                            disablePlaceOrderButton();
//                            if (isPlaceOrderButtonEnabled()) {
//                                removeAllItemsFromCart(userId);
//                            }
//                        }
//                    } else {
//                        // Handle the case where the user document does not exist
//                        Toast.makeText(Cart.this, "User document not found", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(e -> {
//                    // Handle failures
//                    Toast.makeText(Cart.this, "Error retrieving user balance", Toast.LENGTH_SHORT).show();
//                });
//    }

    private void retrieveUserBalance(String userId, int totalPrice) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String currentUserId = currentUser.getUid();
        db.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        int balance = documentSnapshot.getLong("balance").intValue();

                        if (totalPrice <= balance) {
                            // Sufficient balance, proceed with placing the order
                            retrieveCartItemDetails();
                            retrieveCartItemDetails1();
                            removeAllItemsFromCart(currentUserId);
                            list.clear();
                            // Disable the place order button if needed
                            disablePlaceOrderButton();

                            // Remove items from the cart
                            removeAllItemsFromCart(userId);
                            list.clear();

                            // Update the user's balance
                            int newBalance = balance - totalPrice;
                            updateBalanceInUserDocument(userId, newBalance);

                            // Notify the user that the order has been placed
                            Toast.makeText(Cart.this, "Order placed successfully!", Toast.LENGTH_SHORT).show();

                            // Navigate to the order page or perform other actions
                            Intent intent = new Intent(getApplicationContext(), order.class);
                            startActivity(intent);
                        } else {
                            // Insufficient balance, show an alert message
                            showLowBalanceAlert();

                            // Disable the place order button if needed
                            disablePlaceOrderButton();
                        }
                    } else {
                        // Handle the case where the user document does not exist
                        Toast.makeText(Cart.this, "User document not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failures
                    Toast.makeText(Cart.this, "Error retrieving user balance", Toast.LENGTH_SHORT).show();
                });
    }


    private void updateBalanceInUserDocument(String userId, int newBalance) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .document(userId)
                .update("balance", newBalance)
                .addOnSuccessListener(aVoid -> {
                    // Balance updated successfully
                    Log.d("Firestore", "Balance updated successfully");
                })
                .addOnFailureListener(e -> {
                    // Handle failures
                    Log.e("Firestore", "Error updating balance", e);
                });
    }

    private void disablePlaceOrderButton() {
        TextView placeOrderButton = findViewById(R.id.Placeorder);
        placeOrderButton.setEnabled(false);
    }
    private boolean isPlaceOrderButtonEnabled() {
        TextView placeOrderButton = findViewById(R.id.Placeorder);
        return placeOrderButton.isEnabled();
    }

    private void showLowBalanceAlert() {
        // Display an alert dialog indicating low balance
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Low Balance")
                .setMessage("Insufficient balance to place the order.")
                .setPositiveButton("OK", (dialog, which) -> {
                    // Close the dialog or perform other actions as needed
                    dialog.dismiss();
                })
                .create()
                .show();
    }
    private void removeAllItemsFromCart(String currentUserId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference cartCollection = db.collection("Cart");

        // Create a batch write operation
        WriteBatch batch = db.batch();

        // Query to get all documents in the Cart collection for the current user
        Query query = cartCollection.whereEqualTo("userId", currentUserId);

        // Execute the query and delete each document in the batch
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        // Add each document reference to the batch
                        batch.delete(cartCollection.document(document.getId()));
                    }

                    // Commit the batch to delete all documents
                    batch.commit().addOnSuccessListener(aVoid -> {
                        // Handle successful deletion
//                        Toast.makeText(Cart.this, "Items removed from Cart", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(e -> {
                        // Handle failure
                        Toast.makeText(Cart.this, "Error removing items from Cart", Toast.LENGTH_SHORT).show();
                    });
                }
            } else {
                // Handle the error
                Toast.makeText(Cart.this, "Error retrieving cart data for removal", Toast.LENGTH_SHORT).show();
            }
        });
    }
//    private void retrieveCartItemDetails() {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//
//        if (currentUser != null) {
//            // Get the current user's UID
//            String currentUserId = currentUser.getUid();
//
//            // Assuming you have access to the Cart collection
//            CollectionReference cartCollection = db.collection("Cart");
//
//            // Retrieve the cart item details for the current user
//            cartCollection.whereEqualTo("userId", currentUserId)
//                    .get()
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                // Retrieve the relevant fields
//                                String itemName = document.getString("itemName");
//                                String itemPrice = document.getString("itemPrice");
//                                int newPrice = document.getLong("newPrice").intValue();
//                                int count = document.getLong("count").intValue();
//
//                                // Display the details in a toast (you can modify this as needed)
//                                String toastMessage = "Item Name: " + itemName + "\n"
//                                        + "Item Price: " + itemPrice + "\n"
//                                        + "New Price: " + newPrice + "\n"
//                                        + "Count: " + count;
//
//                                Toast.makeText(Cart.this, toastMessage, Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            // Handle the case where there's an error
//                            Toast.makeText(Cart.this, "Error retrieving cart item details", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        }
//    }

//    private void retrieveCartItemDetails() {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//
//        if (currentUser != null) {
//            String currentUserId = currentUser.getUid();
//            CollectionReference cartCollection = db.collection("Cart");
//
//            cartCollection.whereEqualTo("userId", currentUserId)
//                    .get()
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            StringBuilder toastMessageBuilder = new StringBuilder();
//
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                String itemName = document.getString("itemName");
//                                String itemPrice = document.getString("itemPrice");
//                                int newPrice = document.getLong("newPrice").intValue();
//                                int count = document.getLong("count").intValue();
//
//                                boolean isChecked = document.getBoolean("isChecked");
//
//                                // Retrieve checkbox state from the map
//                                toastMessageBuilder.append("Item Name: ").append(itemName).append("\n")
//                                        .append("Item Price: ").append(itemPrice).append("\n")
//                                        .append("New Price: ").append(newPrice).append("\n")
//                                        .append("Count: ").append(count).append("\n")
//                                        .append("isChecked: ").append(isChecked).append("\n");
//                            }
//
//                            Toast.makeText(Cart.this, toastMessageBuilder.toString(), Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(Cart.this, "Error retrieving cart item details", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        }
//    }
//    private void retrieveCartItemDetails1() {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//
//        if (currentUser != null) {
//            String currentUserId = currentUser.getUid();
//            CollectionReference cartCollection = db.collection("Cart");
//            CollectionReference orderCollection = db.collection("Order");
//
//            cartCollection.whereEqualTo("userId", currentUserId)
//                    .get()
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                String itemName = document.getString("itemName");
//                                String itemPrice = document.getString("itemPrice");
//                                int newPrice = document.getLong("newPrice").intValue();
//                                int count = document.getLong("count").intValue();
//                                boolean isChecked = document.getBoolean("isChecked");
//
//                                // Create a new Order object
//                                OrderModel orderModel = new OrderModel(itemName, itemPrice, newPrice, count, isChecked, currentUserId);
//
//                                // Add the Order object to the "Order" collection
//                                orderCollection.add(orderModel);
//                            }
//                        } else {
//                            // Handle the error
//                        }
//                    });
//        }
//    }

    private List<String> cartItemDetailsList = new ArrayList<>();

    private void retrieveCartItemDetails() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            CollectionReference cartCollection = db.collection("Cart");

            cartCollection.whereEqualTo("userId", currentUserId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            cartItemDetailsList.clear(); // Clear previous data

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String itemName = document.getString("itemName");
                                String itemPrice = document.getString("itemPrice");
                                int newPrice = document.getLong("newPrice").intValue();
                                int count = document.getLong("count").intValue();
                                boolean isChecked = document.getBoolean("isChecked");
//
//                                Boolean isChecked = document.getBoolean("isChecked");
//                                boolean isChecked = isCheckedObj != null && isCheckedObj.booleanValue();

                                // Store the cart item details in the list
                                cartItemDetailsList.add("Item Name: " + itemName +
                                        "\nItem Price: " + itemPrice +
                                        "\nNew Price: " + newPrice +
                                        "\nCount: " + count +
                                        "\nisChecked: " + isChecked);
//                                Toast.makeText(Cart.this, "Order placed", Toast.LENGTH_SHORT).show();
                            }

                            // Process the cart item details as needed (e.g., log or use the data)
                            processCartItemDetails();
                        } else {
                            // Handle the error
                            // You can log the error or display an error message to the user
                            Log.e(TAG, "Error retrieving cart item details", task.getException());
                        }
                    });
        }
    }

    private void processCartItemDetails() {
        // Process the cart item details stored in the list
        for (String cartItemDetails : cartItemDetailsList) {
            // You can log the details or perform other actions as needed
            Log.d(TAG, cartItemDetails);
        }
        // You can also use the cartItemDetailsList for further processing as needed
    }

//    private void retrieveCartItemDetails1() {
//        sumNewPriceValues();
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//
//        if (currentUser != null) {
//            String currentUserId = currentUser.getUid();
//            CollectionReference cartCollection = db.collection("Cart");
//            CollectionReference orderCollection = db.collection("Order");
//
//            String orderId = orderCollection.document().getId();
//            cartCollection.whereEqualTo("userId", currentUserId)
//                    .get()
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            List<OrderItem> orderItems = new ArrayList<>();
//
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                String itemName = document.getString("itemName");
//                                String itemPrice = document.getString("itemPrice");
////                                int newPrice = document.getLong("newPrice").intValue();
//                                Long newPriceLong = document.getLong("newPrice");
//                                int newPrice = (newPriceLong != null) ? newPriceLong.intValue() : 0;
//                                int count = document.getLong("count").intValue();
//                                boolean isChecked = document.getBoolean("isChecked");
//
//                                OrderItem orderItem = new OrderItem(itemName, itemPrice, newPrice, count, isChecked);
//                                orderItems.add(orderItem);
//                            }
//
//                            // Create an OrderModel with the list of order items
//                            OrderModel orderModel = new OrderModel(orderItems, currentUserId, "Pending", this.totalPrice); // Set default status as "Pending"
//
//                            Date currentDate = Calendar.getInstance().getTime();
//                            Timestamp timestamp = new Timestamp(currentDate);
//                            orderModel.setOrderTimestamp(timestamp);
//                            // Add the OrderModel to the "Order" collection
//                            orderCollection.document(orderId)
//                                    .set(orderModel)
//                                    .addOnSuccessListener(aVoid -> {
//                                        // Order data successfully stored
//                                    })
//                                    .addOnFailureListener(e -> {
//                                        // Handle the error
//                                    });
//                        } else {
//                            // Handle the error
//                        }
//                    });
//        }
//    }

    private void retrieveCartItemDetails1() {
        sumNewPriceValues();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            CollectionReference cartCollection = db.collection("Cart");
            CollectionReference orderCollection = db.collection("Order");
            CollectionReference itemsCollection = db.collection("items");

            String orderId = orderCollection.document().getId();
            cartCollection.whereEqualTo("userId", currentUserId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<OrderItem> orderItems = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String itemName = document.getString("itemName");
                                String itemPrice = document.getString("itemPrice");
                                Long newPriceLong = document.getLong("newPrice");
                                int newPrice = (newPriceLong != null) ? newPriceLong.intValue() : 0;
                                int count = document.getLong("count").intValue();
                                boolean isChecked = document.getBoolean("isChecked");

                                OrderItem orderItem = new OrderItem(itemName, itemPrice, newPrice, count, isChecked);
                                orderItems.add(orderItem);

                                // Reduce itemQuantity in the items collection
                                reduceItemQuantity(itemsCollection, itemName, count);
                            }

                            // Create an OrderModel with the list of order items
                            OrderModel orderModel = new OrderModel(orderItems, currentUserId, "Pending", this.totalPrice);
                            Date currentDate = Calendar.getInstance().getTime();
                            Timestamp timestamp = new Timestamp(currentDate);
                            orderModel.setOrderTimestamp(timestamp);

                            // Add the OrderModel to the "Order" collection
                            orderCollection.document(orderId)
                                    .set(orderModel)
                                    .addOnSuccessListener(aVoid -> {
                                        // Order data successfully stored
                                    })
                                    .addOnFailureListener(e -> {
                                        // Handle the error
                                    });
                        } else {
                            // Handle the error
                        }
                    });
        }
    }

    private void reduceItemQuantity(CollectionReference itemsCollection, String itemName, int count) {
        itemsCollection.whereEqualTo("itemName", itemName)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Get the current itemQuantity as a string
                            String currentQuantityStr = document.getString("itemQuantity");

                            // Convert the string to an integer
                            int currentQuantity = 0;
                            if (currentQuantityStr != null && !currentQuantityStr.isEmpty()) {
                                currentQuantity = Integer.parseInt(currentQuantityStr);
                            }

                            // Calculate the new itemQuantity after reducing count
                            int newItemQuantity = currentQuantity - count;

                            // Update the itemQuantity in the items collection
                            itemsCollection.document(document.getId())
                                    .update("itemQuantity", String.valueOf(newItemQuantity))
                                    .addOnSuccessListener(aVoid -> {
                                        // ItemQuantity updated successfully
                                    })
                                    .addOnFailureListener(e -> {
                                        // Handle the error
                                    });
                        }
                    } else {
                        // Handle the error
                    }
                });
    }



    public void refresh(View view) {
        reloadYourActivity();
    }
    private void reloadYourActivity() {
        // Implement the logic to reload or refresh your activity
        // For example, recreate the activity
        recreate();
    }

//    private void updateUserItemTotalPrice(String userId, int totalNewPrice) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        CollectionReference usersCollection = db.collection("users");
//
//        // Create a new document in the "users" collection with the user's ID
//        // and then create a subcollection "itemtotalPrice" inside that document
//        DocumentReference userDocRef = usersCollection.document(userId).collection("itemtotalPrice").document();
//
//        // Get the current timestamp
//        Timestamp timestamp = Timestamp.now();
//
//        // Set the data including totalNewPrice and timestamp
//        Map<String, Object> data = new HashMap<>();
//        data.put("totalNewPrice", totalNewPrice);
//        data.put("timestamp", timestamp);
//
//        // Set the totalNewPrice value along with timestamp in the "itemtotalPrice" document
//        userDocRef.set(data)
//                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Item total price updated successfully"))
//                .addOnFailureListener(e -> Log.e("Firestore", "Error updating item total price", e));
//    }

//    private void retrieveUserItemTotalPrice(String userId) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        CollectionReference usersCollection = db.collection("users");
//
//        // Retrieve the "itemtotalPrice" document for the user
//        usersCollection.document(userId).collection("itemtotalPrice").get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        QuerySnapshot querySnapshot = task.getResult();
//                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
//                            // Retrieve the latest document (assuming sorted by timestamp)
//                            DocumentSnapshot latestDocument = querySnapshot.getDocuments().get(0);
//
//                            // Retrieve totalNewPrice from the document
//                            Integer totalNewPrice = latestDocument.getLong("totalNewPrice").intValue();
//
//                            // Display the totalNewPrice in textView24
//                            textView24.setText(String.valueOf(totalNewPrice));
//                        } else {
//                            // Handle the case where there's no data in the "itemtotalPrice" subcollection
//                            textView24.setText("No data available");
//                        }
//                    } else {
//                        // Handle the error
//                        Log.e("Firestore", "Error retrieving item total price", task.getException());
//                        textView24.setText("Error retrieving data");
//                    }
//                });
//    }




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



