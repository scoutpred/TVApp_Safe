package com.example.bottomnavigationview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

public class VDesc extends Fragment {

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
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.voucher_description, container, false);
        count = 0;
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();


        try{
            uid = mUser.getUid();
            Bundle bundle3 = this.getArguments();
            //establishment id
            enn = bundle3.getString("do");

            //discount code
            discc = bundle3.getString("di");

            //START

            db.collection("userDB").document(user.getUid()).collection("Vouchers")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("List", "Document is " + document.getId());
                                    Log.d("List", "Document is " + document.getBoolean("availed"));

                                    String est = document.getString("est");
                                    String cest = document.getString("dis");


                                    if ("false".equals(document.getBoolean("availed").toString())){
                                        Log.d("List", "IF STATEMENT PASSED!");

                                        DocumentReference docRef = db.collection("estDB").document(est).collection("Vouchers").document(cest);


                                        docRef.get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task2) {
                                                        if (task2.isSuccessful()) {
                                                            DocumentSnapshot document2 = task2.getResult();
                                                            if (document2.exists()) {
                                                                Log.d("List", "ON DOC2");

                                                                Log.d("List", "Document is " + document2.getId());
                                                                Log.d("List", "Document name is " + document2.getString("estname"));


                                                                Date dcomp1 = document2.getDate("expiration");
                                                                Date today;

                                                                DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                                                                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                                                                LocalDateTime now = LocalDateTime.now();
                                                                String tdate = dtf.format(now);

                                                                try {
                                                                    today = formatter.parse(tdate);
                                                                    if (today.compareTo(dcomp1) <= 0) {


                                                                            count++;
                                                                            Log.d("List", "Document count is " + count);
                                                                        if (count >= 5){
                                                                            Log.d("List", "Wallet is Full!");
                                                                            add.setText("Wallet Full!");
                                                                            add.setEnabled(false);
                                                                        }


                                                                    }
                                                                } catch (ParseException e) {
                                                                    e.printStackTrace();
                                                                }



                                                            }
                                                        } else {

                                                        }
                                                    }
                                                });




                                    }

                                }
                            } else {

                            }
                        }
                    });


            Log.d("List", "db.collection(\"estDB\").document("+enn+").collection(\"Vouchers\").document("+discc+").collection(\"Holders\").whereEqualTo(\"user\", "+uid+")");

            db.collection("estDB").document(enn).collection("Vouchers").document(discc).collection("Holders")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                Log.d("List", "Task is Done!");
                                boolean isEmpty = task.getResult().isEmpty();
                                if (isEmpty == true){
                                    Log.d("List", "Holders Do not Exist!");



                                }else{
                                    for (DocumentSnapshot document : task.getResult()) {
                                        if(document.exists()) {
                                            db.collection("estDB").document(enn).collection("Vouchers").document(discc).collection("Holders")
                                                    .whereEqualTo("user", uid)
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful()) {

                                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                                    if (document.exists()) {
                                                                        Log.d("List", "Voucher Excluded!");
                                                                        add.setText("Voucher Existed/Availed!");
                                                                        add.setEnabled(false);
                                                                    } else {
                                                                        Log.d("List", "Voucher Does Not Exist!");

                                                                        add.setText("add to my v-wallet");
                                                                        add.setEnabled(true);
                                                                    }
                                                                }

                                                            }else {

                                                            }
                                                        }
                                                    });

                                        } else {
                                            Log.d("List", "Holder Exists");

                                        }

                                    }

                                }


                            } else {

                            }
                        }
                    });
