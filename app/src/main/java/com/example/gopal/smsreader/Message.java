package com.example.gopal.smsreader;

/**
 * Created by Gopal on 11/10/2019.
 */

public class Message {
    private String id;
    private String address;
    private String msg;
    private String readState; //"0" for have not read sms and "1" for have read sms
    private String time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getReadState() {
        return readState;
    }

    public void setReadState(String readState) {
        this.readState = readState;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
