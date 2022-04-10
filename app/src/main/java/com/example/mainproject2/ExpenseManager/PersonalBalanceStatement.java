package com.example.mainproject2.ExpenseManager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import com.example.mainproject2.R;

public class PersonalBalanceStatement extends AppCompatActivity {
    TableLayout table;
    FirebaseAdapter fb;
    ArrayList<FinancialsClass> transactionList;
    String key = null;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_balance_statement);


        getSupportActionBar().setTitle("Personal Balance Statement");

        table = findViewById(R.id.tableInvoices);
        fb = new FirebaseAdapter();
        transactionList = new ArrayList<FinancialsClass>();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference();


        

        loadStatement();
        firebasetrial();

//        LinearLayout mainLayout = findViewById(R.id.mainLayout);
//        mainLayout.addView(table);

    }

    public void loadStatement() {
        fb.get(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                transactionList.clear();

                Map<String, Double> expenses = new HashMap<>();
                Map<String, Double> incomes = new HashMap<>();


                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    FinancialsClass t = dataSnapshot.getValue(FinancialsClass.class);
                    key = dataSnapshot.getKey();
                    t.setKey(key);
                    transactionList.add(t);


                    if (!t.isPositive()) {
                        expenses.put(t.getCategory(), calculateIncome(transactionList,t.getCategory()));
                        System.out.println(t.getCategory());
                    } else {
                        incomes.put(t.getCategory(), calculateIncome(transactionList,t.getCategory()));
                    }



                }
                System.out.println("Incomes: "+incomes);

                int headingSize = 30;
                int contentSize = 20;
                int totalSumSize = 22;
                int contentColor = Color.BLACK;
                int headingColor = Color.BLACK;



                TableRow incomeHeadingRow = new TableRow(getApplicationContext());
                TextView incomeHeading = new TextView(getApplicationContext());
                incomeHeading.setText("Income");
                incomeHeading.setTextColor(headingColor);
                incomeHeading.setTextSize(headingSize);

                TableLayout.LayoutParams incomeHeadingParams= new TableLayout.LayoutParams();
                incomeHeadingParams.setMargins(0,20,0,50);
                incomeHeadingRow.setLayoutParams(incomeHeadingParams);

                View v = new View(getApplicationContext());
                v.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        5
                ));
                v.setBackgroundColor(Color.parseColor("#B3B3B3"));



                incomeHeadingRow.addView(incomeHeading);

                table.addView(incomeHeadingRow);
                table.addView(v);



                double totalIncome=0;

                for (Map.Entry<String,Double> entry : incomes.entrySet()) {
                    TableRow row = new TableRow(getApplicationContext());



                    for (int j = 0; j < 2; j++) {
                        if (j == 0) {

                            TextView tv = new TextView(getApplicationContext());
                            tv.setText(entry.getKey());
                            tv.setPadding(50,0,0,0);
                            tv.setTextSize(contentSize);
                            tv.setTextColor(contentColor);
                            row.addView(tv);
                        } else if (j == 1) {

                            TextView tv = new TextView(getApplicationContext());
                            tv.setText("+"+entry.getValue().toString());
                            totalIncome+=entry.getValue();
                            tv.setPadding(0,0,100,0);
                            tv.setTextSize(contentSize);
                            tv.setGravity(Gravity.RIGHT);
                            tv.setTextColor(contentColor);
                            row.addView(tv);
                        }

                    }


                    table.addView(row);
                }

                TableRow TotalIncomeHeading = new TableRow(getApplicationContext());
                TextView TotalincomeHeadingtv = new TextView(getApplicationContext());
                TotalincomeHeadingtv.setText("Total Income");
                TotalincomeHeadingtv.setTextSize(totalSumSize);

                TotalIncomeHeading.addView(TotalincomeHeadingtv);

                TextView TotalincomeHeadingtv2 = new TextView(getApplicationContext());
                TotalincomeHeadingtv2.setText(String.valueOf(totalIncome));
                TotalincomeHeadingtv2.setTextSize(totalSumSize);
                TotalincomeHeadingtv2.setGravity(Gravity.RIGHT);
                TotalincomeHeadingtv2.setTextColor(Color.parseColor("#4cc206"));
                TotalincomeHeadingtv2.setPadding(0,0,100,0);

                TotalIncomeHeading.addView(TotalincomeHeadingtv2);

                TotalIncomeHeading.setBackgroundColor(Color.parseColor("#D0D0D0"));


                table.addView(TotalIncomeHeading);



                System.out.println("Total Income: "+ totalIncome);





                double totalExpense=0;
                TableRow expenseHeadingRow = new TableRow(getApplicationContext());
                TextView expenseHeading = new TextView(getApplicationContext());
                expenseHeading.setText("Expense");
                expenseHeading.setTextColor(headingColor);
                expenseHeading.setTextSize(headingSize);
                expenseHeadingRow.setLayoutParams(incomeHeadingParams);

                View v1 = new View(getApplicationContext());
                v1.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        5
                ));
                v1.setBackgroundColor(Color.parseColor("#B3B3B3"));

                expenseHeadingRow.addView(expenseHeading);
                table.addView(expenseHeadingRow);
                table.addView(v1);

                for (Map.Entry<String,Double> entry : expenses.entrySet()) {
                    TableRow row = new TableRow(getApplicationContext());

                    for (int j = 0; j < 2; j++) {
                        if (j == 0) {
                            TextView tv = new TextView(getApplicationContext());
                            tv.setText(entry.getKey());
                            tv.setPadding(50,0,0,0);
                            tv.setTextSize(contentSize);
                            tv.setTextColor(contentColor);
                            row.addView(tv);
                        } else if (j == 1) {
                            TextView tv = new TextView(getApplicationContext());
                            tv.setText("-"+entry.getValue().toString());
                            totalExpense+=entry.getValue();
                            tv.setGravity(Gravity.RIGHT);
                            tv.setPadding(0,0,100,0);
                            tv.setTextSize(contentSize);
                            tv.setTextColor(contentColor);
                            row.addView(tv);
                        }

                    }


                    table.addView(row);
                }

                TableRow TotalExpenseHeading = new TableRow(getApplicationContext());
                TextView TotalExpenseHeadingtv = new TextView(getApplicationContext());
                TotalExpenseHeadingtv.setText("Total Expense");
                TotalExpenseHeadingtv.setTextSize(totalSumSize);
                TotalExpenseHeading.addView(TotalExpenseHeadingtv);

                TextView TotalExpenseHeadingtv2 = new TextView(getApplicationContext());
                TotalExpenseHeadingtv2.setText(String.valueOf(totalExpense));
                TotalExpenseHeadingtv2.setTextSize(totalSumSize);
                TotalExpenseHeadingtv2.setGravity(Gravity.RIGHT);
                TotalExpenseHeadingtv2.setPadding(0,0,100,0);
                TotalExpenseHeadingtv2.setTextColor(Color.RED);
                TotalExpenseHeading.addView(TotalExpenseHeadingtv2);

                TotalExpenseHeading.setBackgroundColor(Color.parseColor("#D0D0D0"));

                table.addView(TotalExpenseHeading);



                TableRow BalanceHeading = new TableRow(getApplicationContext());
                TableLayout.LayoutParams tableRowParams= new TableLayout.LayoutParams();
                tableRowParams.setMargins(0, 100, 0, 0);
                BalanceHeading.setLayoutParams(tableRowParams);


                TextView BalanceHeadingtv = new TextView(getApplicationContext());
                BalanceHeadingtv.setText("Balance");
                BalanceHeadingtv.setTextSize(totalSumSize);
                BalanceHeading.addView(BalanceHeadingtv);

                TextView BalanceHeadingtv2 = new TextView(getApplicationContext());
                double balance = totalIncome-totalExpense;
                BalanceHeadingtv2.setText(String.valueOf(balance));
                BalanceHeadingtv2.setTextSize(totalSumSize);
                BalanceHeadingtv2.setPadding(0,0,100,0);
                BalanceHeadingtv2.setGravity(Gravity.RIGHT);
                BalanceHeading.addView(BalanceHeadingtv2);

                BalanceHeading.setBackgroundColor(Color.parseColor("#D0D0D0"));

                table.addView(BalanceHeading);


                System.out.println(expenses.values());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public double calculateIncome(ArrayList<FinancialsClass> transactionList,String category){
        double sum = 0;
        for(FinancialsClass transaction : transactionList)
        {
            if(transaction.getCategory().contentEquals(category)){
//                System.out.println(category+" -- "+ transaction.getMessage()+ " -- "+ transaction.getAmount());
                sum +=Double.parseDouble(transaction.getAmount());
            }

        }
        return sum;
    }

    public void firebasetrial(){

        Query query = databaseReference.child("Expense Management2").orderByChild("date");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println(snapshot);
                for(DataSnapshot data : snapshot.getChildren()){
//                    System.out.println(data);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}