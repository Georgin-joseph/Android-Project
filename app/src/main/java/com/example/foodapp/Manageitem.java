package com.example.foodapp;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import com.example.foodapp.manageitems;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

public class Manageitem extends AppCompatActivity {

    RecyclerView recyclerViewNonVeg, recyclerViewVeg, recyclerViewBeverage, recyclerViewFastFood; // Separate RecyclerViews for "Non-Veg" and "Veg"
    MyAdapter myAdapterNonVeg, myAdapterVeg, myAdapterBeverage, myAdapterFastFood; // Separate Adapters for "Non-Veg" and "Veg"
    ArrayList<manageitems> itemsNonVeg, itemsVeg, itemsBeverage, itemsFastFood; // Separate ArrayLists for "Non-Veg" and "Veg"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manageitem);

        recyclerViewNonVeg = findViewById(R.id.itemlist);
        recyclerViewVeg = findViewById(R.id.itemlist1);
        recyclerViewBeverage=findViewById(R.id.itemlist2);
        recyclerViewFastFood=findViewById(R.id.itemlist3);

        recyclerViewNonVeg.setHasFixedSize(true);
        recyclerViewVeg.setHasFixedSize(true);
        recyclerViewBeverage.setHasFixedSize(true);
        recyclerViewFastFood.setHasFixedSize(true);


        recyclerViewNonVeg.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewVeg.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewBeverage.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewFastFood.setLayoutManager(new LinearLayoutManager(this));

        itemsNonVeg = new ArrayList<>();
        itemsVeg = new ArrayList<>();
        itemsBeverage=new ArrayList<>();
        itemsFastFood=new ArrayList<>();

        myAdapterNonVeg = new MyAdapter(this, itemsNonVeg);
        myAdapterVeg = new MyAdapter(this, itemsVeg);
        myAdapterBeverage=new MyAdapter(this,itemsBeverage);
        myAdapterFastFood=new MyAdapter(this,itemsFastFood);

        recyclerViewNonVeg.setAdapter(myAdapterNonVeg);
        recyclerViewVeg.setAdapter(myAdapterVeg);
        recyclerViewBeverage.setAdapter(myAdapterBeverage);
        recyclerViewFastFood.setAdapter(myAdapterFastFood);

        FirebaseApp.initializeApp(this);

        retrieveItemsByCategory("Non-Veg", itemsNonVeg, myAdapterNonVeg);
        retrieveItemsByCategory("Veg", itemsVeg, myAdapterVeg);
        retrieveItemsByCategory("Beverages",itemsBeverage,myAdapterBeverage);
        retrieveItemsByCategory("Fast Food",itemsBeverage,myAdapterBeverage);
    }

    private void retrieveItemsByCategory(String category, ArrayList<manageitems> items, MyAdapter adapter) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("items").whereEqualTo("category", category);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        manageitems item = document.toObject(manageitems.class);
                        items.add(item);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d("Firebase", "No " + category + " documents found in the 'items' collection.");
                }
            } else {
                Exception e = task.getException();
                e.printStackTrace();
                Log.e("Firebase", "Error retrieving " + category + " data from Firestore: " + e.getMessage());
            }
        });
    }
}

