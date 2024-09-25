package com.example.bottomnavigationview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {

    EditText inputemail, inputPassword, inputCConfirmPassword;
    TextView createnewAccount, forgetpw;
    Button btnLogin;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        forgetpw = findViewById(R.id.forgotPass);
        mAuth=FirebaseAuth.getInstance();

        progressDialog=new ProgressDialog(this);
        inputemail = findViewById(R.id.userName);
        inputPassword = findViewById(R.id.passWord);
        createnewAccount = findViewById(R.id.signUpBtn);
        btnLogin = findViewById(R.id.signInBtn);

        createnewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    startActivity(new Intent(Login.this, Register.class));
                    finish();
            }
        });

        forgetpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,ForgotPassword.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(inputemail.getText().toString().trim().length() == 0){
                    inputemail.setHintTextColor(getResources().getColor(R.color.purple_200));
                    inputemail.setHint("Need Username Here!");
                }else if(inputPassword.getText().toString().trim().length() == 0) {
                    inputPassword.setHintTextColor(getResources().getColor(R.color.purple_200));
                    inputPassword.setHint("Need Password Here!");
                }else{
                    progressDialog.setMessage("Logging in user...");
                    progressDialog.setTitle("Login");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    mAuth.signInWithEmailAndPassword(inputemail.getText().toString(), inputPassword.getText().toString())
                            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                    mUser=mAuth.getCurrentUser();
                                    userID = mUser.getUid();
                                    DocumentReference docRef = db.collection("userDB").document(userID);
                                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                String val = document.getString("type");

                                                if(val.equals("client")){
                                                    if (mAuth.getCurrentUser().isEmailVerified()){
                                                        progressDialog.dismiss();
                                                        sendUserToNextActivity();
                                                        Toast.makeText(Login.this, "Login Success!", Toast.LENGTH_SHORT).show();
                                                    }else{
                                                        progressDialog.dismiss();
                                                        FirebaseAuth.getInstance().signOut();
                                                        Toast.makeText(Login.this, "Please verify email first!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }else{
                                                    FirebaseAuth.getInstance().signOut();
                                                    progressDialog.dismiss();
                                                    Toast.makeText(Login.this, "Employees are not permmitted!", Toast.LENGTH_SHORT).show();
                                                }

                                            } else {

                                            }
                                        }
                                    });
                                    }
                                    else {
                                        FirebaseAuth.getInstance().signOut();
                                        progressDialog.dismiss();
                                        Toast.makeText(Login.this, "Sign-In Failed", Toast.LENGTH_SHORT).show();
                                        Log.e("Firebase", "Sign in failed", task.getException());
                                    }
                                }
                            });
                }

            }
        });




    }
    private void sendUserToNextActivity(){
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(Login.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}