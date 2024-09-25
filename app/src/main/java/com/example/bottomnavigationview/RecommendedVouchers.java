package com.example.bottomnavigationview;

import com.google.firebase.Timestamp;

public class RecommendedVouchers {
    String infoname;
    String infodesc;
    Timestamp expiration;
    String discode;
    String estname;
    String docid;
    boolean valid;



    public RecommendedVouchers(){

    }
    public RecommendedVouchers(String infoname, String infodesc, String discode, Timestamp expiration, String estname, boolean valid, String docid) {
        this.infoname = infoname;
        this.infodesc = infodesc;
        this.expiration = expiration;
        this.discode = discode;
        this.estname = estname;
        this.docid = docid;
        this.valid = valid;

    }

    public String getInfoname() {

        return infoname;
    }

    public void setInfoname(String infoname) {

        this.infoname = infoname;
    }

    public String getDocid() {

        return docid;
    }

    public void setDocid(String docid) {

        this.docid = docid;
    }


    public String getestname() {

        return estname;
    }

    public void setestname(String estname) {

        this.estname = estname;
    }

    public String getdiscode() {

        return discode;
    }

    public void setdiscode(String discode) {

        this.discode = discode;
    }

    public Timestamp getexpiration() {

        return expiration;
    }

    public void setexpiration(Timestamp expiration) {

        this.expiration = expiration;
    }

    public String getInfodesc() {

        return infodesc;
    }

    public void setInfodesc(String infodesc) {

        this.infodesc = infodesc;
    }


    public boolean getvalid() {

        return valid;
    }

    public void setvalid(boolean valid) {

        this.valid = valid;
    }
}
