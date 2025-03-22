package com.example.vasundhara_admin;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostEquipmentActivity extends AppCompatActivity {

    private EditText etName, etPrice, etDescription;
    private Button btnPost;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_equipment);

        etName = findViewById(R.id.etName);
        etPrice = findViewById(R.id.etPrice);
        etDescription = findViewById(R.id.etDescription);
        btnPost = findViewById(R.id.btnPost);

        databaseReference = FirebaseDatabase.getInstance().getReference("Equipment");

        btnPost.setOnClickListener(view -> postEquipment());
    }

    private void postEquipment() {
        String name = etName.getText().toString().trim();
        String price = etPrice.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(price) || TextUtils.isEmpty(description)) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        String id = databaseReference.push().getKey();
        Equipment equipment = new Equipment(id, name, price, description);
        databaseReference.child(id).setValue(equipment)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Equipment Posted!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Failed to post!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static class Equipment {
        public String id, name, price, description;
        public Equipment() {}
        public Equipment(String id, String name, String price, String description) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.description = description;
        }
    }
}