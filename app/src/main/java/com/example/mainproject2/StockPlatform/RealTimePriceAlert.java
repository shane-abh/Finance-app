package com.example.mainproject2.StockPlatform;

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
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.mainproject2.ExpenseManager.ExpenseCalculatorMain;
import com.example.mainproject2.PaymentReminder.DashBoardActivity;
import com.example.mainproject2.ProfileActivity;
import com.example.mainproject2.R;
import com.google.android.material.navigation.NavigationView;

public class RealTimePriceAlert extends AppCompatActivity {

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView nav_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_time_price_alert);

        nav_view = findViewById(R.id.nav_view);
        Navigation_drawer();

        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Real-Time price alert");

        WebView webView = findViewById(R.id.webview);
        //priceAlert works
        webView.loadUrl("file:///android_asset/select.html");

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);

        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.addJavascriptInterface(new WebAppInterface1(this), "Android");


    }
    public class WebAppInterface1 {
        Context mContext;

        /** Instantiate the interface and set the context */
        WebAppInterface1(Context c) {
            mContext = c;
        }

        /** Show a toast from the web page */
        @JavascriptInterface
        public void showToast(String toast) {
            String NOTIFICATION_CHANNEL_ID = "10001" ;
            String default_notification_channel_id = "default";
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "Shane";
                String description = "First notification";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(default_notification_channel_id, name, importance);
                channel.setDescription(description);
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }

            Uri alarmSound =
                    RingtoneManager. getDefaultUri (RingtoneManager. TYPE_NOTIFICATION );
            MediaPlayer mp = MediaPlayer. create (getApplicationContext(), alarmSound);
            mp.start();
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(mContext, default_notification_channel_id )
                            .setSmallIcon(R.drawable.ic_launcher_foreground )
                            .setContentTitle( "Price Alert" )
                            .setContentText( toast ) ;
            NotificationManager mNotificationManager = (NotificationManager)
                    getSystemService(Context. NOTIFICATION_SERVICE );
            mNotificationManager.notify(( int ) System. currentTimeMillis () ,
                    mBuilder.build());
        }
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