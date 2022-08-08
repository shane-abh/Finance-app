package com.example.mainproject2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    FirebaseAuth auth;
    EditText name,email,password;
    Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        signup = findViewById(R.id.button);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        getSupportActionBar().hide();



        signup.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                validate();
            }
        });


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
            firebaseSignUp(user_email,user_password);
        }

    }

    public void firebaseSignUp(String email,String password){
        auth.createUserWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(getApplicationContext(),"Registration Successful",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Registration Failed. "+ e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

}