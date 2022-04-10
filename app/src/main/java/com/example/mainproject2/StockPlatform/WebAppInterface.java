package com.example.mainproject2.StockPlatform;

import android.content.Intent;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.content.Context;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

public class WebAppInterface {
    Context mContext;

    /** Instantiate the interface and set the context */
    WebAppInterface(Context c) {
        mContext = c;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toast) {
        String NOTIFICATION_CHANNEL_ID = "10001" ;
        String default_notification_channel_id = "default";
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();

    }

    @JavascriptInterface
    public void goToPortfolio(){
        Intent intent = new Intent(mContext,MyPortfolioActivity.class);
        mContext.startActivity(intent);

    }
}
