package com.unicorn.coordinate.home.model;

import java.io.Serializable;

public class MyMatchStatus implements Serializable {


    /**
     * Teamid : b68deab0-1467-41c2-914d-cb442f83f6c4
     * Teamname : 3fBOiLuojaHfh6ihZOZevw==
     * Status : 3
     * IsLeader : 1
     * MacthStatus : 3
     * Linesname : Pdvg+yUUqQDwSKFc2k4QNg==
     * TeamType : 0
     */

    private String Teamid;
    private String Teamname;
    private String Status;
    private String IsLeader;
    private String MacthStatus;
    private String Linesname;
    private int TeamType;

    public String getTeamid() {
        return Teamid;
    }

    public void setTeamid(String Teamid) {
        this.Teamid = Teamid;
    }

    public String getTeamname() {
        return Teamname;
    }

    public void setTeamname(String Teamname) {
        this.Teamname = Teamname;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getIsLeader() {
        return IsLeader;
    }

    public void setIsLeader(String IsLeader) {
        this.IsLeader = IsLeader;
    }

    public String getMacthStatus() {
        return MacthStatus;
    }

    public void setMacthStatus(String MacthStatus) {
        this.MacthStatus = MacthStatus;
    }

    public String getLinesname() {
        return Linesname;
    }

    public void setLinesname(String Linesname) {
        this.Linesname = Linesname;
    }

    public int getTeamType() {
        return TeamType;
    }

    public void setTeamType(int TeamType) {
        this.TeamType = TeamType;
    }
}
