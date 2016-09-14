package com.unicorn.coordinate.task;

import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unicorn.coordinate.helper.Constant;
import com.unicorn.coordinate.helper.ResponseHelper;
import com.unicorn.coordinate.task.model.Point;
import com.unicorn.coordinate.task.model.PointDao;
import com.unicorn.coordinate.task.model.Task;
import com.unicorn.coordinate.utils.ConfigUtils;
import com.unicorn.coordinate.volley.SimpleVolley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;


public class TaskHelper {

    // 启动或登录成功后调用
    public static void getTask() {
        if (ConfigUtils.notLogin()) {
            return;
        }
        String url = getTaskUrl();
        Request request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            copeTaskResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        );
        SimpleVolley.addRequest(request);
    }

    private static void copeTaskResponse(String responseString) throws Exception {
        if (ResponseHelper.isWrong(responseString)) {
            return;
        }
        JSONObject response = new JSONObject(responseString);
        JSONArray data = response.getJSONArray(Constant.K_DATA);
        List<Task> taskList = new Gson().fromJson(data.toString(), new TypeToken<List<Task>>() {
        }.getType());
        // 目前只有一个 task
        Task task = taskList.get(0);
        copeTask(task);
    }

    private static void copeTask(Task task) {
        String isdown = task.getIsdown();
        switch (isdown) {
            // 无需下载
            case "0":
                // do nothing
                break;
            // 未下载
            case "1":
                getPoints(task);
                updateTaskStatus(task);
                break;
            // 已下载
            case "2":
                getPoints(task);
                break;
            // 上传数据
            case "3":
                // 上传用户扫描过的最后一个点
                uploadLastPoint();
                break;
            // 删除数据
            case "4":
                PointHelper.deleteAll();
                break;
        }
    }


    //

    public static void getPoints(final Task task) {
        String url = getPointUrl(task.getLines_id());
        Request request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            copePointResponse(response, task);
                        } catch (Exception e) {
                            //
                        }
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        );
        SimpleVolley.addRequest(request);
    }

    private static void copePointResponse(final String responseString, final Task task) throws Exception {
        if (ResponseHelper.isWrong(responseString)) {
            return;
        }
        JSONObject response = new JSONObject(responseString);
        JSONArray data = response.getJSONArray(Constant.K_DATA);
        List<Point> pointList = new Gson().fromJson(data.toString(), new TypeToken<List<Point>>() {
        }.getType());
        for (Point point : pointList) {
            point.setLineid(task.getLines_id());
            point.setMatchuserid(task.getMatchuserid());
        }
        PointDao pointDao = PointHelper.getPointDao();
        pointDao.deleteAll();
        pointDao.insertOrReplaceInTx(pointList);
    }


    //

    public static void updateTaskStatus(final Task task) {
        String url = getSetTaskUrl(task);
        Request request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            ResponseHelper.isRight(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        );
        SimpleVolley.addRequest(request);
    }


    //

    public static void uploadLastPoint() {
        Point lastPoint = PointHelper.getLastPoint();
        if (lastPoint != null) {
            uploadPoint(lastPoint);
        }
    }

    public static void uploadPoint(Point point) {
        String url = getUploadPointUrl(point);
        Request request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            ResponseHelper.isRight(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        );
        SimpleVolley.addRequest(request);
    }


    // ======================== 底层方法 =========================

    private static String getTaskUrl() {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/gettask?").buildUpon();
        builder.appendQueryParameter(Constant.K_USER_ID, ConfigUtils.getUserId());
        return builder.toString();
    }

    private static String getPointUrl(final String linesid) {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/getpoints?").buildUpon();
        builder.appendQueryParameter("linesid", linesid);
        return builder.toString();
    }

    private static String getSetTaskUrl(final Task task) {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/settask?").buildUpon();
        builder.appendQueryParameter("matchuserid", task.getMatchuserid());
        return builder.toString();
    }

    private static String getUploadPointUrl(final Point point) {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/upoadtask?").buildUpon();
        builder.appendQueryParameter("matchuserid", point.getMatchuserid());
        builder.appendQueryParameter("pointid", point.getPointid());
        builder.appendQueryParameter("pointtime", point.getPointtime());
        return builder.toString();
    }


}
