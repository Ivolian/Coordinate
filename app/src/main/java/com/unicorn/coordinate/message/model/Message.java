package com.unicorn.coordinate.message.model;


public class Message  {

    /**
     * Infoid : 4ba7dfc2-db70-485f-a5ca-adb4e07cea11
     * Type : 3
     * Createtime : 2016-08-25 11:02:07
     * Userid : b5b80979-a9c9-44b3-b326-d83b4db604c7
     * Mobile : 18601720508
     * Context : 用户[13636671497]邀请你加入[芈月&半月]队伍,参加[中国坐标·上海徐汇城市定向户外挑战赛],赶快去看看并接受邀请吧.
     * Status : 0
     * Url : null
     * Note : null
     */

    private String Infoid;
    private String Type;
    private String Createtime;
    private String Userid;
    private String Mobile;
    private String Context;
    private String Status;
    private String Url;
    private String Note;

    public String getInfoid() {
        return Infoid;
    }

    public void setInfoid(String Infoid) {
        this.Infoid = Infoid;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public String getCreatetime() {
        return Createtime;
    }

    public void setCreatetime(String Createtime) {
        this.Createtime = Createtime;
    }

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String Userid) {
        this.Userid = Userid;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }

    public String getContext() {
        return Context;
    }

    public void setContext(String Context) {
        this.Context = Context;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }
}
