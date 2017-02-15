package com.unicorn.coordinate.home;

import com.unicorn.coordinate.home.model.MatchInfo;
import com.unicorn.coordinate.home.model.MyMatchStatus;

public class MatchHelper {

    //    0 报名未开始  不能点击
//    1 立即预报名  可以点击(链接到开始报名页面)
//    2 预报名结束  可以点击(链接后续再改)
//    3 报名付费    可以点击(链接后续再改)
//    4 8 9  报名结束 不能点击
//    5 比赛结束 不能点击

    /*
    0 已创建不能预报名
    1 预报名开始可以开始预报名
    2 预报名结束不能预报名
    3 正式报名开始可以支付
    4 比赛开始不能预报名不能报名
    5 比赛结束不能预报名不能报名
    9 正式报名结束只有此状态下可以替换队员更换队长
    8 比赛开始前准备(锁定信息，不能更换队员)
     */

    /*
    0 已创建
    显示“报名未开始” 不可点击

    1 预报名开始可以开始预报名
    显示：立即预报名，后续根据status操作，现在是对的

    2 预报名结束不能预报名
    显示：预报名已结束
            status=6的可以点击进去，到预报名完成界面，付费按钮不显示

    3 正式报名开始可以支付
            status=6 显示 ”立即支付“ 按钮可以点击进入
            status=7 显示 “已完成报名”，可以点击进入现在的完成页面(付费页面不显示按钮)，下方付费按钮不显示

    9 正式报名结束只有此状态下可以替换队员更换队长
            status=7的可以点击进去现在完成页面(付费页面不显示按钮)，下面的立即支付按钮不显示

    4 比赛开始不能预报名不能报名
    显示比赛中，不判断status

    5 比赛结束不能预报名不能报名
    显示比赛结束，不判断status

    8 比赛开始前准备(锁定信息，不能更换队员)
    显示比赛中，不判断status

    */
    public static String matchStatusText(MatchInfo matchInfo) {
        switch (matchInfo.getStatus()) {
            case "0":
                return "报名未开始";
            case "1":
                return "预报名开始";
            case "2":
                return "预报名结束";
            case "3":
                return "正式报名开始";
            case "9":
                return "正式报名结束";
            case "4":
                return "比赛开始";
            case "5":
                return "比赛结束";
            case "8":
                return "赛前准备";
            default:
                return "等待比赛";
        }
    }

   static public String signUpText(MyMatchStatus myMatchStatus) {
        String matchStatus = myMatchStatus.getMacthStatus();
        String status = myMatchStatus.getStatus();
        switch (matchStatus) {
            case "0":
                return "报名未开始";
            case "1":
                return "立即预报名";
            case "2":
                return "预报名结束";
            case "3":
                if (status.equals("6")) {
                    return "立即支付";
                }
                if (status.equals("7")) {
                    return "已完成报名";
                }
            case "9":
                return "正式报名结束";
            case "4":
                return "比赛中";
            case "5":
                return "比赛结束";
            case "8":
                return "比赛中";
            default:
                return "等待比赛";
        }

    }

    public static String myMatchStatusText(MyMatchStatus myMatchStatus) {
        switch (myMatchStatus.getStatus()) {
            case "1":
                return "未报名参赛";
            case "2":
                return "已设定队名";
            case "3":
                return "已选择线路";
            case "4":
                return "被邀请，未操作";
            case "5":
                return "预报名完成";
            case "6":
                return "正式报名";
            case "7":
                return "正式报名完成";

            default:
                return "";
        }
    }

}
