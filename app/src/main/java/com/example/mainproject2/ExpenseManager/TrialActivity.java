package com.example.mainproject2.ExpenseManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.mainproject2.R;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TrialActivity extends AppCompatActivity {

    Context context;

    ArrayList<FinancialsClass> transactionList;
    FirebaseAdapter fb;
    String key = null;

    private DatabaseReference databaseReference;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    WebView webView;


    JSONArray date = new JSONArray();
    JSONArray balance = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial);

        webView = findViewById(R.id.webview);
        transactionList = new ArrayList<FinancialsClass>();
        fb = new FirebaseAdapter();





        orderDate("03/01/2022", "03/07/2022");

//        loadfbData();






    }

//    public void loadfbData(){
//        Query q = db.getReference().child("Users").child(user.getUid()).child(user.getDisplayName()).child("Expense Management").orderByChild("date");
//
//
//        q.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                transactionList.clear();
//
//                Map<String, Double> typeAmountMap = new HashMap<>();
//
//
//
//                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//
//                    FinancialsClass t = dataSnapshot.getValue(FinancialsClass.class);
//                    key = dataSnapshot.getKey();
//                    t.setKey(key);
//                    transactionList.add(t);
////                    System.out.println("Date: "+t.getDate()+" Balance: "+t.getBalance());
//                    date.put(t.getDate());
//                    balance.put(t.getBalance());
//
//                    if(!t.isPositive()) {
//                        typeAmountMap.put(t.getCategory(), Double.parseDouble(String.valueOf(t.getAmount())));
////                        System.out.println(t.getCategory());
//                    }
//
//                }
//                System.out.println("DAte1: "+date);
//
//
//
//
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                webView.loadUrl("file:///android_asset/BalanceTrend.html");
//
//                                webView.getSettings().setJavaScriptEnabled(true);
//                                webView.getSettings().setAllowContentAccess(true);
//                                webView.getSettings().setAllowFileAccess(true);
//                                webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//
//                                webView.getSettings().setAppCacheEnabled(true);
//                                webView.getSettings().setDatabaseEnabled(true);
//
//
//                                webView.getSettings().setAllowFileAccessFromFileURLs(true);
//                                webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
//                                webView.getSettings().setDomStorageEnabled(true);
//
//                                webView.setWebViewClient(new WebViewClient() {
//
//                                    public void onPageFinished(WebView view, String url) {
//                                        view.loadUrl("javascript:init("+date+","+balance+")");
//                                    }
//                                });
//                                System.out.println("Date: "+date);
//                            }
//                        });
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//
//    }

    public void orderDate(String date, String date1){
        Query q = db.getReference().child("Users").child(user.getUid()).child(user.getDisplayName()).child("Expense Management").orderByChild("date").startAt(date.toString()).endAt(date1.toString());
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println(snapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}