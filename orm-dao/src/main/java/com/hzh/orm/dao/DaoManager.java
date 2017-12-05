package com.hzh.orm.dao;

/**
 * Package: com.hzh.orm.dao
 * FileName: DaoManager
 * Date: on 2017/12/3  上午11:17
 * Auther: zihe
 * Descirbe: 数据库管理器
 * Email: hezihao@linghit.com
 */

public class DaoManager {
    private String mDbPath;
    private static boolean isInited = false;

    private DaoManager() {
    }

    private static final class SingletonHolder {
        private static final DaoManager instance = new DaoManager();
    }

    /**
     * 初始化
     *
     * @param dbPath 数据库存储地址
     * @return DaoManager实例
     */
    public static DaoManager init(String dbPath) {
        DaoManager instance = SingletonHolder.instance;
        instance.mDbPath = dbPath;
        isInited = true;
        return instance;
    }

    /**
     * 获取实例，必须先调用init方法进行初始化
     *
     * @return
     */
    public static DaoManager getInstance() {
        if (!isInited) {
            throw new RuntimeException("请先调用init方法进行初始化");
        }
        return SingletonHolder.instance;
    }

    /**
     * 获取设置的数据库存储路径
     *
     * @return 设置的数据库存储路径
     */
    public String getDbPath() {
        return mDbPath;
    }

    /**
     * 是否已经初始化
     *
     * @return true为已经初始化，否则没有进行初始化
     */
    public static boolean isInited() {
        return isInited;
    }
}
