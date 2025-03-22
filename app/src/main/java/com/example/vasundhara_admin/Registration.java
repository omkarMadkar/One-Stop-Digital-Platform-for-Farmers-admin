package com.example.vasundhara_admin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {

    private EditText etName, etEmail, etPassword;
    private AppCompatButton btnRegister;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(Registration.this, AdminHomeActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_registration);

        databaseReference = FirebaseDatabase.getInstance().getReference("Admin");

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        String userId = user.getUid();
                        Admin admin = new Admin(name, email);

                        databaseReference.child(userId).setValue(admin)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Toast.makeText(Registration.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Registration.this, AdminHomeActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(Registration.this, "Failed to save data", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(Registration.this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static class Admin {
        public String name, email;
        public Admin() {}
        public Admin(String name, String email) {
            this.name = name;
            this.email = email;
        }
    }
}

//package com.example.vasundhara_admin;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.AppCompatButton;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//public class Registration extends AppCompatActivity {
//
//    private EditText etName, etEmail, etPassword;
//    private AppCompatButton btnRegister;
//    private FirebaseAuth mAuth;
//    private DatabaseReference databaseReference;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        // Check if the user is already logged in
//        mAuth = FirebaseAuth.getInstance();
//        if (mAuth.getCurrentUser() != null) {
//            // User already registered, navigate to MainActivity
//            startActivity(new Intent(Registration.this, AdminHomeActivity.class));
//            finish(); // Prevent returning to registration screen
//            return;
//        }
//
//        // Set layout
//        setContentView(R.layout.activity_registration);
//
//        // Initialize Firebase and set reference to "Admin" node
//        databaseReference = FirebaseDatabase.getInstance().getReference("Admin");
//
//        // Initialize UI Elements
//        etName = findViewById(R.id.etName);
//        etEmail = findViewById(R.id.etEmail);
//        etPassword = findViewById(R.id.etPassword);
//        btnRegister = findViewById(R.id.btnRegister);
//
//        // Register Button Click
//        btnRegister.setOnClickListener(v -> registerUser());
//    }
//
//    private void registerUser() {
//        String name = etName.getText().toString().trim();
//        String email = etEmail.getText().toString().trim();
//        String password = etPassword.getText().toString().trim();
//
//        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
//            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Firebase Authentication (Email & Password Signup)
//        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                // Save user data in "Admin" node instead of "Users"
//                String userId = mAuth.getCurrentUser().getUid();
//                Admin admin = new Admin(name, email);
//
//                databaseReference.child(userId).setValue(admin).addOnCompleteListener(task1 -> {
//                    if (task1.isSuccessful()) {
//                        Toast.makeText(Registration.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
//
//                        // Navigate to MainActivity after registration
//                        startActivity(new Intent(Registration.this, AdminHomeActivity.class));
//                        finish(); // Prevent user from going back to registration screen
//                    } else {
//                        Toast.makeText(Registration.this, "Failed to save data", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            } else {
//                Toast.makeText(Registration.this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//
//            }
//        });
//    }
//
//    // Admin Model Class
//    public static class Admin {
//        public String name, email;
//        public Admin() {}  // Default constructor (Required for Firebase)
//        public Admin(String name, String email) {
//            this.name = name;
//            this.email = email;
//        }
//    }
//}
