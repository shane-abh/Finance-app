package com.example.mainproject2.StockPlatform;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.mainproject2.R;


public class QuaterlyIncomeStatementFragment extends Fragment {


    public QuaterlyIncomeStatementFragment() {
        // Required empty public constructor
    }

    WebView webView;
    SwipeRefreshLayout refreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quaterly, container, false);

        webView = view.findViewById(R.id.webview);
        refreshLayout = view.findViewById(R.id.refresh);




        loadData();


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                refreshLayout.setRefreshing(false);
            }
        });



        return view;
    }

    public void loadData(){

        webView.loadUrl("file:///android_asset/quaterlyIncomeStatement.html");

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);


        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setDomStorageEnabled(true);

        String strtext = getArguments().getString("symbol");
        webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                view.loadUrl("javascript:init('"+strtext.toString()+"')");
            }
        });

    }
}