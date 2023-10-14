package com.example.foodapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
public class Registration extends AppCompatActivity {

    EditText editTextname,editTextemail,editTextpassword;
    Button buttonreg;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        editTextname=findViewById(R.id.name);
        editTextemail=findViewById(R.id.email);
        editTextpassword=findViewById(R.id.pass);
        buttonreg=findViewById(R.id.Btn1);
        mAuth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progressBar);

        buttonreg.setOnClickListener(new View.OnClickListener(){
           @Override
            public void onClick(View view){
               registerNewUser();
//
           }
        });
    }
    public void registerNewUser(){
        progressBar.setVisibility(View.VISIBLE);
        String name,email,password;
        name=String.valueOf(editTextname.getText());
        email=String.valueOf(editTextemail.getText());
        password=String.valueOf(editTextpassword.getText());

        Toast.makeText(getApplicationContext(),email,Toast.LENGTH_LONG).show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registration success, get the user object
                            FirebaseUser user = mAuth.getCurrentUser();

                            // Create a user document in Firestore
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            DocumentReference userRef = db.collection("users").document(user.getUid());
                            String role;

                            if (email.endsWith("@admin.com")) {
                                role = "admin";
                            } else {
                                role = "user";
                            }

                            // Add user details to Firestore
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("name", name);
                            userData.put("email",email);
                            userData.put("password",password);
                            userData.put("role", role);
                            // Add other details as needed

                            userRef.set(userData)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getApplicationContext(), "User registered successfully", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(Registration.this,Login.class);
                                            startActivity(intent);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), "Firestore error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(getApplicationContext(), "Authentication error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }


}