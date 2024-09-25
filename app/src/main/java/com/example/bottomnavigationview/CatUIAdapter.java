package com.example.bottomnavigationview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CatUIAdapter extends RecyclerView.Adapter<CatUIAdapter.ViewHolder>{
    Context context;
    List<CatModel> list;

    //cats!

    public CatUIAdapter(Context context, List<CatModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CatUIAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CatUIAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment2_catui,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CatUIAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.catname.setText(list.get(position).getestname());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment fragment = new Fragment6();
                Bundle bundle = new Bundle();
                bundle.putCharSequence("docid", list.get(position).getdocid());
                fragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
                Log.d("List", "doc clicked is "+list.get(position).getdocid());


            }
        });
    }

    public void updateList(List<CatModel> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView catname;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            catname = itemView.findViewById(R.id.rec_name2);


        }
    }

}
