package com.example.quickshop.model;

public class CheckoutModel {
    private String pId;
    private String pName;
    private int pQuantity;
    private int pAmount;

    public CheckoutModel(String pId, String pName, int pQuantity, int pAmount) {
        this.pId = pId;
        this.pName = pName;
        this.pQuantity = pQuantity;
        this.pAmount = pAmount;
    }
    public CheckoutModel(){
    }
    public String getpId() { return pId; }
    public void setpId(String pId) { this.pId = pId; }

    public String getpName() { return pName; }
    public void setpName(String pName) { this.pName = pName; }

    public int getpQuantity() { return pQuantity; }
    public void setpQuantity(int pQuantity) { this.pQuantity = pQuantity; }

    public int getpAmount() { return pAmount; }
    public void setpAmount(int pAmount) { this.pAmount = pAmount; }
}
