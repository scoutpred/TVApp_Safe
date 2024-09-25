package com.example.bottomnavigationview;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Fragment3 extends Fragment {

    RecyclerView vc;
    FirebaseFirestore db;
    int counter = 0;
    String docname;
    ImageView qrbtn;

    //Recommendation Vouchers
    List<WalletModel> vdm;
    WalletAdapter vda;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment3_vwallet,container,false);

        db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        vc = rootView.findViewById(R.id.walletRec);

        vc.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        vdm = new ArrayList<>();
        vda = new WalletAdapter(getActivity(),vdm);
        vc.setAdapter(vda);
        qrbtn = (ImageView) rootView.findViewById((R.id.qrBtn));

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
                                                                Log.d("List", "Document count is " + document2.getString("estname"));


                                                                Date dcomp1 = document2.getDate("expiration");
                                                                Date today;

                                                                DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                                                                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                                                                LocalDateTime now = LocalDateTime.now();
                                                                String tdate = dtf.format(now);

                                                                try {
                                                                    today = formatter.parse(tdate);
                                                                    if (today.compareTo(dcomp1) <= 0) {
                                                                        if (counter != 5) {
                                                                            if (!document2.getString("count").equals("0")) {
                                                                                WalletModel chers = document2.toObject(WalletModel.class);
                                                                                vdm.add(chers);
                                                                                vda.notifyDataSetChanged();
                                                                                counter++;
                                                                                Log.d("Counter", "list count: " + Integer.toString(counter));
                                                                            }
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

        qrbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                getFragmentManager().beginTransaction().remove((Fragment) Fragment3.this).commitAllowingStateLoss();
                Fragment fragment = null;
                fragment = new Fragment4();
                getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();

            }
        });





        return rootView;
    }
}
