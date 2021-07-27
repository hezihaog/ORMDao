package com.hzh.orm.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Package: com.hzh.orm.dao.anno
 * FileName: TbName
 * Date: on 2017/12/3  上午11:33
 * Auther: zihe
 * Descirbe: 数据库表名注解
 * Email: hezihao@linghit.com
 */

@Target(ElementType.TYPE)//作用在类上
@Retention(RetentionPolicy.RUNTIME)//保留到运行时
public @interface TbName {
    //表名
    String value();
}
