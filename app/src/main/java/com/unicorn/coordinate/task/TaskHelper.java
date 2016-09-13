package com.unicorn.coordinate.task;

import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unicorn.coordinate.SimpleApplication;
import com.unicorn.coordinate.helper.Constant;
import com.unicorn.coordinate.helper.ResponseHelper;
import com.unicorn.coordinate.task.model.Point;
import com.unicorn.coordinate.task.model.PointDao;
import com.unicorn.coordinate.utils.ConfigUtils;
import com.unicorn.coordinate.volley.SimpleVolley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;


public class TaskHelper {

    public static boolean notInit() {
        return SimpleApplication.getInstance().getPointDao().queryBuilder().count() == 0;
    }

    public static void getPoints(String linesid) {
        String url = getUrl(linesid);
        Request request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            copeResponse(response);
                        } catch (Exception e) {
                            //
                        }
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        );
        SimpleVolley.addRequest(request);
    }

    private static void copeResponse(String responseString) throws Exception {
        if (ResponseHelper.isWrong(responseString)) {
            return;
        }
        JSONObject response = new JSONObject(responseString);
        JSONArray data = response.getJSONArray(Constant.K_DATA);
        List<Point> pointList = new Gson().fromJson(data.toString(), new TypeToken<List<Point>>() {
        }.getType());
        PointDao pointDao = SimpleApplication.getInstance().getPointDao();
        pointDao.insertInTx(pointList);
    }

    private static String getUrl(final String linesid) {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/getpoints?").buildUpon();
        builder.appendQueryParameter("linesid", linesid);
        return builder.toString();
    }

    //

    public static List<Point> getPointList() {
        PointDao pointDao = SimpleApplication.getInstance().getPointDao();
        return pointDao.queryBuilder()
                .list();
    }

}
