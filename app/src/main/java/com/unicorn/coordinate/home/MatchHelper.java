package com.unicorn.coordinate.home;

import com.unicorn.coordinate.home.model.MatchInfo;

import java.util.Arrays;

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

    public static String matchStatusText(MatchInfo matchInfo) {
        switch (matchInfo.getStatus()) {
            case "0":
                return "报名未开始";
            case "1":
                return "立即预报名";
            case "2":
                return "预报名结束";
            case "3":
                return "报名付费";
            case "4":
            case "8":
            case "9":
                return "报名结束";
            case "5":
                return "比赛结束";
            default:
                return "";
        }
    }


    public static boolean isNeedGetMyMatchStatus(MatchInfo matchInfo) {
        String matchStatus = matchInfo.getStatus();
        return Arrays.asList("1", "3").contains(matchStatus);
    }

}
