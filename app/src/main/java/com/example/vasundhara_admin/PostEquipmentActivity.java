package com.example.vasundhara_admin;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class PostEquipmentActivity extends AppCompatActivity {

    private EditText etEquipmentName, etEquipmentPrice, etContact, etLocation, etEquipmentDescription;
    private Spinner spinnerCategory;
    private RadioGroup rgCondition;
    private Button btnSubmitEquipment;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_equipment);

        // Initialize Views
        etEquipmentName = findViewById(R.id.etEquipmentName);
        etEquipmentPrice = findViewById(R.id.etEquipmentPrice);
        etContact = findViewById(R.id.etContact);
        etLocation = findViewById(R.id.etLocation);
        etEquipmentDescription = findViewById(R.id.etEquipmentDescription);
        rgCondition = findViewById(R.id.rgCondition);
        btnSubmitEquipment = findViewById(R.id.btnSubmitEquipment);

        // Get Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Equipments");

        // Submit Button Click Listener
        btnSubmitEquipment.setOnClickListener(view -> submitEquipment());
    }

    private void submitEquipment() {
        String name = etEquipmentName.getText().toString().trim();
        String price = etEquipmentPrice.getText().toString().trim();
        String contact = etContact.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String description = etEquipmentDescription.getText().toString().trim();

        int selectedConditionId = rgCondition.getCheckedRadioButtonId();
        if (selectedConditionId == -1) {
            Toast.makeText(this, "Please select equipment condition", Toast.LENGTH_SHORT).show();
            return;
        }
        String condition = (selectedConditionId == R.id.rbNew) ? "New" : "Used";

        // Validate Inputs
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(price) || TextUtils.isEmpty(contact) ||
                TextUtils.isEmpty(location) || TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a unique key
        String equipmentId = databaseReference.push().getKey();
        if (equipmentId == null) {
            Toast.makeText(this, "Failed to generate ID, try again", Toast.LENGTH_SHORT).show();
            return;
        }

        // Store data in HashMap
        Map<String, String> equipmentData = new HashMap<>();
        equipmentData.put("id", equipmentId);
        equipmentData.put("name", name);
        equipmentData.put("price", price);
        equipmentData.put("contact", contact);
        equipmentData.put("location", location);
        equipmentData.put("description", description);

        equipmentData.put("condition", condition);

        // Upload to Firebase
        databaseReference.child(equipmentId).setValue(equipmentData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(PostEquipmentActivity.this, "Equipment Added Successfully", Toast.LENGTH_SHORT).show();
                    clearFields();
                })
                .addOnFailureListener(e -> Toast.makeText(PostEquipmentActivity.this, "Failed to add equipment", Toast.LENGTH_SHORT).show());
    }


    private void clearFields() {
        etEquipmentName.setText("");
        etEquipmentPrice.setText("");
        etContact.setText("");
        etLocation.setText("");
        etEquipmentDescription.setText("");
        rgCondition.clearCheck();

    }
}
