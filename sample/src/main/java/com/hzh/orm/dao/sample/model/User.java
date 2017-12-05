package com.hzh.orm.dao.sample.model;

import com.hzh.orm.dao.anno.TbField;
import com.hzh.orm.dao.anno.TbName;
import com.hzh.orm.dao.inter.IModel;

/**
 * Package: com.hzh.orm.dao.sample.model
 * FileName: User
 * Date: on 2017/12/4  下午1:48
 * Auther: zihe
 * Descirbe:
 * Email: hezihao@linghit.com
 */

@TbName("tb_user")
public class User implements IModel {
    /**
     * 用户名
     */
    @TbField(value = "tb_user_name", length = 30)
    public String userName;

    /**
     * 个性签名
     */
    @TbField(value = "tb_sign", length = 20)
    public String sign;

    /**
     * 年龄
     */
    @TbField(value = "tb_age", length = 11)
    public String age;

    public User() {
    }

    public User(String userName) {
        this.userName = userName;
    }

    public User(String userName, String sign) {
        this.userName = userName;
        this.sign = sign;
    }

    public User(String userName, String sign, String age) {
        this.userName = userName;
        this.sign = sign;
        this.age = age;
    }

    public String getUserName() {
        return userName;
    }

    public User setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getSign() {
        return sign;
    }

    public User setSign(String sign) {
        this.sign = sign;
        return this;
    }

    public String getAge() {
        return age;
    }

    public User setAge(String age) {
        this.age = age;
        return this;
    }

    @Override
    public String toString() {
        return "[userName:" + this.getUserName() + ", sign:" + this.getSign() + ", age:" + this.getAge() + "]";
    }
}