package com.example.bottomnavigationview;

import androidx.recyclerview.widget.RecyclerView;

public class VoucherCategory {

    String name;
    String img_url;
    String type;
    public VoucherCategory(){

    }

    public VoucherCategory(String name, String img_url, String type){
        this.name = name;
        this.img_url= img_url;
        this.type=type;
    }

    public String getName() {

        return name;
    }

    public String getImg_url() {

        return img_url;
    }

    public String getType() {

        return type;
    }


}
