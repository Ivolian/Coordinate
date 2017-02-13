package com.unicorn.coordinate.home.model;

import java.io.Serializable;

public class MyMatchStatus implements Serializable {


    /**
     * Teamid : 85bdd523-2f24-48f3-aa79-2bb576e22df4
     * Teamname : dVR9TFItwNiCkbmlo2xDBg==
     * Status : 2
     * IsLeader : 1
     * MacthStatus : 3
     * Linesname :
     */

    private String Teamid;
    private String Teamname;
    private String Status;
    private String IsLeader;
    private String MacthStatus;
    private String Linesname;

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
}
