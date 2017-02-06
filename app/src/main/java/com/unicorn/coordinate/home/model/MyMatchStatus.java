package com.unicorn.coordinate.home.model;

import java.io.Serializable;

public class MyMatchStatus implements Serializable{


    /**
     * Teamid : 945153b6-23bd-4dd5-8810-4ed3714732a6
     * Status : 3
     * IsLeader : 1
     * MacthStatus : 3
     */

    private String Teamid;
    private String Status;
    private String IsLeader;
    private String MacthStatus;

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

    public String getMacthStatus() {
        return MacthStatus;
    }

    public void setMacthStatus(String MacthStatus) {
        this.MacthStatus = MacthStatus;
    }
}
