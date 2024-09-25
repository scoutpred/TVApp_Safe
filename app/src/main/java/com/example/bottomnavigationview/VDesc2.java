package com.example.bottomnavigationview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class VDesc2 extends Fragment {

    TextView estn, dis, exp, urlopen;
    String enn, discc, urlink, uid, ttype, eename, disn;
    ImageView bacc;
    FirebaseUser mUser;
    int count;
    MaterialButton add;
    private FirebaseFirestore db;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.voucher_userdesc, container, false);
        count = 0;
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        uid = mUser.getUid();

        Bundle bundle3 = this.getArguments();

        urlopen = (TextView) rootView.findViewById(R.id.info1);
        estn = (TextView) rootView.findViewById(R.id.textView);
        dis = (TextView) rootView.findViewById(R.id.descA);
        exp = (TextView) rootView.findViewById(R.id.expireA);
        bacc = (ImageView) rootView.findViewById(R.id.backBtn);
        add = (MaterialButton) rootView.findViewById(R.id.materialButton) ;

        //infoname
        estn.setText(bundle3.getString("in"));

        //infodesc
        dis.setText(bundle3.getString("id"));

        //expiry date
        exp.setText("Expires on " + bundle3.getString("ed"));

        //establishment id
        enn = bundle3.getString("do");

        //discount code
        discc = bundle3.getString("di");

        //establishment name
        eename = bundle3.getString("en");




        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                if (user != null) {

                    Log.d("Del", "Clicked Delete!");

                    Log.d("List", "db.collection(\"estDB\").document("+enn+").collection(\"Vouchers\").document("+discc+").collection(\"Holders\").whereEqualTo(\\\"user\\\", "+uid+")");

                    db.collection("estDB").document(enn).collection("Vouchers").document(discc).collection("Holders")
                            .whereEqualTo("user", uid)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {

                                            String docid2 = document.getId();

                                            db.collection("userDB").document(uid).collection("Vouchers")
                                                    .document(docid2)
                                                    .delete()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            db.collection("estDB").document(enn).collection("Vouchers").document(discc).collection("Holders")
                                                                    .document(docid2)
                                                                    .delete()
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            getFragmentManager().beginTransaction().remove((Fragment) VDesc2.this).commitAllowingStateLoss();
                                                                            Fragment fragment = null;
                                                                            fragment = new Fragment3();
                                                                            getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();

                                                                        }
                                                                    })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {

                                                                        }
                                                                    });

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



                }else{
                    Intent intent=new Intent(getContext(), Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    getFragmentManager().beginTransaction().remove((Fragment) VDesc2.this).commitAllowingStateLoss();
                }

            }
        });


        bacc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                getFragmentManager().beginTransaction().remove((Fragment) VDesc2.this).commitAllowingStateLoss();
                Fragment fragment = null;
                fragment = new Fragment1();
                getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();

            }
        });

        urlopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DocumentReference docRef2 = db.collection("estDB")
                        .document(enn);
                docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task2) {
                        if (task2.isSuccessful()) {
                            DocumentSnapshot document2 = task2.getResult();
                            if (document2.exists()) {

                                Uri webpage = Uri.parse(document2.getString("site"));

                                if (!document2.getString("site").startsWith("http://") && !document2.getString("site").startsWith("https://")) {
                                   webpage = Uri.parse("http://" + document2.getString("site"));
                                }

                                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                                startActivity(intent);


                            } else {

                            }
                        } else {

                        }
                    }
                });



            }
        });

        return rootView;
    }




}