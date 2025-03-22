package com.example.vasundhara_admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class AdminHomeActivity extends AppCompatActivity {

    private Button btnPostEquipment, btnTransactions, btnNotifications, btnFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home_activity); // Ensure XML file matches

        // Initialize buttons
        btnPostEquipment = findViewById(R.id.btn_post_equipment);
        btnTransactions = findViewById(R.id.btn_transactions);
        btnNotifications = findViewById(R.id.btn_notifications);
        btnFeedback = findViewById(R.id.btn_feedback);

        // Set Click Listeners
        btnPostEquipment.setOnClickListener(view -> openActivity(PostEquipmentActivity.class));
        btnTransactions.setOnClickListener(view -> openActivity(TransactionsActivity.class));
        btnNotifications.setOnClickListener(view -> openActivity(NotificationsActivity.class));
        btnFeedback.setOnClickListener(view -> openActivity(FeedbackActivity.class));
    }

    // Method to navigate to new activities
    private void openActivity(Class<?> activityClass) {
        Intent intent = new Intent(AdminHomeActivity.this, activityClass);
        startActivity(intent);
    }
}
