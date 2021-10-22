package com.example.quickshop.model;

public class FAQ_Model {

    private String Fid;
    private String question;
    private String answer;
    private String user_name;
    private long timestamp;
    private boolean verified;

    public FAQ_Model() {    }

    public FAQ_Model(String fid,String question, String answer, String user_name, long timestamp, boolean verified) {
        this.Fid = fid;
        this.question = question;
        this.answer = answer;
        this.user_name = user_name;
        this.timestamp = timestamp;
        this.verified = verified;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getFid() { return Fid; }

    public void setFid(String fid) { Fid = fid; }
}
