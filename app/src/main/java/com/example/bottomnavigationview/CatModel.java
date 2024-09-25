package com.example.bottomnavigationview;


public class CatModel {

    String estname, docid;

    public CatModel(){

    }
    public CatModel(String estname, String docid) {
        this.estname = estname;
        this.docid = docid;

    }

    public String getestname() {

        return estname;
    }

    public void setestname(String estname) {

        this.estname = estname;
    }

    public String getdocid() {

        return docid;
    }

    public void setdocid(String docid) {

        this.docid = docid;
    }

}
