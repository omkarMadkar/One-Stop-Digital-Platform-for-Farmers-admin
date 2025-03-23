package com.example.vasundhara_admin;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class AdminHomeActivity extends AppCompatActivity {
    private static final String API_KEY = "AIzaSyCCR_eKo8KNeuhpC6JKkSlZJJTwRuy9og8"; // Replace with secured API Key
    private static final String URL = "https://translation.googleapis.com/language/translate/v2";

    private TextView usernameTextView;
    private Button btn, btnPostEquipment, btnOpenStore, btnNotifications;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home_activity); // Ensure XML file matches


        usernameTextView = findViewById(R.id.username); // Reference to username TextView
        btn = findViewById(R.id.btn1);
        btnPostEquipment = findViewById(R.id.btn_post_equipment);
        btnOpenStore = findViewById(R.id.openStore);
        Button btnNotifications = findViewById(R.id.btnNotifications);

        // Fetch Admin Name from Firebase
        fetchAdminName();

        // Set Click Listeners for Navigation
        btnPostEquipment.setOnClickListener(view -> openActivity(PostEquipmentActivity.class));
        btnOpenStore.setOnClickListener(view -> openActivity(OpenStore.class));

        btnNotifications.setOnClickListener(v -> openActivity(Notification.class));


        // Translate button labels when clicked
        btn.setOnClickListener(v -> translateText("Post Equipment", btnPostEquipment));
    }

    private void openActivity(Class<?> activityClass) {
        Intent intent = new Intent(AdminHomeActivity.this, activityClass);
        startActivity(intent);
    }

    // Fetch Admin Name from Firebase Database
    private void fetchAdminName() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            return;
        }

        String adminId = user.getUid(); // Get Admin ID
        Log.d("FirebaseDebug", "Admin ID: " + adminId); // Debugging Log

        DatabaseReference adminRef = FirebaseDatabase.getInstance()
                .getReference("Admin")
                .child(adminId)
                .child("name");

        adminRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String adminName = snapshot.getValue(String.class);
                    Log.d("FirebaseDebug", "Admin Name: " + adminName); // Debugging Log
                    usernameTextView.setText(adminName);
                } else {
                    Log.e("FirebaseDebug", "Admin Name does not exist");
                    usernameTextView.setText("Admin");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("FirebaseDebug", "Firebase Error: " + error.getMessage());
                Toast.makeText(AdminHomeActivity.this, "Failed to load Admin Name", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Function to handle translation
    private void translateText(String textToTranslate, Button targetButton) {
        new TranslateTextTask(targetButton).execute(textToTranslate, "hi"); // Hindi translation
    }

    // AsyncTask for API call
    private class TranslateTextTask extends AsyncTask<String, Void, String> {
        private final Button targetButton;

        public TranslateTextTask(Button targetButton) {
            this.targetButton = targetButton;
        }

        @Override
        protected String doInBackground(String... params) {
            String text = params[0];
            String targetLang = params[1];

            try {
                String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8.toString());
                String requestUrl = URL + "?q=" + encodedText + "&target=" + targetLang + "&key=" + API_KEY;

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(requestUrl).get().build();

                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful() && response.body() != null) {
                        String jsonResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(jsonResponse);
                        return jsonObject.getJSONObject("data")
                                .getJSONArray("translations")
                                .getJSONObject(0)
                                .getString("translatedText");
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return "Translation Failed!";
        }

        @Override
        protected void onPostExecute(String translatedText) {
            targetButton.setText(translatedText);
        }
    }
}
