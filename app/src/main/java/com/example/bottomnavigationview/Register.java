package com.example.bottomnavigationview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    TextView haveAccount;
    EditText inputemail, inputPassword, inputCConfirmPassword, inpf, inpl;
    Button btnregister;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        inputemail=findViewById(R.id.eMail);
        inputPassword=findViewById(R.id.passWord);
        inputCConfirmPassword=findViewById(R.id.conPassword);
        inpf=findViewById(R.id.fname);
        inpl=findViewById(R.id.lname);
        btnregister=findViewById(R.id.signUpBtn);
        progressDialog=new ProgressDialog(this);
        haveAccount=findViewById(R.id.signInBtn);

        mAuth=FirebaseAuth.getInstance();


        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PerformAuth();
            }
        });

        haveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, Login.class));
                finish();

            }
        });

    }

    private void PerformAuth(){
        String email=inputemail.getText().toString();
        String password=inputPassword.getText().toString();
        String confirmpassword=inputPassword.getText().toString();
        String fname=inpf.getText().toString();
        String lname=inpl.getText().toString();

        if (!email.matches(emailPattern)){
            inputemail.setError("Enter Correct Email");
            inputemail.requestFocus();
        }else if(password.isEmpty()||password.length()<8){
            inputPassword.setError("Invalid Password");
            inputPassword.requestFocus();
        }else if (!password.equals(confirmpassword)) {
            inputCConfirmPassword.setError("Passwords do not match!");
            inputPassword.requestFocus();
        }else if (fname.isEmpty()||lname.isEmpty()){
            Toast.makeText(Register.this, "No name", Toast.LENGTH_SHORT).show();
        }else{
            progressDialog.setMessage("Registering user...");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        mAuth.getCurrentUser().sendEmailVerification()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                                            mUser=mAuth.getCurrentUser();
                                            String userID = mUser.getUid();

                                            Map<String, Object> newuser = new HashMap<>();
                                            newuser.put("estemp","");
                                            newuser.put("type", "client");
                                            newuser.put("email",email);
                                            newuser.put("fname", fname);
                                            newuser.put("lname", lname);

                                            db.collection("userDB").document(userID)
                                                    .set(newuser)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            progressDialog.dismiss();
                                                            sendUserToNextActivity();
                                                            Map<String, Object> newdate = new HashMap<>();

                                                            db.collection("userDB").document(userID).collection("Visits")
                                                                    .add(newdate)
                                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                        @Override
                                                                        public void onSuccess(DocumentReference documentReference) {
                                                                            DocumentReference userRef = db.collection("userDB").document(userID);
                                                                            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                    if (task.isSuccessful()) {
                                                                                        DocumentSnapshot uref = task.getResult();
                                                                                        if (uref.exists()) {
                                                                                            Map<String, Object> toUser = new HashMap<>();

                                                                                            db.collection("userDB").document(userID)
                                                                                                    .update(toUser)
                                                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onSuccess(Void aVoid) {
                                                                                                        }
                                                                                                    })
                                                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                                                        @Override
                                                                                                        public void onFailure(@NonNull Exception e) {

                                                                                                        }
                                                                                                    });
                                                                                        }
                                                                                    } else {

                                                                                    }
                                                                                }
                                                                            });

                                                                        }
                                                                    });
                                                            Toast.makeText(Register.this, "Registration successful, please verify email first!", Toast.LENGTH_SHORT).show();

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }else{

                                        }
                                    }
                                });

                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(Register.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void sendUserToNextActivity(){

        Intent intent=new Intent(Register.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        Intent intent=new Intent(Register.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}