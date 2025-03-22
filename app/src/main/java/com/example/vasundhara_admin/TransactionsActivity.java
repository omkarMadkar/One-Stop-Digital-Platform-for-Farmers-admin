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

public class TransactionsActivity extends AppCompatActivity {

    private ListView listViewTransactions;
    private List<String> transactionsList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        listViewTransactions = findViewById(R.id.listViewTransactions);
        transactionsList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Transactions");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                transactionsList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String transaction = dataSnapshot.getValue(String.class);
                    transactionsList.add(transaction);
                }
                // Display transactions in ListView (Adapter implementation required)
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}