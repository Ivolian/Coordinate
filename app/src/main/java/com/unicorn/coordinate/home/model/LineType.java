package com.unicorn.coordinate.home.model;

public class LineType {

    /**
     * Lineid : 360f3864-6cb1-4fa7-8931-71db033f977f
     * Match_id : 211a812b-57c2-4e03-abb5-02f95f558fbe
     * Name : 分赛场线路
     * Players : 5
     * Count : 200
     * Content : 1111
     * Conditions :
     * Createtime : 2017-02-07 22:56:21
     * Status : 0
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
