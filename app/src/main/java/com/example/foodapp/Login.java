package com.example.foodapp;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodapp.Activity.Admin2;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Login extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private Button loginButton, registerButton;
    private FirebaseAuth firebaseAuth;
    TextView forgotpassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();



        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.Btn1);
        forgotpassword=findViewById(R.id.forgot_password);
//        registerButton = findViewById(R.id.registerButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUserAccount();
            }
        });

//        registerButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                goToRegister();
//            }
//        });
    }


    private void loginUserAccount() {
        String email, password;
        email = emailEditText.getText().toString();
        password = passwordEditText.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter email...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // Retrieve the user's role from Firestore
                                retrieveUserRole(email);
                                emailEditText.setText("");
                                passwordEditText.setText("");
                            } else {
                                Toast.makeText(getApplicationContext(), "User authentication failed", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            // Get the error message
                            String errorMessage = task.getException().getMessage();
                            Toast.makeText(getApplicationContext(), "Login failed: " + errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
    private void retrieveUserRole(String email) {
        // Reference to the "users" collection in Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").whereEqualTo("email",email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                           for( QueryDocumentSnapshot document : task.getResult()) {
                               if (document.exists()) {
                                   // Get the "role" field from the document
                                   String role = document.getString("role");

                                   // Check the role and start the appropriate activity
                                   if ("admin".equals(role)) {
                                       Intent intent = new Intent(Login.this, Admin.class);
                                       startActivity(intent);
                                   } else if ("user".equals(role)) {
                                       Intent intent = new Intent(Login.this, Admin2.class);
                                       startActivity(intent);
                                   } else {
                                       Toast.makeText(getApplicationContext(), "Unknown role: " + role, Toast.LENGTH_LONG).show();
                                   }

                               } else {
                                   Toast.makeText(getApplicationContext(), "Error retrieving user data", Toast.LENGTH_LONG).show();
                               }
                           } } }
                });
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(Login.this);
                View dialogview=getLayoutInflater().inflate(R.layout.dialog_forgot,null);
                EditText emailBox=dialogview.findViewById(R.id.emailBox);


                builder.setView(dialogview);
                AlertDialog dialog=builder.create();

                dialogview.findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String useremail=emailBox.getText().toString();
                        if(TextUtils.isEmpty(useremail) && !Patterns.EMAIL_ADDRESS.matcher(useremail).matches()){
                            Toast.makeText(Login.this,"Enter your registered email id",Toast.LENGTH_LONG).show();
                            return;
                        }
                        mAuth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(Login.this,"Check Your email",Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(Login.this,"Unable to send,  failed",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });
                dialogview.findViewById(R.id.btncancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                if(dialog.getWindow() !=null){
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                dialog.show();
            }
        });
    }

//    public void forgotpassword(View view) {
//        Intent i=new Intent(Login.this,Forgotpassword.class);
//        startActivity(i);
//    }
}
