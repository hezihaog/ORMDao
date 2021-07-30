package com.hzh.orm.dao.core;

import com.hzh.orm.dao.ORMDao;

import java.util.List;

/**
 * Package: com.hzh.orm.dao.inter
 * FileName: IBaseDao
 * Date: on 2017/12/3  上午11:36
 * Auther: zihe
 * Descirbe: 数据库操作接口
 */
public interface IDao<M> {
    /**
     * 初始化
     */
    boolean initialize(ORMDao ormDao, Class<M> entity);

    /**
     * 插入一条记录
     *
     * @param entity 表对应的bean对象
     * @return 插入的新行的id
     */
    Long insert(M entity);

    /**
     * 删除一条记录
     *
     * @param where 条件，同样是表对应的bean对象
     */
    Integer delete(M where);

    /**
     * 删除所有记录
     */
    Integer deleteAll();

    /**
     * 更新一条记录
     *
     * @param entity 表对应的bean对象
     * @param where  条件
     */
    Integer update(M entity, M where);

    /**
     * 查询
     *
     * @param where 条件
     * @return 结果集合
     */
    List<M> query(M where);

    /**
     * 查询，可设置排序
     *
     * @param where   条件
     * @param orderBy 排序条件
     * @return 结果集合
     */
    List<M> query(M where, String orderBy);

    /**
     * 查询，可设置排序，分页
     *
     * @param where     条件
     * @param orderBy   排序条件
     * @param page      页码，从1开始，数据库是从0开始的，所以该方法会自减1
     * @param pageCount 页数量
     * @return 结果集合
     */
    List<M> query(M where, String orderBy, Integer page, Integer pageCount);
}