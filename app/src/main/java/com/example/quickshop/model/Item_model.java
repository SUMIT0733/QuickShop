package com.example.quickshop.model;

import android.net.Uri;

import java.io.Serializable;

public class Item_model  {

    private  String pName;
    private  String pWeight;
    private  String pAmount;

    public String getpUrl() {
        return pUrl;
    }

    private  String pUrl;
    private String pId;

    public Item_model(String pName, String pWeight, String pAmount, String pUrl, String pId) {
        this.pName = pName;
        this.pWeight = pWeight;
        this.pAmount = pAmount;
        this.pUrl = pUrl;
        this.pId = pId;
    }

    public Item_model() { }

    public  String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }


    public  String getpWeight() {
        return pWeight;
    }

    public void setpWeight(String pWeight) {
        this.pWeight = pWeight;
    }


    public  String getpAmount() {
        return pAmount;
    }

    public void setpAmount(String pAmount) {
        this.pAmount = pAmount;
    }


//    public  Uri getpUrl() {
//        return Uri.parse(pUrl);
//    }

    public void setpUrl(String pUrl) {
        this.pUrl = pUrl;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }
}
