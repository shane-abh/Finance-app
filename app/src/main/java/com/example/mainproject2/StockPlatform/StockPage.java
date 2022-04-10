package com.example.mainproject2.StockPlatform;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentPagerAdapter;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.example.mainproject2.AccountActivity;
import com.example.mainproject2.CustomViewPager;
import com.example.mainproject2.ExpenseManager.ExpenseCalculatorMain;
import com.example.mainproject2.LoginActivity;
import com.example.mainproject2.PaymentReminder.DashBoardActivity;
import com.example.mainproject2.ProfileActivity;
import com.example.mainproject2.R;
import com.example.mainproject2.VPAdapter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

public class StockPage extends AppCompatActivity {

    private TabLayout tabLayout;
    private CustomViewPager viewPager;

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView nav_view;

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_page);

        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
        viewPager.setPagingEnabled(false);

        tabLayout.setupWithViewPager(viewPager);

        fab = findViewById(R.id.fab);
        nav_view = findViewById(R.id.nav_view);
        Navigation_drawer();

        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        Intent intent = getIntent();
        String str = intent.getStringExtra("message");
        String name = intent.getStringExtra("name");

        Bundle bundle = new Bundle();
        bundle.putString("message", str);
        getSupportActionBar().setTitle(name);

        StockFragment stockFragment = new StockFragment();
        stockFragment.setArguments(bundle);

        FinancialFragment financialFragment = new FinancialFragment();
        financialFragment.setArguments(bundle);

        TechnicalsFragment technicalsFragment = new TechnicalsFragment();
        technicalsFragment.setArguments(bundle);


        vpAdapter.addFragment(stockFragment,"Stock Page");
        vpAdapter.addFragment(financialFragment,"Financials");
        vpAdapter.addFragment(technicalsFragment,"Technicals");


        viewPager.setAdapter(vpAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MyPortfolioActivity.class);
                startActivity(intent);
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

                    case R.id.nav_logout:
//                        Intent intent7 = new Intent(getApplicationContext(), AccountActivity.class);
//                        startActivity(intent7);
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