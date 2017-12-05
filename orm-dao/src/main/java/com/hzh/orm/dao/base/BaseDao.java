package com.hzh.orm.dao.base;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.hzh.orm.dao.anno.TbField;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * Package: com.hzh.orm.dao.base
 * FileName: BaseDao
 * Date: on 2017/12/3  上午11:42
 * Auther: zihe
 * Descirbe:
 * Email: hezihao@linghit.com
 */

public abstract class BaseDao<M> extends AbsDao<M> {
    private static final String TAG = BaseDao.class.getSimpleName();

    @Override
    protected boolean createTable(SQLiteDatabase database) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Field> entry : mFieldMap.entrySet()) {
            //获取列名（字段名）
            String columnName = entry.getKey();
            //取出变量
            Field field = entry.getValue();
            //获取字段值
            TbField tbFieldAnnotation = field.getAnnotation(TbField.class);
            int length = tbFieldAnnotation == null ? 255 : tbFieldAnnotation.length();
            String type = "";
            //将变量使用的类型，转换成数据库支持的数据类型
            Class<?> fieldType = field.getType();
            if (fieldType == String.class) {
                type = "varchar";
            } else if (fieldType == int.class || fieldType == Integer.class) {
                type = "int";
            } else if (fieldType == double.class || fieldType == Double.class) {
                type = "double";
            } else if (fieldType == float.class || fieldType == Float.class) {
                type = "float";
            }
            //拼接创建表字段语句
            if (TextUtils.isEmpty(type)) {
                Log.e(TAG, type.getClass().getName() + "是不支持的字段");
            } else {
                builder.append(columnName);
                builder.append(" ");
                builder.append(type);
                builder.append("(");
                builder.append(length);
                builder.append("),");
            }
        }
        //删除拼接的sql最后一个"，"，因为遍历map不能拿到角标判断是否是最后一个，所以多拼接一个"，"，最后要删除
        builder.deleteCharAt(builder.lastIndexOf(","));
        String str = builder.toString();
        if (TextUtils.isEmpty(str)) {
            Log.e(TAG, "获取不到表字段信息");
            return false;
        }
        String sql = "create table if not exists " + mTbName + " (" + str + ") ";
        Log.e(TAG, "sql ::: ");
        database.execSQL(sql);
        return true;
    }

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
                limit = (startIndex < 0 ? 0 : startIndex) + "," + pageCount;
            }
            if (where != null) {
                Map<String, String> whereMap = getValues(where);
                Condition condition = new Condition(whereMap);
                cursor = mDatabase.query(mTbName, null, condition.whereClause, condition.whereArgs, null, null, orderBy, limit);
            } else {
                cursor = mDatabase.query(mTbName, null, null, null, null, null, orderBy, limit);
            }
            list = getDataList(cursor);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
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
