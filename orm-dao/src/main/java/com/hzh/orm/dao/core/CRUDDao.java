package com.hzh.orm.dao.core;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.List;
import java.util.Map;

/**
 * Package: com.hzh.orm.dao.base
 * FileName: BaseDao
 * Date: on 2017/12/3  上午11:42
 * Auther: zihe
 * Descirbe:
 */
public class CRUDDao<M> extends BaseDao<M> {
    @Override
    public Long insert(M entity) {
        try {
            Map<String, String> values = getValues(entity);
            ContentValues contentValues = getContentValues(values);
            return mDatabase.insert(mTbName, null, contentValues);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return -1L;
    }

    @Override
    public Integer delete(M where) {
        try {
            Map<String, String> whereMap = getValues(where);
            Condition condition = new Condition(whereMap);
            return mDatabase.delete(mTbName, condition.whereClause, condition.whereArgs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Integer update(M entity, M where) {
        try {
            Map<String, String> values = getValues(entity);
            ContentValues contentValues = getContentValues(values);
            Map<String, String> whereMap = getValues(where);
            Condition condition = new Condition(whereMap);
            return mDatabase.update(mTbName, contentValues, condition.whereClause
                    , condition.whereArgs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public List<M> query(M where) {
        return query(where, null);
    }

    @Override
    public List<M> query(M where, String orderBy) {
        return query(where, orderBy, null, null);
    }

    @Override
    public List<M> query(M where, String orderBy, Integer page, Integer pageCount) {
        List<M> list = null;
        Cursor cursor = null;
        try {
            String limit = null;
            if (page != null && pageCount != null) {
                int startIndex = --page;
                limit = (Math.max(startIndex, 0)) + "," + pageCount;
            }
            if (where != null) {
                Map<String, String> whereMap = getValues(where);
                Condition condition = new Condition(whereMap);
                cursor = mDatabase.query(mTbName, null, condition.whereClause, condition.whereArgs, null, null, orderBy, limit);
            } else {
                cursor = mDatabase.query(mTbName, null, null, null, null, null, orderBy, limit);
            }
            list = getDataList(cursor);
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return list;
    }
}