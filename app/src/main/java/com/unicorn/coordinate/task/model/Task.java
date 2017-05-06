package com.unicorn.coordinate.task.model;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Task  {

    /**
     * matchuserid : adebff57-b727-44b4-9c2c-21604b098ed0
     * lines_id : a3eec3f4-1d30-4d89-8a87-9e8117e6faee
     * teamname : nPanI7zQJ1u4JDpKiXwsLg==
     * linename : TfoDIz5aRqX4J5BK1e69Bw==
     * nickname : AJOBgX7zsnpbB4nak1s3cg==
     * match_name : 0IHLXWQjnLdrKWlbs9BDrmQjmIiMgr4eaA+Bw+1oVQzPSTOikRQI+kYsxCcETH27FaOExxAGc38srMImcb3n8g==
     * isdown : 2
     * teamno : 30001
     * tasklogo : shanghai_2017.jpg
     * logopic : log_shanghai.png
     */

    @Id
    private String matchuserid;
    private String lines_id;
    private String teamname;
    private String linename;
    private String nickname;
    private String match_name;
    private String isdown;
    private String teamno;
    private String tasklogo;
    private String logopic;

    @Generated(hash = 1874138248)
    public Task(String matchuserid, String lines_id, String teamname, String linename, String nickname,
            String match_name, String isdown, String teamno, String tasklogo, String logopic) {
        this.matchuserid = matchuserid;
        this.lines_id = lines_id;
        this.teamname = teamname;
        this.linename = linename;
        this.nickname = nickname;
        this.match_name = match_name;
        this.isdown = isdown;
        this.teamno = teamno;
        this.tasklogo = tasklogo;
        this.logopic = logopic;
    }

    @Generated(hash = 733837707)
    public Task() {
    }

    public String getMatchuserid() {
        return matchuserid;
    }

    public void setMatchuserid(String matchuserid) {
        this.matchuserid = matchuserid;
    }

    public String getLines_id() {
        return lines_id;
    }

    public void setLines_id(String lines_id) {
        this.lines_id = lines_id;
    }

    public String getTeamname() {
        return teamname;
    }

    public void setTeamname(String teamname) {
        this.teamname = teamname;
    }

    public String getLinename() {
        return linename;
    }

    public void setLinename(String linename) {
        this.linename = linename;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMatch_name() {
        return match_name;
    }

    public void setMatch_name(String match_name) {
        this.match_name = match_name;
    }

    public String getIsdown() {
        return isdown;
    }

    public void setIsdown(String isdown) {
        this.isdown = isdown;
    }

    public String getTeamno() {
        return teamno;
    }

    public void setTeamno(String teamno) {
        this.teamno = teamno;
    }

    public String getTasklogo() {
        return tasklogo;
    }

    public void setTasklogo(String tasklogo) {
        this.tasklogo = tasklogo;
    }

    public String getLogopic() {
        return logopic;
    }

    public void setLogopic(String logopic) {
        this.logopic = logopic;
    }

}
