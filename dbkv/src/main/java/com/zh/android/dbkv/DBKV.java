package com.zh.android.dbkv;

import android.content.Context;
import android.content.SharedPreferences;

import com.hzh.orm.dao.ORMDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 基于数据库实现的Key-Value存储
 */
public class DBKV implements SharedPreferences {
    /**
     * Dao对象
     */
    private final KVDao mDao;
    /**
     * 监听器集合
     */
    private final CopyOnWriteArrayList<OnSharedPreferenceChangeListener> mOnChangeListeners = new CopyOnWriteArrayList<>();
    /**
     * 唯一实例
     */
    private static volatile DBKV sInstance;

    private DBKV(Context context) {
        this.mDao = ORMDao.getDefault(context)
                .getDao(KVDao.class, KV.class);
    }

    /**
     * 获取实例
     */
    public static DBKV getInstance(Context context) {
        if (sInstance == null) {
            synchronized (DBKV.class) {
                if (sInstance == null) {
                    sInstance = new DBKV(context);
                }
            }
        }
        return sInstance;
    }

    @Override
    public Map<String, ?> getAll() {
        KV where = new KV();
        List<KV> list = mDao.query(where);
        HashMap<String, Object> resultMap = new HashMap<>();
        for (KV kv : list) {
            resultMap.put(kv.getKey(), kv.getValue());
        }
        return resultMap;
    }

    @Override
    public String getString(String key, String defValue) {
        String value = getByKey(key);
        if (value == null) {
            return defValue;
        }
        return value;
    }

    @Override
    public Set<String> getStringSet(String key, Set<String> defValues) {
        throw new RuntimeException("不支持该功能");
    }

    @Override
    public int getInt(String key, int defValue) {
        String value = getByKey(key);
        if (value == null) {
            return defValue;
        }
        return Integer.parseInt(value);
    }

    @Override
    public long getLong(String key, long defValue) {
        String value = getByKey(key);
        if (value == null) {
            return defValue;
        }
        return Long.parseLong(value);
    }

    @Override
    public float getFloat(String key, float defValue) {
        String value = getByKey(key);
        if (value == null) {
            return defValue;
        }
        return Long.parseLong(value);
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        String value = getByKey(key);
        if (value == null) {
            return defValue;
        }
        return Boolean.getBoolean(value);
    }

    @Override
    public boolean contains(String key) {
        return getByKey(key) != null;
    }

    /**
     * 根据Key查询Value，因为存储都是String值，获取再进行转换
     *
     * @return 如果不存在，则返回null
     */
    private String getByKey(String key) {
        if (key == null) {
            return null;
        }
        KV where = new KV(key);
        List<KV> list = mDao.query(where);
        if (list.isEmpty()) {
            return null;
        }
        KV kv = list.get(0);
        return kv.getValue();
    }

    @Override
    public Editor edit() {
        return new KVEditor(this);
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        if (!mOnChangeListeners.contains(listener)) {
            mOnChangeListeners.add(listener);
        }
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        mOnChangeListeners.remove(listener);
    }

    private class KVEditor implements Editor {
        private final SharedPreferences mSharedPreferences;

        public KVEditor(SharedPreferences sharedPreferences) {
            mSharedPreferences = sharedPreferences;
        }

        @Override
        public Editor putString(String key, String value) {
            putByString(key, value);
            return this;
        }

        @Override
        public Editor putStringSet(String key, Set<String> value) {
            putByString(key, value);
            return this;
        }

        @Override
        public Editor putInt(String key, int value) {
            putByString(key, value);
            return this;
        }

        @Override
        public Editor putLong(String key, long value) {
            putByString(key, value);
            return this;
        }

        @Override
        public Editor putFloat(String key, float value) {
            putByString(key, value);
            return this;
        }

        @Override
        public Editor putBoolean(String key, boolean value) {
            putByString(key, value);
            return this;
        }

        @Override
        public Editor remove(String key) {
            mDao.delete(new KV());
            return this;
        }

        @Override
        public Editor clear() {
            mDao.deleteAll();
            return this;
        }

        @Override
        public boolean commit() {
            return true;
        }

        @Override
        public void apply() {
        }

        /**
         * 存储统一用字符串
         */
        private void putByString(String key, Object value) {
            //先查询
            List<KV> list = mDao.query(new KV(key));
            //存在，则删除掉
            if (!list.isEmpty()) {
                for (KV kv : list) {
                    mDao.delete(kv);
                }
            }
            //重新插入
            Long result = mDao.insert(new KV(key, String.valueOf(value)));
            if (result > 0) {
                for (OnSharedPreferenceChangeListener listener : mOnChangeListeners) {
                    listener.onSharedPreferenceChanged(mSharedPreferences, key);
                }
            }
        }
    }
}