package com.unicorn.coordinate.task;

import com.unicorn.coordinate.SimpleApplication;
import com.unicorn.coordinate.helper.Constant;
import com.unicorn.coordinate.helper.TinyDB;
import com.unicorn.coordinate.task.model.Point;
import com.unicorn.coordinate.task.model.PointDao;

import java.util.List;


public class PointHelper {

    public static PointDao getPointDao() {
        return SimpleApplication.getInstance().getPointDao();
    }

    public static void deleteAll() {
        PointDao pointDao = SimpleApplication.getInstance().getPointDao();
        pointDao.deleteAll();
    }

    public static List<Point> getPointList() {
        PointDao pointDao = SimpleApplication.getInstance().getPointDao();
        return pointDao.queryBuilder().list();
    }

    //

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


}
