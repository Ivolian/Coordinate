package com.unicorn.coordinate.home.model;

public class Line {

    /**
     * Lineid : 06ad7679-00a1-46db-aafd-2c526ee24f6a
     * Match_id : d3335b57-3835-46f9-85f4-4b2bcfb25e24
     * Name : 亲子线路
     * Players : 5
     * Count : 100
     * Content : 两个大人一个小孩
     * Conditions : {players:"5",count:"100"}
     * Createtime : 2017-01-30 21:02:32
     * Status : 2
     * PersonPrice : 60.0
     * TeamPrice : 180.0
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
