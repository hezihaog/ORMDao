package com.hzh.orm.dao.base;


import java.util.HashMap;

/**
 * Package: com.hzh.orm.dao
 * FileName: BaseDaoFactory
 * Date: on 2017/12/4  下午1:32
 * Auther: zihe
 * Descirbe:
 * Email: hezihao@linghit.com
 */

public class BaseDaoFactory extends DaoFactory {
    private static final HashMap<String, BaseDaoFactory> factoryMap = new HashMap<String, BaseDaoFactory>();

    private BaseDaoFactory() {
        super();
    }

    private static final class SingleHolder {
        private static final BaseDaoFactory instance = new BaseDaoFactory();
    }

    public static BaseDaoFactory getInstance() {
        return SingleHolder.instance;
    }


    /**
     * 创建Dao实例
     *
     * @param clazz  Dao类Class
     * @param entity Dao类对应的Model层类Class
     * @return 要构造的Dao实例
     */
    public <D extends BaseDao<M>, M> D createDao(Class<D> clazz, Class<M> entity) {
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
