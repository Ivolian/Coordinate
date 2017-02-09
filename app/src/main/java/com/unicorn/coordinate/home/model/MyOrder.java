package com.unicorn.coordinate.home.model;

import java.io.Serializable;

public class MyOrder implements Serializable{

    // Title描述，orderid订单号(传给支付宝的)，ordertotal价格

    /**
     * Id : a0c8ab9e-1977-4c85-b216-f7d636ff2a8f
     * Match_Id : 211a812b-57c2-4e03-abb5-02f95f558fbe
     * Orderid : F63622172713551148692
     * Teamid : d1f44715-3855-4ce0-ac24-b0dd2209cc2b
     * Lineid : 16437e41-c921-466a-9fb9-e59eb2004113
     * Userid : 3f9713dd-5ba5-11e6-a2c5-6c92bf314f0b
     * Ordertotal : 0.01
     * Status : 0
     * Createtime :
     * Title : [测试比赛11]报名费用
     * Paytime :
     */

    private String Id;
    private String Match_Id;
    private String Orderid;
    private String Teamid;
    private String Lineid;
    private String Userid;
    private String Ordertotal;
    private int Status;
    private String Createtime;
    private String Title;
    private String Paytime;

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getMatch_Id() {
        return Match_Id;
    }

    public void setMatch_Id(String Match_Id) {
        this.Match_Id = Match_Id;
    }

    public String getOrderid() {
        return Orderid;
    }

    public void setOrderid(String Orderid) {
        this.Orderid = Orderid;
    }

    public String getTeamid() {
        return Teamid;
    }

    public void setTeamid(String Teamid) {
        this.Teamid = Teamid;
    }

    public String getLineid() {
        return Lineid;
    }

    public void setLineid(String Lineid) {
        this.Lineid = Lineid;
    }

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String Userid) {
        this.Userid = Userid;
    }

    public String getOrdertotal() {
        return Ordertotal;
    }

    public void setOrdertotal(String Ordertotal) {
        this.Ordertotal = Ordertotal;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public String getCreatetime() {
        return Createtime;
    }

    public void setCreatetime(String Createtime) {
        this.Createtime = Createtime;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getPaytime() {
        return Paytime;
    }

    public void setPaytime(String Paytime) {
        this.Paytime = Paytime;
    }
}
