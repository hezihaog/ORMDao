package com.hzh.orm.dao.base;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.hzh.orm.dao.anno.TbField;
import com.hzh.orm.dao.anno.TbName;
import com.hzh.orm.dao.inter.IDao;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Package: com.hzh.orm.dao.base
 * FileName: AbsDao
 * Date: on 2017/12/3  下午11:32
 * Auther: zihe
 * Descirbe:
 * Email: hezihao@linghit.com
 */

public abstract class AbsDao<M> implements IDao<M> {
    /**
     * 数据库操作对象
     */
    protected SQLiteDatabase mDatabase;
    /**
     * 表对应的bean对象
     */
    protected Class<M> mEntityClass;
    /**
     * 表名
     */
    protected String mTbName;
    /**
     * 表字段
     */
    protected Map<String, Field> mFieldMap = new LinkedHashMap<>();

    /**
     * 初始化表操作对象，例如创建表，获取表字段和对应的bean类的字段映射关系
     *
     * @param database 数据库对象
     * @param entity   表对应bean对象
     * @return 是否初始化表成功
     */
    protected boolean init(SQLiteDatabase database, Class<M> entity) {
        this.mDatabase = database;
        mEntityClass = entity;
        //必须在打开了数据库的情况下进行
        if (!database.isOpen()) {
            return false;
        }
        //获取类上设置的表名注解
        TbName tbNameAnnotation = entity.getAnnotation(TbName.class);
        mTbName = tbNameAnnotation == null ? entity.getSimpleName() : tbNameAnnotation.value();
        //将类中变量上使用的字段注解的表字段名和字段用Map映射绑定起来
        if (!generateFieldMap()) {
            return false;
        }
        //开始创建表
        return createTable(database);
    }

    /**
     * 获取表映射字段
     *
     * @return 是否获取成功
     */
    private boolean generateFieldMap() {
        //获取表映射类中的public字段，包括父类
        Field[] fields = mEntityClass.getFields();
        if (fields == null || fields.length == 0) {
            //类中无任何字段
            return false;
        }
        for (Field field : fields) {
            field.setAccessible(true);
            //拿取字段上标记的字段注解
            TbField tbField = field.getAnnotation(TbField.class);
            //如果有字段没有标记注解，则以变量名为字段名，否则，则使用字段注解上标记设置的值作为字段名
            mFieldMap.put(tbField == null ? field.getName() : tbField.value(), field);
        }
        return true;
    }


    /**
     * 创建表
     *
     * @param database 数据库对象
     * @return 是否创建表成功
     */
    protected abstract boolean createTable(SQLiteDatabase database);

    /**
     * 条件类，遍历生成where语句
     */
    protected static class Condition {
        //条件语句。如 name=? and password=?
        String whereClause;
        //条件的占位符对应的数值，如 new String[]{"LQR","123456"}
        String[] whereArgs;

        public Condition(Map<String, String> whereMap) {
            StringBuilder builder = new StringBuilder();
            //条件占位符对应的数值的集合
            ArrayList<String> list = new ArrayList<String>();
            String andStr = "and ";
            for (Map.Entry<String, String> entry : whereMap.entrySet()) {
                if (!TextUtils.isEmpty(entry.getValue())) {
                    //拼接删除条件
                    builder.append(andStr);
                    builder.append(entry.getKey());
                    builder.append("=? ");
                    //存储占位符对应的值
                    list.add(entry.getValue());
                }
            }
            //删除开头的"and "，第一句不需要
            this.whereClause = builder.delete(0, andStr.length()).toString();
            this.whereArgs = list.toArray(new String[list.size()]);
        }
    }

    /**
     * 将bean类的字段变量的字段名和值转换成Map键值对
     *
     * @return Map键值对对象
     * @throws IllegalAccessException
     */
    protected Map<String, String> getValues(M entity) throws IllegalAccessException {
        LinkedHashMap<String, String> result = new LinkedHashMap<>();
        for (Map.Entry<String, Field> fieldEntry : mFieldMap.entrySet()) {
            Object value = fieldEntry.getValue().get(entity);
            //取出表名和字段值
            result.put(fieldEntry.getKey(), value == null ? "" : value.toString());
        }
        return result;
    }

    /**
     * 将字段名和字段值Map键值对转换成ContentValue对象
     *
     * @param values 字段名和字段值Map键值对象
     * @return 转换的ContentValue对象
     */
    protected ContentValues getContentValues(Map<String, String> values) {
        ContentValues contentValues = new ContentValues();
        for (Map.Entry<String, String> value : values.entrySet()) {
            contentValues.put(value.getKey(), value.getValue());
        }
        return contentValues;
    }

    /**
     * 通过游标，将表中数据转成对象集合
     */
    protected List<M> getDataList(Cursor cursor) throws IllegalAccessException, InstantiationException {
        if (cursor != null) {
            List<M> result = new ArrayList<M>();
            // 遍历游标，获取表中一行行的数据
            while (cursor.moveToNext()) {
                //获取当前new的对象的 泛型的父类 类型
                ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
                Class<M> clazz = (Class<M>) pt.getActualTypeArguments()[0];// 获取第一个类型参数的真实类型
                M item = clazz.newInstance();
                // 遍历表字段，使用游标一个个取值，赋值给新创建的对象。
                for (String columnName : mFieldMap.keySet()) {
                    //找到表字段
                    // 找到表字段对应的类属性
                    Field field = mFieldMap.get(columnName);
                    // 根据类属性类型，使用游标获取表中的值
                    Object val = null;
                    Class<?> fieldType = field.getType();
                    if (fieldType == String.class) {
                        val = cursor.getString(cursor.getColumnIndex(columnName));
                    } else if (fieldType == int.class || fieldType == Integer.class) {
                        val = cursor.getInt(cursor.getColumnIndex(columnName));
                    } else if (fieldType == double.class || fieldType == Double.class) {
                        val = cursor.getDouble(cursor.getColumnIndex(columnName));
                    } else if (fieldType == float.class || fieldType == Float.class) {
                        val = cursor.getFloat(cursor.getColumnIndex(columnName));
                    }
                    // 反射给对象属性赋值
                    field.set(item, val);
                }
                // 将对象添加到集合中
                result.add(item);
            }
            return result;
        }
        return null;
    }
}
