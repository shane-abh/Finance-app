package com.example.mainproject2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccountActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    Button logoutBtn;
    TextView userName,userEmail,userId;
    ImageView profileImage;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;

    private FirebaseAuth mAuth;

    DatabaseReference databaseReference;

    FirebaseUser user;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        logoutBtn=(Button)findViewById(R.id.logoutBtn);
        userName=(TextView)findViewById(R.id.name);
        userEmail=(TextView)findViewById(R.id.email);
        userId=(TextView)findViewById(R.id.userId);
        profileImage=(ImageView)findViewById(R.id.profileImage);

        getSupportActionBar().setTitle("My Account");



        mAuth = FirebaseAuth.getInstance();

         user = mAuth.getCurrentUser();

        if(user!=null){
//            Toast.makeText(getApplicationContext(),user.getUid(),Toast.LENGTH_LONG).show();

            userName.setText("");

            userEmail.setText("Email: " + user.getEmail());
//            userId.setText(user.getProviderData().toString());

        }else{
            Toast.makeText(getApplicationContext(),"Not Registered",Toast.LENGTH_LONG).show();
        }


        for (UserInfo user: FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {
            if(user.getProviderId().equals("password")){

                userId.setText("Authentication type: " + user.getProviderId());
            }

            if(user.getProviderId().equals("google.com")){
                Glide.with(this).load(user.getPhotoUrl()).into(profileImage);
                userName.setText("Name: " + user.getDisplayName());
                userId.setText("Authentication type: " + user.getProviderId());
            }
        }





        gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("383113449125-p1s0odq88d5l0fp9s05k2ss8403kt2ec.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();




        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (UserInfo user: FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {
                    if (user.getProviderId().equals("password")) {
                        System.out.println("User is signed in with email/password");
                        mAuth.signOut();
                        gotoMainActivity();
                    }

                    if(user.getProviderId().equals("google.com")){
                        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                        if (status.isSuccess()){
                                            FirebaseAuth.getInstance().signOut();
                                            gotoMainActivity();
                                        }else{
                                            Toast.makeText(getApplicationContext(),"Session not closed",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }


                }

//                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
//                        new ResultCallback<Status>() {
//                            @Override
//                            public void onResult(Status status) {
//                                if (status.isSuccess()){
//                                    FirebaseAuth.getInstance().signOut();
//                                    gotoMainActivity();
//                                }else{
//                                    Toast.makeText(getApplicationContext(),"Session not closed",Toast.LENGTH_LONG).show();
//                                }
//                            }
//                        });
            }
        });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        OptionalPendingResult<GoogleSignInResult> opr= Auth.GoogleSignInApi.silentSignIn(googleApiClient);
//        if(opr.isDone()){
//            GoogleSignInResult result=opr.get();
//            handleSignInResult(result);
//        }else{
//            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
//                @Override
//                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
//                    handleSignInResult(googleSignInResult);
//                }
//            });
//        }
//    }
//    private void handleSignInResult(GoogleSignInResult result){
//        if(result.isSuccess()){
//            GoogleSignInAccount account=result.getSignInAccount();
//
//            userName.setText("");
//            userEmail.setText(user.getEmail());
//            userId.setText(user.getProviderId());
//
//
//
//
//
//
//
//            try{
//                Glide.with(this).load(account.getPhotoUrl()).into(profileImage);
//            }catch (NullPointerException e){
//                Toast.makeText(getApplicationContext(),"image not found",Toast.LENGTH_LONG).show();
//            }
//
//        }else{
//            gotoMainActivity();
//        }
//    }
    private void gotoMainActivity(){
        Intent intent=new Intent(this,LoginActivity.class);
        startActivity(intent);
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}