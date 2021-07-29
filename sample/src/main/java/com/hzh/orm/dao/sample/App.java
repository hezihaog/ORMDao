package com.hzh.orm.dao.sample;

import android.app.Application;

import com.hzh.orm.dao.ORMDao;
import com.hzh.orm.dao.core.DaoLogger;

import java.io.File;

public class App extends Application {
    private static final String TAG = App.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        //设置日志
        DaoLogger.setLogger(new DaoLogger.Log() {
            @Override
            public void info(String msg) {
                android.util.Log.i(TAG, msg);
            }

            @Override
            public void debug(String msg) {
                android.util.Log.d(TAG, msg);
            }

            @Override
            public void error(String msg) {
                android.util.Log.e(TAG, msg);
            }
        });
        //自定义数据库位置
        new ORMDao.Builder()
                .setDbPath(new File(getFilesDir(), "app.db").getAbsolutePath())
                .installDefault();
    }
}