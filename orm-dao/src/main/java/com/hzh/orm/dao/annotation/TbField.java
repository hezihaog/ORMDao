package com.hzh.orm.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Package: com.hzh.orm.dao.anno
 * FileName: TbField
 * Date: on 2017/12/3  上午11:34
 * Auther: zihe
 * Descirbe:数据库字段注解
 */
@Target(ElementType.FIELD)//作用在成员变量上
@Retention(RetentionPolicy.RUNTIME)//保留到运行时
public @interface TbField {
    //字段名
    String value();

    //字段值的长度
    int length();
}