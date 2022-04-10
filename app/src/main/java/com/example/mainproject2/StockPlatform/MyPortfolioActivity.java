package com.example.mainproject2.StockPlatform;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.mainproject2.ExpenseManager.ExpenseCalculatorMain;
import com.example.mainproject2.PaymentReminder.DashBoardActivity;
import com.example.mainproject2.ProfileActivity;
import com.example.mainproject2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MyPortfolioActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    PortfolioAdapter portfolioAdapter;
    ArrayList<userStocks> list;
    ArrayList<String> json;

    FloatingActionButton fab;
    SwipeRefreshLayout refresh;

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView nav_view;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_portfolio);

        recyclerView = findViewById(R.id.userStocks);
        fab = findViewById(R.id.fab);
        refresh = findViewById(R.id.refresh);

        nav_view = findViewById(R.id.nav_view);
        Navigation_drawer();

        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child(user.getDisplayName()).child("My Portfolio");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        json = new ArrayList<>();

        portfolioAdapter = new PortfolioAdapter(this,list);
        recyclerView.setAdapter(portfolioAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InsertPortfolio.class);
                startActivity(intent);
            }
        });



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    userStocks u = dataSnapshot.getValue(userStocks.class);
                    String sym = u.getSymbol();
                    System.out.println(sym);


                    list.add(u);
                }
                portfolioAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list.clear();
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            userStocks u = dataSnapshot.getValue(userStocks.class);
                            String sym = u.getSymbol();
                            System.out.println(sym);


                            list.add(u);
                        }
                        portfolioAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                refresh.setRefreshing(false);
            }
        });
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
                        Intent intent3 = new Intent(getApplicationContext(),MyPortfolioActivity.class);
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