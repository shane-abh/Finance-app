package com.example.mainproject2.ExpenseManager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mainproject2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AddIncomeFragment extends Fragment {



    public AddIncomeFragment() {
        // Required empty public constructor
    }

    Spinner spinner;
    String[] paths = {"--Select--","Income", "Investments", "Savings", "Loan", "Others"};

    Calendar myCalendar = Calendar.getInstance();
    EditText dateEdt, amount, mess;
    Button btn;
    boolean positive = true;
    TextView tvSign, inc_exp;
    String key = null;
    ArrayList<FinancialsClass> transactionList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_income, container, false);

        spinner = view.findViewById(R.id.spinner);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, paths);


        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter1);

        amount = view.findViewById(R.id.amount);
        amount.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});
        mess = view.findViewById(R.id.message);
        dateEdt = view.findViewById(R.id.Date);
        btn = view.findViewById(R.id.btn);
        tvSign = view.findViewById(R.id.tvSign);
        inc_exp = view.findViewById(R.id.inc_exp);

        transactionList = new ArrayList<>();





        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel();
            }
        };
        dateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAdapter fb = new FirebaseAdapter();
//                FinancialsClass a = new FinancialsClass();

                if(amount.getText().toString().isEmpty()){
                    amount.setError("Enter amount");
                }else if( mess.getText().toString().isEmpty()){
                    mess.setError("Enter a note");
                } else if( dateEdt.getText().toString().isEmpty()){
                    dateEdt.setError("Choose date");
                } else if(spinner.getSelectedItem().equals("--Select--")){
                    Toast.makeText(getContext(), "Select a category", Toast.LENGTH_SHORT).show();
                }else {


                    fb.get(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            transactionList.clear();
                            FinancialsClass a = new FinancialsClass();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                FinancialsClass t = dataSnapshot.getValue(FinancialsClass.class);
                                key = dataSnapshot.getKey();
                                t.setKey(key);
                                transactionList.add(t);
                            }
                            System.out.println(calculateBalance(transactionList));
                            double bal = calculateBalance(transactionList);
                            String income = calculateIncome(transactionList);
                            String expense = calculateExpense(transactionList);

                            double amt = Double.parseDouble(amount.getText().toString());
                            a.setAmount(amount.getText().toString());
                            a.setCategory(spinner.getSelectedItem().toString());
                            a.setDate(dateEdt.getText().toString());
                            a.setMessage(mess.getText().toString());
                            a.setPositive(positive);
                            a.setBalance(String.valueOf(bal + amt));
                            a.setIncome(income);
                            a.setExpense(expense);
                            System.out.println("Add Income");
                            fb.add(a).addOnCompleteListener(task -> {

                                if (task.isSuccessful()) {
                                    Toast.makeText((getContext()), "Data inserted", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getContext(), ExpenseDetails.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText((getContext()), "Data not inserted", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
        });



        return view;

    }


    public double calculateBalance(ArrayList<FinancialsClass> transactionList) {
        double bal = 0;
        for (FinancialsClass transaction : transactionList) {
            if (transaction.isPositive()) {
                bal += Double.parseDouble(transaction.getAmount());
            } else {
                bal -= Double.parseDouble(transaction.getAmount());
            }
        }
        return bal;
    }

    public String calculateIncome(ArrayList<FinancialsClass> transactionList) {

        double income = 0;
        for (FinancialsClass transaction : transactionList) {
            if (transaction.isPositive()) {
                income += Double.parseDouble(transaction.getAmount());
            }

        }
        return String.valueOf(income);
    }

    public String calculateExpense(ArrayList<FinancialsClass> transactionList) {

        double expense = 0;
        for (FinancialsClass transaction : transactionList) {
            if (!transaction.isPositive()) {

                expense += Double.parseDouble(transaction.getAmount());
            }
        }
        return String.valueOf(expense);
    }


    // Function to change sign
    private void changeSign() {
        if (positive) {
            tvSign.setText("+AED");
            tvSign.setTextColor(Color.parseColor("#00c853"));  //Green
            amount.setTextColor(Color.parseColor("#00c853"));

            amount.setHintTextColor(Color.parseColor("#00c853"));
            inc_exp.setText("Income");
            positive = true;
        }
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        dateEdt.setText(dateFormat.format(myCalendar.getTime()));
    }
}