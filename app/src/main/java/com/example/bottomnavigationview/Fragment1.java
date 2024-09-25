package com.example.bottomnavigationview;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
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
import java.util.concurrent.ThreadLocalRandom;

public class Fragment1 extends Fragment {

    RecyclerView categoryRec,recommendedRec;
    FirebaseFirestore db;
    int counter = -1;
    ArrayList<String> arrayListOfDocs = new ArrayList<String>();
    String docname;

//Home of App
    List<VoucherCategory> voucherCategoryList;
    VoucherCategoryAdapter voucherCategoryAdapter;

//Recommendation Vouchers
    List<RecommendedVouchers> recommendedVouchersList;
    RecommendedVoucherAdapter recommendedVoucherAdapter;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment1_home,container,false);

        db = FirebaseFirestore.getInstance();

        categoryRec = rootView.findViewById(R.id.category);
        recommendedRec = rootView.findViewById(R.id.rec_vouchers);


        //for Voucher Category
        categoryRec.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        voucherCategoryList = new ArrayList<>();
        voucherCategoryAdapter = new VoucherCategoryAdapter(getActivity(),voucherCategoryList);
        categoryRec.setAdapter(voucherCategoryAdapter);

        db.collection("VoucherCategory").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document :task.getResult()){
                                VoucherCategory voucherCategory = document.toObject(VoucherCategory.class);
                                voucherCategoryList.add(voucherCategory);
                                voucherCategoryAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });



        //for Recommended Vouchers
        recommendedRec.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        recommendedVouchersList = new ArrayList<>();
        recommendedVoucherAdapter = new RecommendedVoucherAdapter(getActivity(),recommendedVouchersList);
        recommendedRec.setAdapter(recommendedVoucherAdapter);



        // //Get Random Document
        db.collection("estDB")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            if (task.getResult().size() > 0){

                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    counter++;
                                    arrayListOfDocs.add(document.getId());
                                    Log.d("List", "doc scanned is "+arrayListOfDocs.toString());
                                    Log.d("Counter", "list count: " + Integer.toString(counter));
                                    if (counter == 0){
                                        docname = arrayListOfDocs.get(0);
                                        Log.d("doc", "doc is " + arrayListOfDocs.get(0));

                                    }else{
                                        docname = arrayListOfDocs.get((int)(Math.random() * arrayListOfDocs.size()));
                                        Log.d("doc", "doc is " + docname);

                                    }
                                }
                            }

                        } else {

                        }
                        db.collection("estDB")
                                .document(docname).collection("Vouchers").get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()){
                                            for (QueryDocumentSnapshot document :task.getResult()){

                                                Date dcomp1 = document.getDate("expiration");
                                                Date today;

                                                DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                                                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                                                LocalDateTime now = LocalDateTime.now();
                                                String tdate = dtf.format(now);



                                                try {
                                                    today = formatter.parse(tdate);
                                                    if (today.compareTo(dcomp1) <= 0) {
                                                        if (!document.getString("count").equals("0")){
                                                            RecommendedVouchers recommendedVouchers = document.toObject(RecommendedVouchers.class);
                                                            recommendedVouchersList.add(recommendedVouchers);
                                                            recommendedVoucherAdapter.notifyDataSetChanged();
                                                        }
                                                    }
                                                }catch (ParseException e) {
                                                    e.printStackTrace();
                                                }






                                            }
                                        } else {
                                            Toast.makeText(getActivity(), "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                });







        return rootView;
    }


}
