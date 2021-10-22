package com.example.quickshop.model;

public class CartModel {
    private String pId;
    private String pName;
    private String pWeight;
    private int pQuantity;
    private int pAmount;

    public CartModel(String pId, String pName, String pWeight, int pQuantity, int pAmount) {
        this.pId = pId;
        this.pName = pName;
        this.pWeight = pWeight;
        this.pQuantity = pQuantity;
        this.pAmount = pAmount;
    }

    public CartModel(){
    }

    public String getpId() {
        return pId;
    }
    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getpName() {
        return pName;
    }
    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getpWeight() {
        return pWeight;
    }
    public void setpWeight(String pWeight) {
        this.pWeight = pWeight;
    }

    public int getpQuantity() {
        return pQuantity;
    }
    public void setpQuantity(int pQuantity) {
        this.pQuantity = pQuantity;
    }

    public int getpAmount() {
        return pAmount;
    }
    public void setpAmount(int pAmount) {
        this.pAmount = pAmount;
    }


}
