package com.example.mainproject2.ExpenseManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anychart.AnyChartView;
import com.example.mainproject2.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExpenseDetails extends AppCompatActivity {

    public static TextView tvEmpty;
    EditText etAmount, etMessage;
    //    ImageView ivSend;
    boolean positive = true;
    RecyclerView rvTransactions;
    TransactionAdapter adapter;
    ArrayList<FinancialsClass> transactionList;
    TextView tvBalance;

    FirebaseAdapter fb;
    String key = null;

    boolean isLoading=false;

    public static double income = 0;
    public static double expense= 0;
    AnyChartView anyChartView;
    PieChart pieChart;

    AppCompatButton pbs;

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_details);

        initViews();

        fb = new FirebaseAdapter();

        checkIfEmpty(transactionList.size());
        pieChart = findViewById(R.id.pieChart_view);
        initPieChart();

        pbs = findViewById(R.id.pbs);

        getSupportActionBar().setTitle("Expense Manager");

        rvTransactions.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        rvTransactions.setLayoutManager(layoutManager);

        adapter = new TransactionAdapter(this,transactionList);
        rvTransactions.setAdapter(adapter);

        fab = findViewById(R.id.fab);
        tvBalance = findViewById(R.id.tvBalance);

        loadfbData();

        pbs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PersonalBalanceStatement.class);
                startActivity(intent);
            }
        });


        rvTransactions.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)
            {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItem = linearLayoutManager.getItemCount();
                int lastVisible = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if(totalItem< lastVisible+5)
                {
                    if(!isLoading)
                    {
                        isLoading=true;
                        loadfbData();
                    }
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddFinancials.class);
                startActivity(intent);
            }
        });

    }


    // Initializing Views
    private void initViews() {
        transactionList = new ArrayList<FinancialsClass>();
//        tvSign = findViewById(R.id.tvSign);
        rvTransactions = findViewById(R.id.rvTransactions);
//        etAmount = findViewById(R.id.etAmount);
//        etMessage = findViewById(R.id.etMessage);
//        ivSend = findViewById(R.id.ivSend);
//        tvEmpty = findViewById(R.id.tvEmpty);
    }
    private void initPieChart(){
        //using percentage as values instead of amount
        pieChart.setUsePercentValues(true);

        //remove the description label on the lower left corner, default true if not set
        pieChart.getDescription().setEnabled(true);
        pieChart.getLegend().setTextColor(Color.BLACK);

//        pieChart.spin( 500,0,-360f, Easing.EaseInOutQuad);
        pieChart.setCenterText("Expenses");
        pieChart.setCenterTextSize(18);
        pieChart.setCenterTextColor(Color.WHITE);
        //enabling the user to rotate the chart, default true
        pieChart.setRotationEnabled(true);
        //adding friction when rotating the pie chart
        pieChart.setDragDecelerationFrictionCoef(0.9f);
        //setting the first entry start from right hand side, default starting from top
        pieChart.setRotationAngle(0);

        //highlight the entry when it is tapped, default true if not set
        pieChart.setHighlightPerTapEnabled(true);
        //adding animation so the entries pop up from 0 degree
//        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        //setting the color of the hole in the middle, default white
        pieChart.setHoleColor(Color.parseColor("#000000"));
        pieChart.setHoleRadius(60);
        pieChart.setBackgroundColor(Color.parseColor("#EDEDED"));

    }

    public static void checkIfEmpty(int size) {
        if (size == 0)
        {
            tvEmpty.setVisibility(View.VISIBLE);
        }
        else {
            tvEmpty.setVisibility(View.GONE);
        }
    }

    public  void setBalance(ArrayList<FinancialsClass> transactionList){
        double bal = calculateBalance(transactionList);

        if(bal<=0)
        {
            tvBalance.setText("- AED "+calculateBalance(transactionList)*-1);
            Toast.makeText(getApplicationContext(),"Insufficient Balance",Toast.LENGTH_SHORT).show();
        }
        else {
            tvBalance.setText("+ AED "+calculateBalance(transactionList));

        }
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



    public void loadfbData(){
        fb.get(key).addValueEventListener(new ValueEventListener() {
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
                    if(!t.isPositive()) {
                        typeAmountMap.put(t.getCategory(), Double.parseDouble(String.valueOf(t.getAmount())));
                        System.out.println(t.getCategory());
                    }

                }
                setBalance(transactionList);


                String label = "type";
                //initializing colors for the entries
                ArrayList<Integer> colors = new ArrayList<>();
                colors.add(Color.parseColor("#006FFF"));
                colors.add(Color.parseColor("#13F4EF"));
                colors.add(Color.parseColor("#68FF00"));
                colors.add(Color.parseColor("#FAFF00"));
                colors.add(Color.parseColor("#FFBF00"));
                colors.add(Color.parseColor("#FF005C"));
                colors.add(Color.parseColor("#3ca567"));

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

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}


