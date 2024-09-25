package com.example.bottomnavigationview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditProf extends AppCompatActivity {
    EditText fne, lne;
    TextView accept, backk;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment4_edit_profile);

        fne = findViewById(R.id.fName);
        lne = findViewById(R.id.lName);
        accept = findViewById(R.id.edit_profile);
        backk = findViewById(R.id.edit_back);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.collection("userDB").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .update(
                                "fname", fne.getText().toString(),
                                "lname", lne.getText().toString()
                        );
                startActivity(new Intent(EditProf.this,MainActivity.class));
            }
        });

        backk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProf.this,MainActivity.class));
                finish();
            }
        });

        DocumentReference docRef = db.collection("userDB").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        fne.setText(document.getString("fname"));
                        lne.setText(document.getString("lname"));

                    } else {

                    }
                } else {

                }
            }
        });




    }


}
