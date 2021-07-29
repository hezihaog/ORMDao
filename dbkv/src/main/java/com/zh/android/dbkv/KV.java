package com.zh.android.dbkv;

import com.hzh.orm.dao.annotation.TbField;
import com.hzh.orm.dao.annotation.TbName;

/**
 * Key-Value表
 */
@TbName("tb_kv")
public class KV {
    /**
     * Key
     */
    @TbField(value = "tb_key", length = 30)
    public String key;

    /**
     * Value
     */
    @TbField(value = "tb_value", length = 20)
    public String value;

    public KV() {
    }

    public KV(String key) {
        this.key = key;
    }

    public KV(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "KV{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}