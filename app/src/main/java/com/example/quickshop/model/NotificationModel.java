package com.example.quickshop.model;

public class NotificationModel {

    String text_msg;
    String Order_id;
    long timestamp;
    boolean new_notification;

    public boolean isNew_notification() {
        return new_notification;
    }

    public void setNew_notification(boolean new_notification) {
        this.new_notification = new_notification;
    }

    public NotificationModel(String text_msg, String order_id, long timestamp, boolean new_notification) {
        this.text_msg = text_msg;
        Order_id = order_id;
        this.timestamp = timestamp;
        this.new_notification = new_notification;
    }

    public NotificationModel() {    }

    public String getText_msg() {
        return text_msg;
    }

    public void setText_msg(String text_msg) {
        this.text_msg = text_msg;
    }

    public String getOrder_id() {
        return Order_id;
    }

    public void setOrder_id(String order_id) {
        Order_id = order_id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
