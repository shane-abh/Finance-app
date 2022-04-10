package com.example.mainproject2.StockPlatform;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.mainproject2.R;
import com.example.mainproject2.CustomViewPager;
import com.example.mainproject2.VPAdapter;
import com.google.android.material.tabs.TabLayout;

public class IncomeStatement extends AppCompatActivity {

    private TabLayout tabLayout;
    private CustomViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_sheet);

        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
        viewPager.setPagingEnabled(false);

        tabLayout.setupWithViewPager(viewPager);

        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        Intent intent = getIntent();
        String str = intent.getStringExtra("message");

        Bundle bundle = new Bundle();
        bundle.putString("symbol", str);

        AnnualIncomeStatementFrag fragobj = new AnnualIncomeStatementFrag();
        fragobj.setArguments(bundle);


        vpAdapter.addFragment(fragobj, "Annual");

        QuaterlyIncomeStatementFragment quat = new QuaterlyIncomeStatementFragment();
        quat.setArguments(bundle);
        vpAdapter.addFragment(quat, "Quarterly");




        viewPager.setAdapter(vpAdapter);

    }
}