package com.example.bottomnavigationview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecommendedVoucherAdapter extends RecyclerView.Adapter<RecommendedVoucherAdapter.ViewHolder>{

    Context context;
    List<RecommendedVouchers> list;

    public RecommendedVoucherAdapter(Context context, List<RecommendedVouchers> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment2_voucher_main,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.infoname.setText(list.get(position).getInfoname());
        holder.infodesc.setText(list.get(position).getInfodesc());
        holder.docid = list.get(position).getdiscode();
        holder.estname = list.get(position).getestname();

        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        Date thisdate = list.get(position).getexpiration().toDate();
        String strDate = sdfDate.format(thisdate);
        holder.expiration.setText(strDate);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment fragment = new VDesc();
                Bundle bundle3 = new Bundle();
                bundle3.putCharSequence("di", list.get(position).getdiscode());
                bundle3.putCharSequence("en", list.get(position).getestname());
                bundle3.putCharSequence("in", list.get(position).getInfoname());
                bundle3.putCharSequence("id", list.get(position).getInfodesc());
                bundle3.putCharSequence("ed", strDate);
                bundle3.putCharSequence("do", list.get(position).getDocid());

                fragment.setArguments(bundle3);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
                Log.d("List", "doc clicked is "+list.get(position).getdiscode());



            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView infoname,infodesc,expiration;
        String docid, estname;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            infoname = itemView.findViewById(R.id.rec_name2);
            infodesc = itemView.findViewById(R.id.rec_des);
            expiration = itemView.findViewById(R.id.rec_expires);

        }
    }


}
