package com.unicorn.coordinate.task;

import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.unicorn.coordinate.SimpleApplication;
import com.unicorn.coordinate.helper.Constant;
import com.unicorn.coordinate.helper.ResponseHelper;
import com.unicorn.coordinate.helper.TinyDB;
import com.unicorn.coordinate.task.event.StopRefreshingEvent;
import com.unicorn.coordinate.task.event.RefreshTaskEvent;
import com.unicorn.coordinate.task.model.Point;
import com.unicorn.coordinate.task.model.PointDao;
import com.unicorn.coordinate.utils.AESUtils;
import com.unicorn.coordinate.utils.ConfigUtils;
import com.unicorn.coordinate.utils.MD5Util;
import com.unicorn.coordinate.utils.ToastUtils;
import com.unicorn.coordinate.volley.SimpleVolley;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.DateTime;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PointHelper {

    private static final String LAST_POINT_MD5 = MD5Util.getMD5String("csdxsuccessdx");

    public static void copeScanResult(String scanResult) throws Exception {
        if (isLine(scanResult)) {
            copeLine(scanResult);
        } else {
            copePoint(scanResult);
        }
    }


    // =================== line ===================

    private static boolean isLine(String scanResult) {
        return scanResult.startsWith("{");
    }

    private static void copeLine(String scanResult) throws Exception {
        JSONObject jsonObject = new JSONObject(scanResult);
        String linesid = jsonObject.getString("linesid");
        copeLinesid(linesid);
    }

    public static void copeLinesid(String linesid) {
        Point startPoint = getStartPoint();
        if (startPoint == null) {
            ToastUtils.show("找不到起点");
            return;
        }
        if (!linesid.equals(startPoint.getLineid())) {
            ToastUtils.show("所扫描线路与本地线路不匹配");
            return;
        }
        setTimeAndUpload(startPoint);
    }


    // =================== point ===================

    private static void copePoint(String scanResult) {
        if (scanResult.equals(LAST_POINT_MD5)) {
            copeLastPoint();
            return;
        }
        copeGeneralPoint(scanResult);
    }

    private static void copeGeneralPoint(String scanResult) {
        Point nextPoint = getNextPoint();
        if (nextPoint == null) {
            ToastUtils.show("请扫码签到");
            return;
        }
        String pointiddx = nextPoint.getPointid() + "dx";
        String afterMd5 = MD5Util.getMD5String(pointiddx);
        if (afterMd5.equals(scanResult)) {
            setTimeAndUpload(nextPoint);
        } else {
            ToastUtils.show("请扫描" + AESUtils.decrypt(nextPoint.getPointname()) + "对应的任务二维码");
        }
    }

    private static void copeLastPoint() {
        Point nextPoint = getNextPoint();
        if (nextPoint == null) {
            ToastUtils.show("请扫码签到");
            return;
        }
        if (nextPoint.getPointtype() == PointType.LAST_POINT) {
            setTimeAndUpload(nextPoint);
        } else {
            ToastUtils.show("请扫描" + AESUtils.decrypt(nextPoint.getPointname()) + "对应的任务二维码");
        }
    }


    //

    public static List<Point> getDisplayPointList() {
        Point currentPoint = getCurrentPoint();
        if (currentPoint == null) {
            return new ArrayList<>();
        }
        Point nextPoint = getNextPoint();
        if (nextPoint == null) {
            nextPoint = currentPoint;
        }
        PointDao pointDao = getPointDao();
        return pointDao.queryBuilder()
                .where(PointDao.Properties.Sort.le(nextPoint.getSort()))
                .list();
    }


    //


    private static void setTimeAndUpload(Point point) {
        setPointTime(point);
        uploadPoint(point);
    }

    private static void setPointTime(Point point) {
        DateTime dateTime = new DateTime(new Date());
        String pointtime = dateTime.toString("yyyy-MM-dd HH:mm:ss");
        point.setPointtime(pointtime);
    }

    public static Point getNextPoint() {
        Point currentPoint = getCurrentPoint();
        if (currentPoint == null) {
            return null;
        }
        if (currentPoint.getPointtype() == PointType.LAST_POINT) {
            return currentPoint;
        }
        return findPointBySort(currentPoint.getSort() + 1);
    }


    // =================== uploadPoint ===================

    public static void uploadPoint(final Point point) {
        String url = getUploadPointUrl(point);
        Request request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            copeUploadPointResponse(response, point);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        );
        SimpleVolley.addRequest(request);
    }

    private static void copeUploadPointResponse(String response, Point point) throws Exception {
        if (ResponseHelper.isWrong(response)) {
            return;
        }
        PointHelper.saveLastPoint(point);
        PointHelper.saveCurrentPoint(point);
        ToastUtils.show(point.getPointtype() == PointType.LAST_POINT ? "已完成所有进度" : "扫码成功");
        EventBus.getDefault().post(new RefreshTaskEvent());
    }

    private static String getUploadPointUrl(final Point point) {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/uploadtask?").buildUpon();
        builder.appendQueryParameter("matchuserid", point.getMatchuserid());
        builder.appendQueryParameter("pointid", point.getPointid());
        builder.appendQueryParameter("pointtime", point.getPointtime());
        return builder.toString();
    }


    // =================== syncTeam ===================

    public static void syncTeam() {
        String url = getTeamPointUrl();
        Request request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        EventBus.getDefault().post(new StopRefreshingEvent());
                        try {
                            copeSyncTeamResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        );
        SimpleVolley.addRequest(request);
    }

    private static void copeSyncTeamResponse(String responseString) throws Exception {
        if (ResponseHelper.isWrong(responseString)) {
            return;
        }
        JSONObject response = new JSONObject(responseString);
        String teamPointId = response.getString(Constant.K_DATA);
        Point teamPoint = findPointByPointId(teamPointId);
        PointHelper.saveCurrentPoint(teamPoint);
        ToastUtils.show("已同步队伍进度");
        EventBus.getDefault().post(new RefreshTaskEvent());
    }

    private static String getTeamPointUrl() {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/getteamrecord?").buildUpon();
        builder.appendQueryParameter("matchuserid", getStartPoint().getMatchuserid());
        return builder.toString();
    }


    // =================== 本地保存 ===================

    public static void saveLastPoint(Point point) {
        TinyDB tinyDB = TinyDB.getNewInstance();
        tinyDB.putObject(Constant.K_LAST_POINT, point);
    }

    public static Point getLastPoint() {
        TinyDB tinyDB = TinyDB.getNewInstance();
        return (Point) tinyDB.getObject(Constant.K_LAST_POINT, Point.class);
    }

    public static void saveCurrentPoint(Point point) {
        TinyDB tinyDB = TinyDB.getNewInstance();
        tinyDB.putObject(Constant.K_CURRENT_POINT, point);
    }

    public static Point getCurrentPoint() {
        TinyDB tinyDB = TinyDB.getNewInstance();
        return (Point) tinyDB.getObject(Constant.K_CURRENT_POINT, Point.class);
    }

    public static void clearPointInfo() {
        TinyDB tinyDB = TinyDB.getNewInstance();
        tinyDB.remove(Constant.K_LAST_POINT);
        tinyDB.remove(Constant.K_CURRENT_POINT);
    }


    // =================== 底层函数 ===================

    private static Point findPointBySort(int sort) {
        return getPointDao().queryBuilder()
                .where(PointDao.Properties.Sort.eq(sort))
                .unique();
    }

    private static Point findPointByPointId(String pointId) {
        return getPointDao().queryBuilder()
                .where(PointDao.Properties.Pointid.eq(pointId))
                .unique();
    }

    public static Point getStartPoint() {
        return getPointDao().queryBuilder()
                .where(PointDao.Properties.Pointtype.eq(PointType.START_POINT))
                .unique();
    }

    public static void deleteAll() {
        getPointDao().deleteAll();
    }

    public static PointDao getPointDao() {
        return SimpleApplication.getInstance().getPointDao();
    }


}
