package com.example.foodapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodapp.Adapter.CategoryAdapter;
import com.example.foodapp.Adapter.FastDeliveryAdapter;
import com.example.foodapp.Cart;
import com.example.foodapp.Domain.CategoryDomain;
import com.example.foodapp.MyAdapter;
import com.example.foodapp.R;
import com.example.foodapp.User;
import com.example.foodapp.User_profile;
import com.example.foodapp.fastDeliveryDomain;
import com.example.foodapp.order;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Admin2 extends AppCompatActivity {
    private RecyclerView.Adapter adapter,adapter2;
    private RecyclerView recyclerViewCategory,recyclerViewPopularList;
    ArrayList<fastDeliveryDomain> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin2);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        recyclerViewPopularList=findViewById(R.id.view2);
        recyclerViewPopularList.setHasFixedSize(true);
        recyclerViewPopularList.setLayoutManager(new LinearLayoutManager(this));

        list =new ArrayList<>();
        adapter2=new FastDeliveryAdapter(this,list);
        recyclerViewPopularList.setAdapter(adapter2);


        recyclerViewCategory();
        setRecyclerViewPopularList();
        // Assuming you have a TextView with ID "welcomeMessage" in your layout
        TextView welcomeMessageTextView = findViewById(R.id.textView);

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
                                welcomeMessageTextView.setText("Hi " + userName);
                            } else {
                                // Handle the case where no documents match the query
                                Toast.makeText(Admin2.this, "User document does not exist for email: " + userEmail, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Handle any errors that occur during the query
                            Toast.makeText(Admin2.this, "Error querying user data: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }

    private void recyclerViewCategory() {
        LinearLayoutManager LinearLayoutManager=new LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL,false);
        recyclerViewCategory=findViewById(R.id.view1);
        recyclerViewCategory.setLayoutManager(LinearLayoutManager);

        ArrayList<CategoryDomain>categoryList=new ArrayList<>();
        categoryList.add(new CategoryDomain("Non-Veg","cat_1"));
        categoryList.add(new CategoryDomain("Veg","cat_2"));
        categoryList.add(new CategoryDomain("Beverages","cat_3"));
        categoryList.add(new CategoryDomain("Fast Food","cat_4"));

        adapter=new CategoryAdapter(categoryList);
        recyclerViewCategory.setAdapter(adapter);
    }
    private void setRecyclerViewPopularList(){
        FirebaseApp.initializeApp(this);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("items");

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        fastDeliveryDomain item = document.toObject(fastDeliveryDomain.class);


                        list.add(item);
                    }
                    adapter2.notifyDataSetChanged();
                } else {
                    // Handle the case when there is no data in the collection
                    Log.d("Firebase", "No documents found in the 'items' collection.");

                }
            } else {
                // Handle the error
                Exception e = task.getException();
                e.printStackTrace();
                Log.e("Firebase", "Error retrieving data from Firestore: " + e.getMessage());
            }
        });
    }

    public void userprofile(View view) {
        Intent i = new Intent(getApplicationContext(), User_profile.class);
        startActivity(i);
    }

    public void cart(View view) {
        Intent i = new Intent(getApplicationContext(), Cart.class);
        startActivity(i);
    }

    public void Orders(View view) {
        Intent i = new Intent(getApplicationContext(), order.class);
        startActivity(i);
    }
}
