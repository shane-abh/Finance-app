package com.example.mainproject2.StockPlatform;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mainproject2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class FinancialFragment extends Fragment {
    TextView sym;

    CardView balanceSheetcard;
    CardView incomeStatementcard;
    CardView cashflowcard;

    LinearLayout hidden1;
    ImageView arrow1;

    LinearLayout hidden2;
    ImageView arrow2;

    LinearLayout hidden3;
    ImageView arrow3;

    FloatingActionButton fab;
    

    public FinancialFragment() {
        // Required empty public constructor
    }

    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_financial, container, false);

        

        balanceSheetcard = view.findViewById(R.id.BalanceSheetcard);
        incomeStatementcard = view.findViewById(R.id.incomeStatementcard);
        cashflowcard = view.findViewById(R.id.cashflowcard);

        hidden1 = view.findViewById(R.id.hidden1);
        hidden2 = view.findViewById(R.id.hidden2);
        hidden3 = view.findViewById(R.id.hidden3);

        arrow1 = view.findViewById(R.id.arrow1);
        arrow2 = view.findViewById(R.id.arrow2);
        arrow3 = view.findViewById(R.id.arrow3);



        sym = view.findViewById(R.id.symbol);

        fab = view.findViewById(R.id.fab);


        String str = getArguments().getString("message");
        sym.setText(str);

        arrow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // If the CardView is already expanded, set its visibility
                //  to gone and change the expand less icon to expand more.
                if (hidden1.getVisibility() == View.VISIBLE) {

                    // The transition of the hiddenView is carried out
                    //  by the TransitionManager class.
                    // Here we use an object of the AutoTransition
                    // Class to create a default transition.
                    TransitionManager.beginDelayedTransition(balanceSheetcard,
                            new AutoTransition());
                    hidden1.setVisibility(View.GONE);
                    arrow1.setImageResource(R.drawable.ic_baseline_expand_more_24);
                }

                // If the CardView is not expanded, set its visibility
                // to visible and change the expand more icon to expand less.
                else {

                    TransitionManager.beginDelayedTransition(balanceSheetcard,
                            new AutoTransition());
                    hidden1.setVisibility(View.VISIBLE);
                    arrow1.setImageResource(R.drawable.ic_baseline_expand_less_24);
                }
            }
        });

        balanceSheetcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BalanceSheet.class);
                intent.putExtra("message", str.toString());
                startActivity(intent);
            }
        });


        arrow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // If the CardView is already expanded, set its visibility
                //  to gone and change the expand less icon to expand more.
                if (hidden1.getVisibility() == View.VISIBLE) {

                    // The transition of the hiddenView is carried out
                    //  by the TransitionManager class.
                    // Here we use an object of the AutoTransition
                    // Class to create a default transition.
                    TransitionManager.beginDelayedTransition(balanceSheetcard,
                            new AutoTransition());
                    hidden1.setVisibility(View.GONE);
                    arrow1.setImageResource(R.drawable.ic_baseline_expand_more_24);
                }

                // If the CardView is not expanded, set its visibility
                // to visible and change the expand more icon to expand less.
                else {

                    TransitionManager.beginDelayedTransition(balanceSheetcard,
                            new AutoTransition());
                    hidden1.setVisibility(View.VISIBLE);
                    arrow1.setImageResource(R.drawable.ic_baseline_expand_less_24);
                }
            }
        });

//        balanceSheetcard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), BalanceSheet.class);
//                intent.putExtra("message", str.toString());
//                startActivity(intent);
//            }
//        });





        arrow2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {


                if (hidden2.getVisibility() == View.VISIBLE) {


                    TransitionManager.beginDelayedTransition(incomeStatementcard,
                            new AutoTransition());
                    hidden2.setVisibility(View.GONE);
                    arrow2.setImageResource(R.drawable.ic_baseline_expand_more_24);
                }


                else {

                    TransitionManager.beginDelayedTransition(incomeStatementcard,
                            new AutoTransition());
                    hidden2.setVisibility(View.VISIBLE);
                    arrow2.setImageResource(R.drawable.ic_baseline_expand_less_24);
                }
            }
        });

//        incomeStatementcard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), IncomeStatement.class);
//                intent.putExtra("message", str.toString());
//                startActivity(intent);
//            }
//        });



        arrow3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (hidden3.getVisibility() == View.VISIBLE) {


                    TransitionManager.beginDelayedTransition(cashflowcard,
                            new AutoTransition());
                    hidden3.setVisibility(View.GONE);
                    arrow3.setImageResource(R.drawable.ic_baseline_expand_more_24);
                }


                else {

                    TransitionManager.beginDelayedTransition(cashflowcard,
                            new AutoTransition());
                    hidden3.setVisibility(View.VISIBLE);
                    arrow3.setImageResource(R.drawable.ic_baseline_expand_less_24);
                }
            }
        });

//        cashflowcard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), CashFlowStatement.class);
//                intent.putExtra("message", str.toString());
//                startActivity(intent);
//            }
//        });
//
//
//
//        balanceSheet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), BalanceSheet.class);
//                intent.putExtra("message", str.toString());
//                startActivity(intent);
//            }
//        });
//
//        incomeStatement.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), IncomeStatement.class);
//                intent.putExtra("message", str.toString());
//                startActivity(intent);
//            }
//        });
//
//        cashFlow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), CashFlowStatement.class);
//                intent.putExtra("message", str.toString());
//                startActivity(intent);
//            }
//        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),MyPortfolioActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}