package com.example.bottomnavigationview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    EditText inputemail;
    Button btn4getti;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
   TextView signInBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        inputemail=findViewById(R.id.inputEmail);
        btn4getti=findViewById(R.id.btn4get);
        mAuth = FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        signInBtn = findViewById(R.id.signInBtn);

        btn4getti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performReset();
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgotPassword.this, Login.class));
                finish();
            }
        });
    }

    private void performReset() {
        String email = inputemail.getText().toString().trim();

        if(email.isEmpty()){
            Toast.makeText(ForgotPassword.this, "Email is empty!", Toast.LENGTH_SHORT).show();
            inputemail.requestFocus();
            return;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(ForgotPassword.this, "Invalid Email Address!", Toast.LENGTH_SHORT).show();
            inputemail.requestFocus();
            return;
        }else {

            progressDialog.setMessage("Please wait...");

            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                sendUserToNextActivity();
                                Toast.makeText(ForgotPassword.this, "Check your email for the password reset link!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ForgotPassword.this, "Something went wrong...", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
    }
    private void sendUserToNextActivity(){

        Intent intent=new Intent(ForgotPassword.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



    }
