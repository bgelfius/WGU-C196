package com.example.bgelfius.c196_wgu;

public class PassableData {
    private  String keyname;
    private Long keyID;

    public PassableData(String keyname, Long keyID) {
        this.keyname = keyname;
        this.keyID = keyID;
    }

    public PassableData() {
    }

    public String getKeyname() {
        return keyname;
    }

    public void setKeyname(String keyname) {
        this.keyname = keyname;
    }

    public Long getKeyID() {
        return keyID;
    }

    public void setKeyID(Long keyID) {
        this.keyID = keyID;
    }
}
