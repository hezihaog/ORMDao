package com.hzh.orm.dao;

import android.database.sqlite.SQLiteDatabase;

import com.hzh.orm.dao.core.CRUDDao;

/**
 * Package: com.hzh.orm.dao
 * FileName: DaoManager
 * Date: on 2017/12/3  上午11:17
 * Auther: zihe
 * Descirbe: 数据库管理器
 * Email: hezihao@linghit.com
 */

public class DaoManager {
    //数据库存放的路径
    private static String mDbPath;
    private static boolean isInitSuccess = false;

    private DaoManager() {
    }

    /**
     * 初始化
     *
     * @param dbPath 数据库存储地址
     */
    public static void initialize(String dbPath) {
        mDbPath = dbPath;
        isInitSuccess = true;
    }

    /**
     * 创建Dao实例
     *
     * @param clazz  Dao类Class
     * @param entity Dao类对应的Model层类Class
     * @return 要构造的Dao实例
     */
    public static <D extends CRUDDao<M>, M> D createDao(Class<D> clazz, Class<M> entity) {
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(mDbPath, null);
        CRUDDao<M> dao = null;
        try {
            dao = (CRUDDao<M>) clazz.newInstance();
            boolean result = dao.initialize(database, entity);
            if (!result){
                throw new RuntimeException("Dao初始化失败");
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (D) dao;
    }

    /**
     * 获取设置的数据库存储路径
     *
     * @return 设置的数据库存储路径
     */
    public static String getDbPath() {
        return mDbPath;
    }

    /**
     * 是否已经初始化
     *
     * @return true为已经初始化，否则没有进行初始化
     */
    public static boolean isInitSuccess() {
        return isInitSuccess;
    }
}
