package com.example.bottomnavigationview;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class Fragment6 extends Fragment {

    RecyclerView VoucherList;
    FirebaseFirestore db;
    EditText sb;

    List<VoucherDataModel> voucherList;
    VoucherDataAdapter voucherDataAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment2_vcategory,container,false);

        db = FirebaseFirestore.getInstance();
        sb = rootView.findViewById(R.id.search_box);
        Bundle bundle = this.getArguments();
        Bundle bundle2 = this.getArguments();
        String est = bundle.getString("estname");
        String doci = bundle.getString("docid");


        VoucherList = rootView.findViewById(R.id.view_category);


        //for Voucher List
        VoucherList.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        voucherList = new ArrayList<>();
        voucherDataAdapter = new VoucherDataAdapter(getActivity(),voucherList);
        VoucherList.setAdapter(voucherDataAdapter);

        db.collection("estDB").document(doci).collection("Vouchers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
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
                                VoucherDataModel vchers = document.toObject(VoucherDataModel.class);
                                voucherList.add(vchers);
                                voucherDataAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                        } else {

                        }
                    }
                });

        sb.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // filter your list from your input
                filter(s.toString());
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });



        return rootView;
    }

    void filter(String text){
        List<VoucherDataModel> temp = new ArrayList();
        for(VoucherDataModel d: voucherList){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.getInfoname().toLowerCase().contains(text.toLowerCase()) || d.getestname().toLowerCase().contains(text.toLowerCase())){
                temp.add(d);
            }
        }
        //update recyclerview
        voucherDataAdapter.updateList(temp);
    }


}
