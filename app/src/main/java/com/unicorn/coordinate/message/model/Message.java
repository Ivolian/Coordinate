package com.unicorn.coordinate.message.model;


import java.io.Serializable;

public class Message implements Serializable {

    /**
     * Infoid : e509ea3b-0e5d-4939-ae90-5bd2e2d16d48
     * Type : 3
     * createtime : 2017-02-04 22:08:36
     * Userid : 3f971696-5ba5-11e6-a2c5-6c92bf314f0b
     * Mobile : 18000000025
     * Context : 用户[18000000021]邀请你加入[对得起]队伍,参加[测试比赛2],赶快去看看并接受邀请吧.
     * Status : 0
     * Url :
     * Note :
     * Field1 : aeb4f195-c1bd-4935-8d2b-4699178b47c0
     * Field2 : 0
     */

    private String Infoid;
    private String Type;
    private String createtime;
    private String Userid;
    private String Mobile;
    private String Context;
    private String Status;
    private String Url;
    private String Note;
    private String Field1;
    private String Field2;

    public String getInfoid() {
        return Infoid;
    }

    public void setInfoid(String Infoid) {
        this.Infoid = Infoid;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String Userid) {
        this.Userid = Userid;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }

    public String getContext() {
        return Context;
    }

    public void setContext(String Context) {
        this.Context = Context;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String Url) {
        this.Url = Url;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String Note) {
        this.Note = Note;
    }

    public String getField1() {
        return Field1;
    }

    public void setField1(String Field1) {
        this.Field1 = Field1;
    }

    public String getField2() {
        return Field2;
    }

    public void setField2(String Field2) {
        this.Field2 = Field2;
    }
}
