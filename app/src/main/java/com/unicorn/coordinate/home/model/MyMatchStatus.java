package com.unicorn.coordinate.home.model;

import java.io.Serializable;

public class MyMatchStatus implements Serializable{

    /**
     * Teamid :
     * Status : 1
     * IsLeader :
     */

    private String Teamid;
    private String Status;
    private String IsLeader;

    public String getTeamid() {
        return Teamid;
    }

    public void setTeamid(String Teamid) {
        this.Teamid = Teamid;
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

}