/*

*/

            //END

        }catch (Exception e){

        }


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
                    Log.d("List", "Add Clicked!");


                    if (count < 5){

                        Map<String, Object> addv = new HashMap<>();
                        addv.put("availed", false);
                        addv.put("dis", discc);
                        addv.put("est", enn);
                        addv.put("user", uid);

                        db.collection("estDB").document(enn).collection("Vouchers").document(discc).collection("Holders")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("List", "Task is Done!");
                                            boolean isEmpty = task.getResult().isEmpty();
                                            if (isEmpty == true){
                                                Log.d("List", "Holders Do not Exist!");
                                                db.collection("userDB").document(uid).collection("Vouchers")
                                                        .add(addv)
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                            @Override
                                                            public void onSuccess(DocumentReference documentReference) {
                                                                Log.d("test", "DocumentSnapshot written with ID: " + documentReference.getId());

                                                                Map<String, Object> addvest = new HashMap<>();
                                                                addvest.put("availed", false);
                                                                addvest.put("dis", discc);
                                                                addvest.put("user", uid);


                                                                db.collection("estDB").document(enn).collection("Vouchers").document(discc)
                                                                        .collection("Holders").document(documentReference.getId())
                                                                        .set(addvest);

                                                                add.setText("Voucher Added!");
                                                                add.setEnabled(false);


                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.w("test", "Error add document", e);
                                                            }
                                                        });



                                            }else if (isEmpty == false){
                                                Log.d("List", "Holder Existed. Creating Voucher Data");
                                                for (DocumentSnapshot document : task.getResult()) {
                                                    if(document.exists()) {
                                                        db.collection("estDB").document(enn).collection("Vouchers").document(discc).collection("Holders")
                                                                .whereEqualTo("user", uid)
                                                                .get()
                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                        if (task.isSuccessful()) {

                                                                                if (document.exists()) {
                                                                                    db.collection("userDB").document(uid).collection("Vouchers")
                                                                                            .add(addv)
                                                                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                                                @Override
                                                                                                public void onSuccess(DocumentReference documentReference) {
                                                                                                    Log.d("test", "DocumentSnapshot written with ID: " + documentReference.getId());

                                                                                                    Map<String, Object> addvest = new HashMap<>();
                                                                                                    addvest.put("availed", false);
                                                                                                    addvest.put("dis", discc);
                                                                                                    addvest.put("user", uid);


                                                                                                    db.collection("estDB").document(enn).collection("Vouchers").document(discc)
                                                                                                            .collection("Holders").document(documentReference.getId())
                                                                                                            .set(addvest);

                                                                                                    add.setText("Voucher Added!");
                                                                                                    add.setEnabled(false);


                                                                                                }
                                                                                            })
                                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                                @Override
                                                                                                public void onFailure(@NonNull Exception e) {
                                                                                                    Log.w("test", "Error add document", e);
                                                                                                }
                                                                                            });

                                                                                } else {



                                                                                }


                                                                        }else {
                                                                               if (document.exists()) {
                                                                                    db.collection("userDB").document(uid).collection("Vouchers")
                                                                                            .add(addv)
                                                                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                                                @Override
                                                                                                public void onSuccess(DocumentReference documentReference) {
                                                                                                    Log.d("test", "DocumentSnapshot written with ID: " + documentReference.getId());

                                                                                                    Map<String, Object> addvest = new HashMap<>();
                                                                                                    addvest.put("availed", false);
                                                                                                    addvest.put("dis", discc);
                                                                                                    addvest.put("user", uid);


                                                                                                    db.collection("estDB").document(enn).collection("Vouchers").document(discc)
                                                                                                            .collection("Holders").document(documentReference.getId())
                                                                                                            .set(addvest);

                                                                                                    add.setText("Voucher Added!");
                                                                                                    add.setEnabled(false);


                                                                                                }
                                                                                            })
                                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                                @Override
                                                                                                public void onFailure(@NonNull Exception e) {
                                                                                                    Log.w("test", "Error add document", e);
                                                                                                }
                                                                                            });

                                                                                } else {



                                                                                }


                                                                        }
                                                                    }
                                                                });

                                                    } else {
                                                        Log.d("List", "Holder Exists");

                                                    }

                                                }

                                            }


                                        } else {

                                        }
                                    }
                                });


                    }



                }else{
                    Intent intent=new Intent(getContext(), Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    getFragmentManager().beginTransaction().remove((Fragment) VDesc.this).commitAllowingStateLoss();
                }

            }
        });


        bacc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                getFragmentManager().beginTransaction().remove((Fragment) VDesc.this).commitAllowingStateLoss();
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