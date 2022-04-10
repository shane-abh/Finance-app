package com.example.mainproject2.ExpenseManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.mainproject2.PaymentReminder.DashBoardActivity;
import com.example.mainproject2.ProfileActivity;
import com.example.mainproject2.R;
import com.example.mainproject2.StockPlatform.MyPortfolioActivity;
import com.example.mainproject2.StockPlatform.RealTimePriceAlert;
import com.example.mainproject2.StockPlatform.Search;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExpenseCalculatorMain extends AppCompatActivity {

    PieChart pieChart;
//    TransactionAdapter adapter;
    ArrayList<FinancialsClass> transactionList;
    TextView details;

    double income,expense;


    FirebaseDatabase db = FirebaseDatabase.getInstance();

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    WebView webView;


    JSONArray date = new JSONArray();
    JSONArray balance = new JSONArray();

    FirebaseAdapter fb;
    String key = null;

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView nav_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_calculator_main);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);


        nav_view = findViewById(R.id.nav_view);
        Navigation_drawer();
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Expense Manager");


        details = findViewById(R.id.Details);
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ExpenseDetails.class);
                startActivity(intent);
            }
        });

        pieChart = findViewById(R.id.pieChart_view);

        initPieChart();
        transactionList = new ArrayList<FinancialsClass>();

        fb = new FirebaseAdapter();

        loadfbData();


        webView = findViewById(R.id.webview);
//        webView.loadUrl("file:///android_asset/graph.html");
//
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setAllowContentAccess(true);
//        webView.getSettings().setAllowFileAccess(true);
//        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//
//        webView.getSettings().setAppCacheEnabled(true);
//        webView.getSettings().setDatabaseEnabled(true);
//
//
//        webView.getSettings().setAllowFileAccessFromFileURLs(true);
//        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
//        webView.getSettings().setDomStorageEnabled(true);

    }

    private void initPieChart(){
        //using percentage as values instead of amount
        pieChart.setUsePercentValues(true);

        //remove the description label on the lower left corner, default true if not set
        pieChart.getDescription().setEnabled(true);
        pieChart.getLegend().setTextColor(Color.BLACK);

//        pieChart.spin( 500,0,-360f, Easing.EaseInOutQuad);
//        pieChart.setCenterText("");
//        pieChart.setCenterTextSize(18);
//        pieChart.setCenterTextColor(Color.WHITE);
        //enabling the user to rotate the chart, default true
        pieChart.setRotationEnabled(true);
        //adding friction when rotating the pie chart
        pieChart.setDragDecelerationFrictionCoef(0.9f);
        //setting the first entry start from right hand side, default starting from top
        pieChart.setRotationAngle(0);

        //highlight the entry when it is tapped, default true if not set
        pieChart.setHighlightPerTapEnabled(true);

        //setting the color of the hole in the middle, default white

        pieChart.setHoleColor(Color.parseColor("#000000"));
        pieChart.setHoleRadius(20);
        pieChart.setTransparentCircleRadius(55);
        pieChart.setBackgroundColor(Color.parseColor("#EDEDED"));


    }

    public void loadfbData(){
        Query q = db.getReference().child("Users").child(user.getUid()).child(user.getDisplayName()).child("Expense Management").orderByChild("date");

        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                transactionList.clear();
                ArrayList<PieEntry> pieEntries = new ArrayList<>();
                Map<String, Double> typeAmountMap = new HashMap<>();


                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    FinancialsClass t = dataSnapshot.getValue(FinancialsClass.class);
                    key = dataSnapshot.getKey();
                    t.setKey(key);
                    transactionList.add(t);

                    date.put(t.getDate());
                    balance.put(t.getBalance());

                    typeAmountMap.put("Income", calculateIncome(transactionList));
                    typeAmountMap.put("Expense", calculateExpense(transactionList));
                    System.out.println(t.getCategory());


                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl("file:///android_asset/trial.html");

                        webView.getSettings().setJavaScriptEnabled(true);
                        webView.getSettings().setAllowContentAccess(true);
                        webView.getSettings().setAllowFileAccess(true);
                        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

                        webView.getSettings().setAppCacheEnabled(true);
                        webView.getSettings().setDatabaseEnabled(true);


                        webView.getSettings().setAllowFileAccessFromFileURLs(true);
                        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
                        webView.getSettings().setDomStorageEnabled(true);

                        webView.setWebViewClient(new WebViewClient() {

                            public void onPageFinished(WebView view, String url) {
                                view.loadUrl("javascript:init("+date+","+balance+")");
                            }
                        });
                        System.out.println("Date: "+date);
                    }
                });

                String label = "";
                //initializing colors for the entries
                ArrayList<Integer> colors = new ArrayList<>();
                colors.add(Color.parseColor("#F7004C"));
                colors.add(Color.parseColor("#25F700"));


                for(String type: typeAmountMap.keySet()){
                    pieEntries.add(new PieEntry(typeAmountMap.get(type).floatValue(), type));
                }

                //collecting the entries with label name
                PieDataSet pieDataSet = new PieDataSet(pieEntries,label);
                //setting text size of the value
                pieDataSet.setValueTextSize(12f);
                //providing color list for coloring different entries
                pieDataSet.setColors(colors);
                //grouping the data set from entry to chart
                PieData pieData = new PieData(pieDataSet);
                //showing the value of the entries, default true if not set
                pieData.setDrawValues(true);
                pieData.setValueFormatter(new PercentFormatter());

                pieChart.setData(pieData);
                pieChart.invalidate();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    public double calculateBalance(ArrayList<FinancialsClass> transactionList)
    {
        double bal = 0;
        for(FinancialsClass transaction : transactionList)
        {
            if(transaction.isPositive())
            {
                bal+=Double.parseDouble(transaction.getAmount());
            }
            else {
                bal-=Double.parseDouble(transaction.getAmount());
            }
        }
        return bal;
    }

    public double calculateIncome(ArrayList<FinancialsClass> transactionList)
    {

        income = 0;
        for(FinancialsClass transaction : transactionList)
        {
            if(transaction.isPositive())
            {

                income+= Double.parseDouble(transaction.getAmount());
            }

        }
        return income;
    }

    public  double calculateExpense(ArrayList<FinancialsClass> transactionList)
    {
        int bal = 0;
        expense = 0;
        for(FinancialsClass transaction : transactionList)
        {
            if(transaction.isPositive())
            {


            }
            else {

                expense+= Double.parseDouble(transaction.getAmount());
            }
        }
        return expense;
    }

    public void Navigation_drawer(){
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.nav_Home:
                        Intent intent1 = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(intent1);
                        return true;

                    case R.id.nav_search_stock:
                        Intent intent2 = new Intent(getApplicationContext(), Search.class);
                        startActivity(intent2);

                    case R.id.nav_portfolio:
                        Intent intent3 = new Intent(getApplicationContext(), MyPortfolioActivity.class);
                        startActivity(intent3);
                        return true;

                    case R.id.nav_expense:
                        Intent intent4 = new Intent(getApplicationContext(), ExpenseCalculatorMain.class);
                        startActivity(intent4);
                        return true;

                    case R.id.nav_priceAlert:
                        Intent intent5 = new Intent(getApplicationContext(), RealTimePriceAlert.class);
                        startActivity(intent5);
                        return true;

                    case R.id.nav_reminder:
                        Intent intent6 = new Intent(getApplicationContext(), DashBoardActivity.class);
                        startActivity(intent6);
                        return true;


                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}