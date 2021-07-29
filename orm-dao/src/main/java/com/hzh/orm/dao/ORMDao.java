package com.hzh.orm.dao;

import android.content.Context;

import com.hzh.orm.dao.core.CRUDDao;
import com.hzh.orm.dao.core.IDao;

import java.io.File;

/**
 * Package: com.hzh.orm.dao
 * FileName: DaoManager
 * Date: on 2017/12/3  上午11:17
 * Auther: zihe
 * Descirbe: ORM Dao
 */
public class ORMDao {
    /**
     * 默认数据库文件名称
     */
    private static final String DEFAULT_DB_NAME = "orm_dao.db";

    /**
     * 数据库路径
     */
    private final String dbPath;

    /**
     * 唯一实例
     */
    private static volatile ORMDao sInstance;

    private ORMDao(Builder builder) {
        this.dbPath = builder.dbPath;
    }

    public String getDbPath() {
        return dbPath;
    }

    public static class Builder {
        private String dbPath;

        public Builder setDbPath(String dbPath) {
            this.dbPath = dbPath;
            return this;
        }

        public ORMDao build() {
            return new ORMDao(this);
        }

        public void installDefault() {
            if (sInstance != null) {
                throw new RuntimeException("默认实例已经存在了，请确保调用installDefault()之前，没有调用过getDefault()");
            }
            sInstance = build();
        }
    }

    public static ORMDao getDefault(Context context) {
        context = context.getApplicationContext();
        if (sInstance == null) {
            synchronized (ORMDao.class) {
                if (sInstance == null) {
                    sInstance = new Builder()
                            .setDbPath(new File(context.getFilesDir(), DEFAULT_DB_NAME).getAbsolutePath())
                            .build();
                }
            }
        }
        return sInstance;
    }

    /**
     * 获取Dao实例
     *
     * @param clazz  Dao类Class
     * @param entity Dao类对应的Model层类Class
     * @return 要构造的Dao实例
     */
    public <D extends IDao<M>, M> D getDao(Class<D> clazz, Class<M> entity) {
        IDao<M> dao = null;
        try {
            dao = (CRUDDao<M>) clazz.newInstance();
            boolean result = dao.initialize(this, entity);
            if (!result) {
                throw new RuntimeException("Dao初始化失败");
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (D) dao;
    }
}