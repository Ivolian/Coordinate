package com.unicorn.coordinate;

import com.unicorn.coordinate.task.model.Point;
import com.unicorn.coordinate.task.model.Task;
import com.unicorn.coordinate.task.model.TaskDao;
import com.unicorn.coordinate.utils.ConfigUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

import okhttp3.Call;

public class LocalHelper {

    public File getLocalSketcvoice(Point point) {
        return new File(ConfigUtils.getBaseDirPath(), fileName(point.getSketchvoice()));
    }

    public File getLocalSketcmap(Point point) {
        return new File(ConfigUtils.getBaseDirPath(), fileName(point.getSketchmap()));
    }


    public void localPoint(Point point) {
        downloadFile(point.getSketchmap());
        downloadFile(point.getSketchvoice());
    }

    //

    public File getLocalTaskLogo() {
        return new File(ConfigUtils.getBaseDirPath(), fileName(getTask().getTasklogo()));
    }

    public File getLocalLogoPic() {
        return new File(ConfigUtils.getBaseDirPath(), fileName(getTask().getLogopic()));
    }

    private Task getTask() {
        TaskDao taskDao = SimpleApplication.getInstance().getTaskDao();
        return taskDao.queryBuilder().unique();
    }

    public void localTask(Task task) {
        downloadFile(task.getTasklogo());
        downloadFile(task.getLogopic());
    }

    private void downloadFile(String url) {
        if (url == null || url.equals("")) {
            return;
        }
        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new FileCallBack(ConfigUtils.getBaseDirPath(), fileName(url)) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
//                        ToastUtils.show("error");
                    }

                    @Override
                    public void onResponse(File response, int id) {
//                        ToastUtils.show("ok");
                    }
                });
    }

    private String fileName(String url) {
        int index = url.lastIndexOf("/");
        String fileName = url.substring(index + 1, url.length());
        return fileName;
    }

}
