package com.unicorn.coordinate.utils;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.unicorn.coordinate.helper.Constant;
import com.unicorn.coordinate.volley.SimpleVolley;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.File;

import cz.msebera.android.httpclient.Header;


public class UpdateUtils {

    private static Activity currentActivity;

    private static void init(Activity activity) {
        currentActivity = activity;
    }

    private static void clear() {
        currentActivity = null;
    }

    //

    public static void checkUpdate(Activity activity) {
        init(activity);
        String url = getUrl();
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

    private static void copeResponse(String responseString) throws Exception {
        // 如果 isRight，则不需更新
        JSONObject response = new JSONObject(responseString);
        String code = response.getString(Constant.K_CODE);
        boolean success = (code != null && code.equals(Constant.RESPONSE_SUCCESS_CODE));
        if (success) {
            return;
        }
        String apkUrl = response.getString(Constant.K_MSG);
        apkUrl = "http://" + apkUrl;
        showConfirmDialog(apkUrl);
    }

    private static void showConfirmDialog(final String apkUrl) {
        new MaterialDialog.Builder(currentActivity)
                .content("检测到新版本，请更新")
                .positiveText("确认")
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        downloadApk(apkUrl);
                    }
                }).show();
    }

    private static void downloadApk(final String apkUrl) {
        final MaterialDialog mask = DialogUtils.showMask2(currentActivity, "下载 APK 中", "请稍后");
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(apkUrl, new FileAsyncHttpResponseHandler(currentActivity) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, File response) {
                mask.dismiss();
                installApk(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                mask.dismiss();
                ToastUtils.show("下载失败");
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                mask.setProgress((int) (bytesWritten * 100 / totalSize));
            }
        });
    }

    private static void installApk(File response) {
        String apkPath = ConfigUtils.getBaseDirPath() + "/coordinate.apk";
        File apk = new File(apkPath);
        if (apk.exists()) {
            apk.delete();
        }
        try {
            FileUtils.copyFile(response, apk);
        } catch (Exception e) {
            //
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(apk), "application/vnd.android.package-archive");
        currentActivity.startActivity(intent);

        clear();
    }

    private static String getUrl() {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/checkversion?").buildUpon();
        builder.appendQueryParameter("ver", AppUtils.getVersionName());
        return builder.toString();
    }

}
