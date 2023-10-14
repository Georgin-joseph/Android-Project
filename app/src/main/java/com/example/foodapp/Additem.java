package com.example.foodapp;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.example.foodapp.Item; // Assuming Item class is in a separate file

import java.io.ByteArrayOutputStream;

public class Additem extends Activity {

    private EditText editTextItemId;
    private EditText editTextItemName;
    private Spinner spinnerCategory;
    private EditText editTextItemPrice;
    private EditText editTextItemQuantity;
    private EditText editTextItemDescription;
    private Button btnUploadImage;
    private Button btnCancel;
    private Button btnAddItem;
    private ImageView imageView;

    private static final int REQUEST_IMAGE = 1;

    private FirebaseFirestore db; // Firestore reference
    private FirebaseStorage storage; // Firebase Storage reference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additem);

        editTextItemId = findViewById(R.id.ItemId);
        editTextItemName = findViewById(R.id.ItemName);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        editTextItemPrice = findViewById(R.id.editTextItemPrice);
        editTextItemQuantity = findViewById(R.id.editTextItemQuantity);
        editTextItemDescription = findViewById(R.id.editTextItemDescription);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        btnCancel = findViewById(R.id.btnCancel);
        btnAddItem = findViewById(R.id.btnAddItem);
        imageView = findViewById(R.id.imageView);

        String[] categories = {"Veg", "Non-Veg", "Cake", "Beverages", "Fast Food"};

        // Create an ArrayAdapter to bind the data to the Spinner
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the ArrayAdapter to the Spinner
        spinnerCategory.setAdapter(categoryAdapter);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize Firebase Storage
        storage = FirebaseStorage.getInstance();

        // Set click listeners for buttons
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle Cancel button click
                finish();
            }
        });

        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent to select an image from local storage
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle Add Item button click
                final String itemId = editTextItemId.getText().toString();
                final String itemName = editTextItemName.getText().toString();
                final String category = spinnerCategory.getSelectedItem().toString();
                final String itemPrice = editTextItemPrice.getText().toString();
                final String itemQuantity = editTextItemQuantity.getText().toString();
                final String itemDescription = editTextItemDescription.getText().toString();

                // Create a new item object without imageUrl
                final Item item = new Item(itemId, itemName, category, itemPrice, itemQuantity, itemDescription, "");

                // Define a reference to the Firebase Cloud Storage location where you want to store the image.
                String imagePath = "images/" + itemId + ".jpg";
                final StorageReference imageRef = storage.getReference().child(imagePath);

                // Check if the itemId already exists in Firestore
                db.collection("items")
                        .whereEqualTo("itemId", itemId)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                if (task.getResult() != null && !task.getResult().isEmpty()) {
                                    // An item with the same itemId already exists
                                    Toast.makeText(Additem.this, "Item with this ID already exists", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Get the image as a Bitmap (assuming you have already selected it) and convert it to a byte array
                                    Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                    byte[] imageData = baos.toByteArray();

                                    // Upload the image to Firebase Cloud Storage
                                    UploadTask uploadTask = imageRef.putBytes(imageData);

                                    // Add a success listener to handle the image upload completion
                                    uploadTask.addOnSuccessListener(taskSnapshot -> {
                                        // The image has been successfully uploaded
                                        // Now, add the item to Firestore, including the image URL
                                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                            String imageUrl = uri.toString();
                                            item.setImageUrl(imageUrl); // Set the imageUrl

                                            // Add the item to Firestore
                                            db.collection("items")
                                                    .add(item)
                                                    .addOnSuccessListener(documentReference -> {
                                                        Toast.makeText(Additem.this, "Item added successfully", Toast.LENGTH_SHORT).show();
                                                        Log.d("Upload", "Image and item added successfully");
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(Additem.this, "Error adding item: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        Log.e("Upload", "Error adding item: " + e.getMessage());
                                                    });
                                        });
                                    });
                                }
                            } else {
                                Log.e("Firestore", "Error checking itemId uniqueness: " + task.getException());
                            }
                        });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            try {
                // Convert the selected image to a Bitmap and display it in an ImageView
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
