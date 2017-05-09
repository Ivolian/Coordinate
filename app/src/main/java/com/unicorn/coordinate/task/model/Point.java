package com.unicorn.coordinate.task.model;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

@Entity
public class Point implements Serializable{

    /**
     * Pointid : 27ae79f2-ba06-4ea2-9441-2f40285ec744
     * Lineguid : a3eec3f4-1d30-4d89-8a87-9e8117e6faee
     * Id :
     * Eventid :
     * Lineid :
     * Pointname : exuzkBP0daJrqzUIcU7yjw==
     * Content : dub1agYeXAmiuaviT6NH0w==
     * Sort : 10
     * Pointtype : 2
     * Status : 0
     * Pointno : JD10
     * Pointaddress : 终点地址
     * Pointtask : AtXYNvVVPBn9YNRBR2Rr+x4S+qhZF5aOWj8mfWvKrM+aUAHxnE6M2DxJdEaWjXA7Ipf6mgHBU+jlG+nKQlQ58dKwm1m55xf+Ngt656cT6F7UrG5IDDvh3l3LjWyP/ocihP9xe/QaLWw/UPGeI1MZPEy+gpHLWxS4ovKQOqxZjSfltdUtdr0sgIY1Lok0jdnfBgmsP1uTwkYfwg3UR9U4z35gg5hHFRO9jqtiXxu2hLGL60KEpvcQR9zQrpSKXLM2Id6+b9T0LL36yEl93820D8mXipgLJ3rFg+vffjR7ArBkmZEPbMMwGNGCFMKefikR
     * Pointout : WVbEhJc7U6uiLexbIdYzrg==
     * Sketchmap : http://139.196.107.169:9000/UploadFiles/JD10_image_20170426164406.png
     * Sketchvoice :
     * linkno :
     */

    @org.greenrobot.greendao.annotation.Id
    private String Pointid;
    private String Lineguid;
    private String Id;
    private String Eventid;
    private String Lineid;
    private String Pointname;
    private String Content;
    private int Sort;
    private int Pointtype;
    private int Status;
    private String Pointno;
    private String Pointaddress;
    private String Pointtask;
    private String Pointout;
    private String Sketchmap;
    private String Sketchvoice;
    private String linkno;

    public String getPointid() {
        return Pointid;
    }

    public void setPointid(String Pointid) {
        this.Pointid = Pointid;
    }

    public String getLineguid() {
        return Lineguid;
    }

    public void setLineguid(String Lineguid) {
        this.Lineguid = Lineguid;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getEventid() {
        return Eventid;
    }

    public void setEventid(String Eventid) {
        this.Eventid = Eventid;
    }

    public String getLineid() {
        return Lineid;
    }

    public void setLineid(String Lineid) {
        this.Lineid = Lineid;
    }

    public String getPointname() {
        return Pointname;
    }

    public void setPointname(String Pointname) {
        this.Pointname = Pointname;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public int getSort() {
        return Sort;
    }

    public void setSort(int Sort) {
        this.Sort = Sort;
    }

    public int getPointtype() {
        return Pointtype;
    }

    public void setPointtype(int Pointtype) {
        this.Pointtype = Pointtype;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public String getPointno() {
        return Pointno;
    }

    public void setPointno(String Pointno) {
        this.Pointno = Pointno;
    }

    public String getPointaddress() {
        return Pointaddress;
    }

    public void setPointaddress(String Pointaddress) {
        this.Pointaddress = Pointaddress;
    }

    public String getPointtask() {
        return Pointtask;
    }

    public void setPointtask(String Pointtask) {
        this.Pointtask = Pointtask;
    }

    public String getPointout() {
        return Pointout;
    }

    public void setPointout(String Pointout) {
        this.Pointout = Pointout;
    }

    public String getSketchmap() {
        return Sketchmap;
    }

    public void setSketchmap(String Sketchmap) {
        this.Sketchmap = Sketchmap;
    }

    public String getSketchvoice() {
        return Sketchvoice;
    }

    public void setSketchvoice(String Sketchvoice) {
        this.Sketchvoice = Sketchvoice;
    }

    public String getLinkno() {
        return linkno;
    }

    public void setLinkno(String linkno) {
        this.linkno = linkno;
    }


    private static final long serialVersionUID = 1L;

    // 手动添加的字段，返回数据中没有
    private String matchuserid;
    private String Pointtime;
    
    @Generated(hash = 1977038299)
    public Point() {
    }

    @Generated(hash = 1427496592)
    public Point(String Pointid, String Lineguid, String Id, String Eventid, String Lineid, String Pointname, String Content, int Sort, int Pointtype, int Status, String Pointno, String Pointaddress, String Pointtask, String Pointout, String Sketchmap, String Sketchvoice, String linkno, String matchuserid, String Pointtime) {
        this.Pointid = Pointid;
        this.Lineguid = Lineguid;
        this.Id = Id;
        this.Eventid = Eventid;
        this.Lineid = Lineid;
        this.Pointname = Pointname;
        this.Content = Content;
        this.Sort = Sort;
        this.Pointtype = Pointtype;
        this.Status = Status;
        this.Pointno = Pointno;
        this.Pointaddress = Pointaddress;
        this.Pointtask = Pointtask;
        this.Pointout = Pointout;
        this.Sketchmap = Sketchmap;
        this.Sketchvoice = Sketchvoice;
        this.linkno = linkno;
        this.matchuserid = matchuserid;
        this.Pointtime = Pointtime;
    }



    public String getMatchuserid() {
        return matchuserid;
    }

    public void setMatchuserid(String matchuserid) {
        this.matchuserid = matchuserid;
    }

    public String getPointtime() {
        return Pointtime;
    }

    public void setPointtime(String pointtime) {
        Pointtime = pointtime;
    }


}
