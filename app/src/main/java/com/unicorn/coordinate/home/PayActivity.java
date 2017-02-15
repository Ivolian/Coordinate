package com.unicorn.coordinate.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.f2prateek.dart.InjectExtra;
import com.google.gson.Gson;
import com.unicorn.coordinate.R;
import com.unicorn.coordinate.base.BaseActivity;
import com.unicorn.coordinate.helper.ClickHelper;
import com.unicorn.coordinate.helper.Constant;
import com.unicorn.coordinate.helper.PayStatusHelper;
import com.unicorn.coordinate.helper.ResponseHelper;
import com.unicorn.coordinate.home.event.PaySuccessEvent;
import com.unicorn.coordinate.home.model.MyMatchStatus;
import com.unicorn.coordinate.home.model.MyOrder;
import com.unicorn.coordinate.home.model.MyPayResult;
import com.unicorn.coordinate.pay.OrderInfoUtil2_0;
import com.unicorn.coordinate.pay.PayResult;
import com.unicorn.coordinate.utils.ConfigUtils;
import com.unicorn.coordinate.utils.ToastUtils;
import com.unicorn.coordinate.volley.SimpleVolley;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class PayActivity extends BaseActivity {


    // ======================== payOnClick ============================

    @BindView(R.id.pay)
    TextView pay;

    @OnClick(R.id.pay)
    public void payOnClick() {
        if (ClickHelper.isSafe() && time == 0) {
            checkPay();
        }
    }

    private void checkPay() {
        String url = checkPayUrl();
        Request request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            copeResponseZ(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        );
        SimpleVolley.addRequest(request);
    }

    private void copeResponseZ(String responseString) throws Exception {
        if (ResponseHelper.isWrong(responseString)) {
            return;
        }
        JSONObject response = new JSONObject(responseString);
        JSONObject data = response.getJSONObject(Constant.K_DATA);
        String status = data.getString("status");
        if (status.equals("0")) {
            payV2();
        }else {
            ToastUtils.show(PayStatusHelper.payStatusText(status));
            startTiming();
        }

    }


    // ======================== timing =========================

    private final Handler handler = new Handler();

    private int time = 0;

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            pay.setText(time == 0 ? "确认支付" : "请稍后再试（" + time + "S）");
            if (time == 0) {
                return;
            }
            time--;
            handler.postDelayed(this, 1000);
        }
    };

    private void startTiming() {
        time = 10;
        handler.post(runnable);
    }



    private String checkPayUrl() {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/checkpay?").buildUpon();
        builder.appendQueryParameter("teamid", myMatchStatus.getTeamid());
        return builder.toString();
    }

    // ======================== payV2 ============================

    public static final String APPID = "2017010604887216";
    public static final String RSA2_PRIVATE = "MIIEowIBAAKCAQEAon7DiJVGs040fQrHD89MrFmUGHsa22/1S1VOU6bWg61TVEHkCnzJjG8n8KGGSW7DzeEeCRBei++284OwSLHpKDL8oSzGTy7cmJ9D82ggzQkXgCnathi6Fe1uABa1JLTu6yVGAKdxMuQmiy9sC8X7NNmwAZn09zKs2iZzjlDNVyh6fAddaKp26htHz8ON2ZPD6qyrudqwvmSBJAjYyo8cqenywHDsq2EPG9OvC5sccpOLAd3iO3YLHojJRWzfM0m3Hlr2SsEH4lCn7SbJT6NHBXA4XzJh1dcSy40Dr3yS6L1SafgzGQnYGB9rSLZF0TPl+BsJtnfUjjRX9xoodMkg+wIDAQABAoIBAGN7jkSBd55xA6uGPKbOOFfQI931CFVOLvPnDyQhoXVOWRTdnjpZeZ9TsjUc/rTYbmIOL7BG6EtuACkH95YZ7e/hf09BtUtnGkEKSLStjF9a8JtThIY1jz+7c7v0KwSFDTzDPh9KZABPM9XEu7ZdiFUr7Rid5B6vVKneHqMDjyOLN+UmFpIVfIncI7gOw+EYxgvnAZn4uxN7EDYJZHh+tFcY+/Svh5EMd/yTGTaOv/EEF9x8z/rhAa9LQErKtz6CAbk5m21siklmDnwpNk/BuFFdqpBV/FKBHA8qBMgJBoZgOHea5t+r1dqxuIjc9vP+mGDiP2q6f0ZyON+1y1jobNkCgYEAzzBK3bI/V0+2XlPCTvRZuYUHEP0lDCoC9tnSoVz/Q+xSLN+ItQ2HhRrCYrW9TXXIhSzEc0zuE++JPX3axWCscl/BwQy9l33Vl8U1J5QozO6NKakBPgAwwHIauDCIVfgcJiNk1FoyVtGIDeoTWAAXXNniFNyfqcN0iKb9WC6z7kcCgYEAyMb5NcGpCoOEJY3uPszzm09xXXQn91t5OEG3NJbklELgfwbKa1UP6ZafhkwHM4ZNg93jIH5LPws8khOk2KBmLclUAjLxlQqzP938BBlO33d6yIpN332+k4eVHo79OxZLak6OH1PEpCdmeg77B8qoS6PZY6I24+gE7szwwBNsja0CgYEApY2SFOnMISkpsN++wLYRR+NbDSEpzDl5muPcoWGc+xpHY2HOa9V+t0X1k0hjFhUKgBeQdg0/o6BgikkZWWPxB4cqW+ahS/6ZgKds3M5Pd8kZ9PL9chxW2D+UyB7HoeIaJANeGyxjUuoki/HBOS0Bfgj/1OmhjtYGNPcyMpdG4RsCgYBcaNhDGcOIB0hHULAbT5HAJixzawcKHpwIfJaqKdlDNBTH4oRAKoKMbLs3wL5/xIzg6LulNPvFJJLYWRpCanEdGdEEwhXXF3mxJ6ba7BsVV5S3wJBfES0qEflce0uqanSDlWxsls2nEEtmWobH60rSpbNk3ECPXTcIAmO8xDPucQKBgF8THn47QUqqPG3by76Zw5WeslmkJxGjqN6OcQ5i68sYh8e13i+kIIxxl0BjPCi5WuC4aqBOdjPMNc4Z3Ra2D6zsCyy1XLxVXIA2SumOvm5gtjTriFvn20rj895/IlOB++9ZslgMzp/J+kwS9MjUR0t+RPHepUHGmK/j86AReVF2";
    public static final String RSA_PRIVATE = "";
    private static final int SDK_PAY_FLAG = 1;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        feedback(payResult);
                    } else {
                        Toast.makeText(PayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };


    public void payV2() {
        if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            finish();
                        }
                    }).show();
            return;
        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2, myOrder.getOrdertotal(), myOrder.getTitle(), myOrder.getOrderid());
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(PayActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    // ======================== 反馈支付结果 ============================

    private void feedback(PayResult payResult) {
        MyPayResult myPayResult = new Gson().fromJson(payResult.getResult(), MyPayResult.class);
        String url = feedbackUrl(myPayResult.getAlipay_trade_app_pay_response().getTrade_no(),
                payResult.getResultStatus(), myPayResult.getAlipay_trade_app_pay_response().getSeller_id());
        Request request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            copeResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        );
        SimpleVolley.addRequest(request);
    }

    private void copeResponse(String responseString) throws Exception {
        if (ResponseHelper.isWrong(responseString)) {
            return;
        }
        EventBus.getDefault().post(new PaySuccessEvent());
        finish();
    }

    private String feedbackUrl(String trade_no, String trade_status, String buyer_email) {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/completepay?").buildUpon();
        builder.appendQueryParameter("orderid", myOrder.getOrderid());
        builder.appendQueryParameter("trade_no", trade_no);
        builder.appendQueryParameter("trade_status", trade_status);
        builder.appendQueryParameter("buyer_email", buyer_email);
        return builder.toString();

//        trade_no	2017021321001004010287445167
//        trade_status	9000
//        buyer_email	2088801855353392
//        return ConfigUtils.getBaseUrl() + "/api/completepay2?result=" + result;
    }


    // ======================== ignore ============================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
    }

    @InjectExtra(Constant.K_MY_ORDER)
    MyOrder myOrder;

    @InjectExtra(Constant.K_MY_MATCH_STATUS)
    MyMatchStatus myMatchStatus;

    @OnClick(R.id.back)
    public void backOnClick() {
        if (ClickHelper.isSafe()) {
            finish();
        }
    }


}
