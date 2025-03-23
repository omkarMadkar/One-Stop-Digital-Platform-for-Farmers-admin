package com.example.vasundhara_admin;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vasundhara_admin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;

public class OpenStore extends AppCompatActivity {

    private EditText etProductName, etPrice, etContact, etLocation, etstorename;
    private Button btnSubmit;
    private DatabaseReference databaseReference;
    private String adminId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_store);

        // Initialize Firebase
        adminId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Get logged-in admin ID
        databaseReference = FirebaseDatabase.getInstance().getReference("Admin").child(adminId).child("Store");

        // Initialize UI Components
        etProductName = findViewById(R.id.etProductName);
        etPrice = findViewById(R.id.etPrice);
        etContact = findViewById(R.id.etContact);
        etLocation = findViewById(R.id.etLocation);
        btnSubmit = findViewById(R.id.btnSubmit);
        etstorename = findViewById(R.id.etStoreName);


        // Handle Submit Button Click
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitProduct();
            }
        });
    }

    private void submitProduct() {
        // Fetch user inputs
        String productName = etProductName.getText().toString().trim();
        String price = etPrice.getText().toString().trim();
        String contact = etContact.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String storename = etstorename.getText().toString().trim();

        // Validate Inputs
        if (TextUtils.isEmpty(productName) || TextUtils.isEmpty(price)
                || TextUtils.isEmpty(contact) || TextUtils.isEmpty(location)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate unique product ID
        String productId = databaseReference.push().getKey();

        // Create a data map
        Map<String, String> productData = new HashMap<>();
        productData.put("id", productId);
        productData.put("name", productName);
        productData.put("price", price);
        productData.put("contact", contact);
        productData.put("location", location); // Store manual location input
        productData.put("Store Name", storename);

        // Store data in Firebase
        if (productId != null) {
            databaseReference.child(productId).setValue(productData);
            Toast.makeText(this, "Product added successfully!", Toast.LENGTH_SHORT).show();
            clearFields();
        }
    }

    private void clearFields() {
        etProductName.setText("");
        etPrice.setText("");
        etContact.setText("");
        etLocation.setText("");
        etstorename.setText("");
    }
}
