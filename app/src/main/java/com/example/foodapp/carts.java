package com.example.foodapp;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class carts {
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public Task<Integer> getItemQuantityFromItemsCollection(String itemId) {
        // Reference to the "Items" collection in Firestore
        CollectionReference itemsCollection = firestore.collection("Items");

        // Query to find the document based on itemId
        Query query = itemsCollection.whereEqualTo("itemId", itemId);

        // Execute the query
        return query.get().continueWith(task -> {
            if (task.isSuccessful()) {
                // Iterate through the result documents
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Retrieve the item quantity field (replace "itemQuantity" with the actual field name)
                    Long itemQuantity = document.getLong("itemQuantity");

                    // Return the item quantity as an integer
                    if (itemQuantity != null) {
                        return itemQuantity.intValue();
                    }
                }
            }

            // Return a default value (e.g., 0) if the item is not found or an error occurs
            return 0;
        });
    }
}
