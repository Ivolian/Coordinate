package com.unicorn.coordinate.home.model;

import java.io.Serializable;

// 和 Match 区别不大，开发过程中的多余产物
public class MatchInfo implements Serializable{

    /**
     * Match_id : 6a61b95b-2d5d-4373-abaf-776aa0e67fcf
     * Match_name : 梦想坐标·环球城市定向挑战赛冲绳站
     * Content : 2016年，依托上海市登山户外运动协会在赛事举办方面的丰富经验与资源，我公司与之合作，结合体育+旅游的创新概念，创办“梦想坐标·环球城市定向挑战赛”，选手们将于12月03日搭乘豪华舒适的公主邮轮蓝宝石号前往美丽的日本冲绳参赛（12月06日）。这次定向赛，报名者的门槛降低，注重参与者的体验，充分体现趣味与互动，让队员们自由穿梭在美丽的度假胜地，收获属于自己独一无二的旅行体验。同时，我们邀请明星选手，并进行赛事直播，共邀您一同体验与众不同的冲绳之旅。
     * Area1 : 日本
     * Area2 : 冲绳
     * Date1 : 2016-10-01 00:09:00
     * Date2 : 2016-10-10 09:00:00
     * Date3 : 2016-10-20 09:00:00
     * Date4 : 2016-12-06 10:00:00
     * Pic1 : Big_chongshen_2016.jpg
     * Pic2 : chongshen.jpg
     * ispic : 0
     * logopic : lo03.png
     * Status : 5
     */

    private String Match_id;
    private String Match_name;
    private String Content;
    private String Area1;
    private String Area2;
    private String Date1;
    private String Date2;
    private String Date3;
    private String Date4;
    private String Pic1;
    private String Pic2;
    private String ispic;
    private String logopic;
    private String Status;

    public String getMatch_id() {
        return Match_id;
    }

    public void setMatch_id(String Match_id) {
        this.Match_id = Match_id;
    }

    public String getMatch_name() {
        return Match_name;
    }

    public void setMatch_name(String Match_name) {
        this.Match_name = Match_name;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public String getArea1() {
        return Area1;
    }

    public void setArea1(String Area1) {
        this.Area1 = Area1;
    }

    public String getArea2() {
        return Area2;
    }

    public void setArea2(String Area2) {
        this.Area2 = Area2;
    }

    public String getDate1() {
        return Date1;
    }

    public void setDate1(String Date1) {
        this.Date1 = Date1;
    }

    public String getDate2() {
        return Date2;
    }

    public void setDate2(String Date2) {
        this.Date2 = Date2;
    }

    public String getDate3() {
        return Date3;
    }

    public void setDate3(String Date3) {
        this.Date3 = Date3;
    }

    public String getDate4() {
        return Date4;
    }

    public void setDate4(String Date4) {
        this.Date4 = Date4;
    }

    public String getPic1() {
        return Pic1;
    }

    public void setPic1(String Pic1) {
        this.Pic1 = Pic1;
    }

    public String getPic2() {
        return Pic2;
    }

    public void setPic2(String Pic2) {
        this.Pic2 = Pic2;
    }

    public String getIspic() {
        return ispic;
    }

    public void setIspic(String ispic) {
        this.ispic = ispic;
    }

    public String getLogopic() {
        return logopic;
    }

    public void setLogopic(String logopic) {
        this.logopic = logopic;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    //

    public String getDate(){
        return Date4.substring(0, 10);
    }


}
