package com.unicorn.coordinate.helper;

public class PayStatusHelper {

    public static String payStatusText(String payStatus) {
        switch (payStatus) {
            case "0":
                return "支付订单成功锁定";
            case "1":
                return "比赛不是支付状态，不能支付";
            case "2":
                return "正在支付";
            case "3":
                return "已经成功支付";
            case "4":
                return "报名数量已经完成";
            case "5":
                return "报名队列已满";
            default:
                return "";
        }
    }

}
