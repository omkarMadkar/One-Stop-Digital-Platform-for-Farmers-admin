package com.example.vasundhara_admin;

import android.content.Intent;
import android.os.Bundle;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class AdminHomeActivity extends AppCompatActivity {
    private static final String API_KEY = "AIzaSyCCR_eKo8KNeuhpC6JKkSlZJJTwRuy9og8"; // Replace with a secured API Key
    private static final String URL = "https://translation.googleapis.com/language/translate/v2";

    private TextView translatedTextView;
    private Button btn, btnPostEquipment, btnTransactions, btnNotifications, btnFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home_activity); // Ensure XML file matches

        translatedTextView = findViewById(R.id.translatedTextView);
        btn = findViewById(R.id.btn1);
        btnPostEquipment = findViewById(R.id.btn_post_equipment);
        btnTransactions = findViewById(R.id.btn_transactions);
        btnNotifications = findViewById(R.id.btn_notifications);
        btnFeedback = findViewById(R.id.btn_feedback);

        // Set Click Listeners for Navigation
        btnPostEquipment.setOnClickListener(view -> openActivity(PostEquipmentActivity.class));
        btnTransactions.setOnClickListener(view -> openActivity(TransactionsActivity.class));
        btnNotifications.setOnClickListener(view -> openActivity(NotificationsActivity.class));
        btnFeedback.setOnClickListener(view -> openActivity(FeedbackActivity.class));

        // Translate button labels when clicked
        btn.setOnClickListener(v -> {
            translateText("Post Equipment", btnPostEquipment);
            translateText("Transactions", btnTransactions);
            translateText("Notifications", btnNotifications);
            translateText("Feedback", btnFeedback);
        });
    }

    private void openActivity(Class<?> activityClass) {
        Intent intent = new Intent(AdminHomeActivity.this, activityClass);
        startActivity(intent);
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
