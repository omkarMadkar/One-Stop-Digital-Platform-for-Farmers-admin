package com.example.vasundhara_admin;

import android.os.Bundle;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class FeedbackActivity extends AppCompatActivity {

    private ListView listViewFeedback;
    private List<String> feedbackList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

//        listViewFeedback = findViewById(R.id.listViewFeedback);
//        feedbackList = new ArrayList<>();
//        databaseReference = FirebaseDatabase.getInstance().getReference("Feedback");
//
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                feedbackList.clear();
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    String feedback = dataSnapshot.getValue(String.class);
//                    feedbackList.add(feedback);
//                }
//                // Display feedback in ListView (Adapter implementation required)
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {}
//        });
    }
}