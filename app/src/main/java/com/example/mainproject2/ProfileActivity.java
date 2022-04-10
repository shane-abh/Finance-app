package com.example.mainproject2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mainproject2.ExpenseManager.ExpenseCalculatorMain;
import com.example.mainproject2.ExpenseManager.TrialActivity;
import com.example.mainproject2.PaymentReminder.DashBoardActivity;
import com.example.mainproject2.StockPlatform.RealTimePriceAlert;
import com.example.mainproject2.StockPlatform.Search;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {

    CardView expenseCalculator, investmentPlatform, realTimeAlert, paymentReminders,account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setTitle("Home");
//        getSupportActionBar().setLogo(R.drawable.home);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        expenseCalculator = findViewById(R.id.expenseCalculator);
        investmentPlatform = findViewById(R.id.investmentPlatform);
        realTimeAlert = findViewById(R.id.realtimeAlert);
        paymentReminders = findViewById(R.id.paymentReminders);
        account = findViewById(R.id.account);

        expenseCalculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ExpenseCalculatorMain.class);
                startActivity(intent);
            }
        });

        investmentPlatform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Search.class);
                startActivity(intent);
            }
        });

        paymentReminders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DashBoardActivity.class);
                startActivity(intent);
            }
        });

        realTimeAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RealTimePriceAlert.class);
                startActivity(intent);
            }
        });

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
                startActivity(intent);
            }
        });




    }
}