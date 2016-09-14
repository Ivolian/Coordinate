package com.unicorn.coordinate.task;

import com.unicorn.coordinate.SimpleApplication;
import com.unicorn.coordinate.task.model.Point;
import com.unicorn.coordinate.task.model.PointDao;


public class PointHelper {

    public static void deleteAll() {
        PointDao pointDao = SimpleApplication.getInstance().getPointDao();
        pointDao.deleteAll();
    }

    public static Point getLastPoint() {
        PointDao pointDao = SimpleApplication.getInstance().getPointDao();
        return pointDao.queryBuilder()
                .where(PointDao.Properties.Pointtime.isNotNull())
                .orderDesc(PointDao.Properties.Sort)
                .unique();
    }


}
