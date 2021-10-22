package com.example.quickshop.model;

public class OrderModel {
    private String pick_chooseBranch;
    private String pick_chooseTime;
    private String pickdate;
    private String oID;
    private String cName;
    private String cContact;
    private String uID;
    private String paymentID;
    private String timeStamp;
    private int total_amount;
    private int pay_amount;
    private int orderStatus;
    private String O_C_Time;

    public OrderModel(){ }


    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }



    public String getPick_chooseBranch() {
        return pick_chooseBranch;
    }

    public void setPick_chooseBranch(String pick_chooseBranch) { this.pick_chooseBranch = pick_chooseBranch; }


    public String getPick_chooseTime() {
        return pick_chooseTime;
    }

    public void setPick_chooseTime(String pick_chooseTime) { this.pick_chooseTime = pick_chooseTime; }


    public String getPickdate() {
        return pickdate;
    }

    public void setPickdate(String pickdate) {
        this.pickdate = pickdate;
    }


    public String getoID() {
        return oID;
    }

    public void setoID(String oID) {
        this.oID = oID;
    }


    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }


    public String getcContact() {
        return cContact;
    }

    public void setcContact(String cContact) {
        this.cContact = cContact;
    }


    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }


    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }


    public int getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(int total_amount) {
        this.total_amount = total_amount;
    }


    public int getPay_amount() {
        return pay_amount;
    }

    public void setPay_amount(int pay_amount) {
        this.pay_amount = pay_amount;
    }


    public int getOrderStatus() { return orderStatus; }

    public void setOrderStatus(int orderStatus) { this.orderStatus = orderStatus; }


    public String getO_C_Time() { return O_C_Time; }

    public void setO_C_Time(String o_C_Time) { O_C_Time = o_C_Time; }


    public OrderModel(String pick_chooseBranch, String pick_chooseTime, String pickdate, String oID, String cName, String cContact, String uID, String paymentID, int total_amount, int pay_amount, String timeStamp, int orderStatus, String o_c_time) {
        this.pick_chooseBranch = pick_chooseBranch;
        this.pick_chooseTime = pick_chooseTime;
        this.pickdate = pickdate;
        this.oID = oID;
        this.cName = cName;
        this.cContact = cContact;
        this.uID = uID;
        this.paymentID = paymentID;
        this.total_amount = total_amount;
        this.pay_amount = pay_amount;
        this.timeStamp = timeStamp;
        this.orderStatus = orderStatus;
        O_C_Time = o_c_time;
    }
}
