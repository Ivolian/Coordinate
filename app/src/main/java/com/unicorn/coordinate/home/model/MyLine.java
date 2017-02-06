package com.unicorn.coordinate.home.model;

public class MyLine {


    /**
     * Lineid : 38bac529-fe5a-4a23-8fdd-03ee244b2f45
     * Match_id : 5585b465-ced3-4d42-a250-8fc866d8a983
     * Name : 汽车
     * Players : 4
     * Count : 1
     * Content : 汽车线路
     * Conditions : {players:"4",count:"1"}
     * Createtime : 2017-02-06 11:24:36
     * Status : 1
     * PersonPrice : 0.01
     * TeamPrice : 0.01
     */

    private String Lineid;
    private String Match_id;
    private String Name;
    private int Players;
    private int Count;
    private String Content;
    private String Conditions;
    private String Createtime;
    private int Status;
    private double PersonPrice;
    private double TeamPrice;

    public String getLineid() {
        return Lineid;
    }

    public void setLineid(String Lineid) {
        this.Lineid = Lineid;
    }

    public String getMatch_id() {
        return Match_id;
    }

    public void setMatch_id(String Match_id) {
        this.Match_id = Match_id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public int getPlayers() {
        return Players;
    }

    public void setPlayers(int Players) {
        this.Players = Players;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int Count) {
        this.Count = Count;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public String getConditions() {
        return Conditions;
    }

    public void setConditions(String Conditions) {
        this.Conditions = Conditions;
    }

    public String getCreatetime() {
        return Createtime;
    }

    public void setCreatetime(String Createtime) {
        this.Createtime = Createtime;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public double getPersonPrice() {
        return PersonPrice;
    }

    public void setPersonPrice(double PersonPrice) {
        this.PersonPrice = PersonPrice;
    }

    public double getTeamPrice() {
        return TeamPrice;
    }

    public void setTeamPrice(double TeamPrice) {
        this.TeamPrice = TeamPrice;
    }

}
