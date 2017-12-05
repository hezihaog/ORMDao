package com.hzh.orm.dao.base;

import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.hzh.orm.dao.DaoManager;

/**
 * Package: com.hzh.orm.dao
 * FileName: DaoFactory
 * Date: on 2017/12/3  上午11:16
 * Auther: zihe
 * Descirbe: Dao基础生成工厂，可以生成BaseDao子类的工厂见{@link com.hzh.orm.dao.base.BaseDaoFactory}
 * Email: hezihao@linghit.com
 */

public abstract class DaoFactory {
    //数据库存放的路径
    protected String mDbPath;
    //数据库对象
    protected SQLiteDatabase mDatabase;

    protected DaoFactory() {
        boolean isInited = DaoManager.isInited();
        if (isInited) {
            String dbPath = DaoManager.getInstance().getDbPath();
            if (TextUtils.isEmpty(dbPath)) {
                throw new RuntimeException("请先调用DaoManager的init方法初始化数据库路径");
            }
            this.mDbPath = dbPath;
            mDatabase = SQLiteDatabase.openOrCreateDatabase(mDbPath, null);
        }
    }
}