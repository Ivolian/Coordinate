package com.unicorn.coordinate.user.model;


import java.io.Serializable;

public class UserInfo implements Serializable{

    /**
     * userid : beed1725-063e-412c-9f0a-6602fb251b16
     * Name : null
     * Playerid : 0
     * Mobile : 13611840424
     * Passwd : 96E79218965EB72C92A549DD5A330112
     * sexy : null
     * cardtype : null
     * cardno : null
     * mono : 051516
     * birthday : null
     * Last_Time : 2016-09-10 14:43:06
     * Status : 0
     * DeviceToken : -
     */

    private String userid;
    private String Name;
    private int Playerid;
    private String Mobile;
    private String Passwd;
    private String sexy;
    private String cardtype;
    private String cardno;
    private String mono;
    private String birthday;
    private String Last_Time;
    private int Status;
    private String DeviceToken;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getPlayerid() {
        return Playerid;
    }

    public void setPlayerid(int playerid) {
        Playerid = playerid;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getPasswd() {
        return Passwd;
    }

    public void setPasswd(String passwd) {
        Passwd = passwd;
    }

    public String getSexy() {
        return sexy;
    }

    public void setSexy(String sexy) {
        this.sexy = sexy;
    }

    public String getCardtype() {
        return cardtype;
    }

    public void setCardtype(String cardtype) {
        this.cardtype = cardtype;
    }

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getMono() {
        return mono;
    }

    public void setMono(String mono) {
        this.mono = mono;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getLast_Time() {
        return Last_Time;
    }

    public void setLast_Time(String last_Time) {
        Last_Time = last_Time;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getDeviceToken() {
        return DeviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        DeviceToken = deviceToken;
    }
}
