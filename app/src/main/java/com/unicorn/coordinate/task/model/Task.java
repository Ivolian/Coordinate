package com.unicorn.coordinate.task.model;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Task  {


    /**
     * matchuserid : 2e169eb8-f8b1-4f62-a62d-eb788c126e87
     * lines_id : a3eec3f4-1d30-4d89-8a87-9e8117e6faee
     * teamname : KT9bqpanCMwGdmK0iMUFCA==
     * linename : TfoDIz5aRqX4J5BK1e69Bw==
     * nickname : wMHR+nxcHXj9qdz3iLE8kQ==
     * match_name : 0IHLXWQjnLdrKWlbs9BDrmQjmIiMgr4eaA+Bw+1oVQzPSTOikRQI+kYsxCcETH27FaOExxAGc38srMImcb3n8g==
     * isdown : 2
     * teamno : 30002
     * tasklogo : http://img.chengshidingxiang.com/taian_2017.jpg
     * logopic : http://img.chengshidingxiang.com/logo_taian2017.png
     * date4 : 2017-05-20
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
    private String date4;

    @Generated(hash = 803651617)
    public Task(String matchuserid, String lines_id, String teamname, String linename, String nickname,
            String match_name, String isdown, String teamno, String tasklogo, String logopic, String date4) {
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
        this.date4 = date4;
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

    public String getDate4() {
        return date4;
    }

    public void setDate4(String date4) {
        this.date4 = date4;
    }
}
