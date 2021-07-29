package com.hzh.orm.dao.sample;

import android.app.Application;

import com.hzh.orm.dao.ORMDao;

import java.io.File;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //自定义数据库位置
        new ORMDao.Builder()
                .setDbPath(new File(getFilesDir(), "app.db").getAbsolutePath())
                .installDefault();
    }
}