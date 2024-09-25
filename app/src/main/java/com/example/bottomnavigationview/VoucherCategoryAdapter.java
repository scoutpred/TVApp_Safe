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

import java.util.List;

public class VoucherCategoryAdapter extends RecyclerView.Adapter<VoucherCategoryAdapter.ViewHolder> {

    Context context;
    List<VoucherCategory> categoryList;

    public VoucherCategoryAdapter(Context context, List<VoucherCategory> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment1_category,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Glide.with(context).load(categoryList.get(position).getImg_url()).into(holder.catImg);
        holder.name.setText(categoryList.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment fragment = new Fragment5();
                Bundle bundle = new Bundle();
                bundle.putCharSequence("estname", categoryList.get(position).getType());
                fragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
                Log.d("List", "doc clicked is "+categoryList.get(position).getType());


            }
        });

    }

    @Override
    public int getItemCount() {

        return categoryList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView catImg;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            catImg = itemView.findViewById(R.id.category_img);
            name = itemView.findViewById(R.id.category_name);
        }
    }
}
