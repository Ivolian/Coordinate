package com.unicorn.coordinate.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.PayTask;
import com.f2prateek.dart.InjectExtra;
import com.unicorn.coordinate.R;
import com.unicorn.coordinate.base.BaseActivity;
import com.unicorn.coordinate.helper.ClickHelper;
import com.unicorn.coordinate.helper.Constant;
import com.unicorn.coordinate.home.event.PaySuccessEvent;
import com.unicorn.coordinate.home.model.MyOrder;
import com.unicorn.coordinate.pay.AuthResult;
import com.unicorn.coordinate.pay.OrderInfoUtil2_0;
import com.unicorn.coordinate.pay.PayResult;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import butterknife.OnClick;

public class PayActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
    }

    @InjectExtra(Constant.K_MY_ORDER)
    MyOrder myOrder;

    @OnClick(R.id.back)
    public void backOnClick() {
        if (ClickHelper.isSafe()) {
            finish();
        }
    }

    @OnClick(R.id.pay)
    public void payOnClick() {
        if (ClickHelper.isSafe()) {
            pay();
        }
    }



    private void pay() {
        payV2();
    }

    /**
     * 支付宝支付业务：入参app_id
     */
    public static final String APPID = "2017010604887216";

    /**
     * 支付宝账户登录授权业务：入参pid值
     */
    public static final String PID = "";
    /**
     * 支付宝账户登录授权业务：入参target_id值
     */
    public static final String TARGET_ID = "";

    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /**
     * 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1
     */
    public static final String RSA2_PRIVATE = "MIIEowIBAAKCAQEAon7DiJVGs040fQrHD89MrFmUGHsa22/1S1VOU6bWg61TVEHkCnzJjG8n8KGGSW7DzeEeCRBei++284OwSLHpKDL8oSzGTy7cmJ9D82ggzQkXgCnathi6Fe1uABa1JLTu6yVGAKdxMuQmiy9sC8X7NNmwAZn09zKs2iZzjlDNVyh6fAddaKp26htHz8ON2ZPD6qyrudqwvmSBJAjYyo8cqenywHDsq2EPG9OvC5sccpOLAd3iO3YLHojJRWzfM0m3Hlr2SsEH4lCn7SbJT6NHBXA4XzJh1dcSy40Dr3yS6L1SafgzGQnYGB9rSLZF0TPl+BsJtnfUjjRX9xoodMkg+wIDAQABAoIBAGN7jkSBd55xA6uGPKbOOFfQI931CFVOLvPnDyQhoXVOWRTdnjpZeZ9TsjUc/rTYbmIOL7BG6EtuACkH95YZ7e/hf09BtUtnGkEKSLStjF9a8JtThIY1jz+7c7v0KwSFDTzDPh9KZABPM9XEu7ZdiFUr7Rid5B6vVKneHqMDjyOLN+UmFpIVfIncI7gOw+EYxgvnAZn4uxN7EDYJZHh+tFcY+/Svh5EMd/yTGTaOv/EEF9x8z/rhAa9LQErKtz6CAbk5m21siklmDnwpNk/BuFFdqpBV/FKBHA8qBMgJBoZgOHea5t+r1dqxuIjc9vP+mGDiP2q6f0ZyON+1y1jobNkCgYEAzzBK3bI/V0+2XlPCTvRZuYUHEP0lDCoC9tnSoVz/Q+xSLN+ItQ2HhRrCYrW9TXXIhSzEc0zuE++JPX3axWCscl/BwQy9l33Vl8U1J5QozO6NKakBPgAwwHIauDCIVfgcJiNk1FoyVtGIDeoTWAAXXNniFNyfqcN0iKb9WC6z7kcCgYEAyMb5NcGpCoOEJY3uPszzm09xXXQn91t5OEG3NJbklELgfwbKa1UP6ZafhkwHM4ZNg93jIH5LPws8khOk2KBmLclUAjLxlQqzP938BBlO33d6yIpN332+k4eVHo79OxZLak6OH1PEpCdmeg77B8qoS6PZY6I24+gE7szwwBNsja0CgYEApY2SFOnMISkpsN++wLYRR+NbDSEpzDl5muPcoWGc+xpHY2HOa9V+t0X1k0hjFhUKgBeQdg0/o6BgikkZWWPxB4cqW+ahS/6ZgKds3M5Pd8kZ9PL9chxW2D+UyB7HoeIaJANeGyxjUuoki/HBOS0Bfgj/1OmhjtYGNPcyMpdG4RsCgYBcaNhDGcOIB0hHULAbT5HAJixzawcKHpwIfJaqKdlDNBTH4oRAKoKMbLs3wL5/xIzg6LulNPvFJJLYWRpCanEdGdEEwhXXF3mxJ6ba7BsVV5S3wJBfES0qEflce0uqanSDlWxsls2nEEtmWobH60rSpbNk3ECPXTcIAmO8xDPucQKBgF8THn47QUqqPG3by76Zw5WeslmkJxGjqN6OcQ5i68sYh8e13i+kIIxxl0BjPCi5WuC4aqBOdjPMNc4Z3Ra2D6zsCyy1XLxVXIA2SumOvm5gtjTriFvn20rj895/IlOB++9ZslgMzp/J+kwS9MjUR0t+RPHepUHGmK/j86AReVF2";
    public static final String RSA_PRIVATE = "";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

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
                        EventBus.getDefault().post(new PaySuccessEvent());
                        finish();

                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(PayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(PayActivity.this,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(PayActivity.this,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };


    /**
     * 支付宝支付业务
     *
     * @param v
     */
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



}
