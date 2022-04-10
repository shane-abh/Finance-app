package com.example.mainproject2.StockPlatform;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.mainproject2.R;


public class TechnicalsFragment extends Fragment {



    public TechnicalsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_stock, container, false);


        String stockSymbol = getArguments().getString("message");
        System.out.println(stockSymbol);

        WebView webView = view.findViewById(R.id.webview);
        webView.loadUrl("file:///android_asset/TechAnalysis.html");

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
                view.loadUrl("javascript:init('"+stockSymbol.toString()+"')");
            }
        });

        webView.addJavascriptInterface(new WebAppInterface(getContext()), "Android");

        return view;
    }
}