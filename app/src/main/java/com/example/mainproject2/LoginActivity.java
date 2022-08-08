package com.example.mainproject2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener  {

    SignInButton signInButton;
    private GoogleApiClient googleApiClient;
    TextView textView,signup,forgotpassword;

    FirebaseAuth auth;
    FirebaseUser user;
    private static final int RC_SIGN_IN = 1;
    EditText name,email,password;
    Button signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        signin = findViewById(R.id.button);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signup = findViewById(R.id.signup);
        forgotpassword = findViewById(R.id.forgotPassword);


        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("383113449125-p1s0odq88d5l0fp9s05k2ss8403kt2ec.apps.googleusercontent.com")
                .requestEmail()
                .build();
        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();


        auth = FirebaseAuth.getInstance();
        signInButton=(SignInButton)findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,RC_SIGN_IN);
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(intent);
            }
        });

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateEmail();
            }
        });

        user = auth.getCurrentUser();

        if(user!= null){
            gotoProfile();
        }


    }



    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_CANCELED){

            if(requestCode==RC_SIGN_IN){
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

                AuthCredential credential = GoogleAuthProvider.getCredential(result.getSignInAccount().getIdToken(), null);
                auth.signInWithCredential(credential);
//            Toast.makeText(getApplicationContext(),user.toString(),Toast.LENGTH_LONG).show();
                handleSignInResult(result);

            }
        }


    }
    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            gotoProfile();
        }else{
            Toast.makeText(getApplicationContext(),"Sign in cancel",Toast.LENGTH_LONG).show();
        }
    }
    private void gotoProfile(){
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        Intent intent=new Intent(LoginActivity.this,ProfileActivity.class);
        startActivity(intent);
    }

    public void validate(){
        String user_email = email.getText().toString();
        String user_password = password.getText().toString();

        if(!Patterns.EMAIL_ADDRESS.matcher(user_email).matches()){
            email.setError("Invalid");
            Toast.makeText(getApplicationContext(), "Email Invalid", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(user_password)){
            password.setError("Enter Password");
        } else if(user_password.length()<6){
            password.setError("Password should be more than 6 characters");
        } else {
            firebaseSignIn(user_email,user_password);
        }
    }

    public void firebaseSignIn(String email,String password){
        auth.signInWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(LoginActivity.this,ProfileActivity.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Login Not Successful. "+ e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void validateEmail(){
        String user_email = email.getText().toString();
        if(!Patterns.EMAIL_ADDRESS.matcher(user_email).matches()){
            email.setError("Invalid");
            Toast.makeText(getApplicationContext(), "Email Invalid", Toast.LENGTH_SHORT).show();
        }else{
            auth.sendPasswordResetEmail(user_email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(), "Password reset email sent.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}