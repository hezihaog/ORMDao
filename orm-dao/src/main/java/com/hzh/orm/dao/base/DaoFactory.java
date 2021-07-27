package com.hzh.orm.dao.base;

import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.hzh.orm.dao.DaoManager;

/**
 * Package: com.hzh.orm.dao
 * FileName: DaoFactory
 * Date: on 2017/12/3  上午11:16
 * Auther: zihe
 * Descirbe: Dao生成工厂
 * Email: hezihao@linghit.com
 */

public class DaoFactory {
    //数据库存放的路径
    protected static String mDbPath;
    //数据库对象
    protected static SQLiteDatabase mDatabase;

    static {
        boolean isInit = DaoManager.isInitSuccess();
        if (isInit) {
            String dbPath = DaoManager.getInstance().getDbPath();
            if (TextUtils.isEmpty(dbPath)) {
                throw new RuntimeException("请先调用DaoManager的init方法初始化数据库路径");
            }
            mDbPath = dbPath;
            mDatabase = SQLiteDatabase.openOrCreateDatabase(mDbPath, null);
        }
    }

    /**
     * 创建Dao实例
     *
     * @param clazz  Dao类Class
     * @param entity Dao类对应的Model层类Class
     * @return 要构造的Dao实例
     */
    public static <D extends BaseDao<M>, M> D createDao(Class<D> clazz, Class<M> entity) {
        BaseDao<M> dao = null;
        try {
            dao = (BaseDao<M>) clazz.newInstance();
            dao.init(mDatabase, entity);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (D) dao;
    }
}