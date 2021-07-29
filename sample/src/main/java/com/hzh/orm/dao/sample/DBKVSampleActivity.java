package com.hzh.orm.dao.sample;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.zh.android.dbkv.DBKV;

import java.util.Map;

/**
 * DBKV例子
 */
public class DBKVSampleActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbkv);
        findViewById(R.id.insert_string).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBKV.getInstance(DBKVSampleActivity.this)
                        .edit()
                        .putString("key_string", "Hello DBKV")
                        .apply();
            }
        });
        findViewById(R.id.insert_int).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBKV.getInstance(DBKVSampleActivity.this)
                        .edit()
                        .putInt("key_int", 1024)
                        .apply();
            }
        });
        findViewById(R.id.insert_long).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBKV.getInstance(DBKVSampleActivity.this)
                        .edit()
                        .putLong("key_long", 10240000)
                        .apply();
            }
        });
        findViewById(R.id.insert_float).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBKV.getInstance(DBKVSampleActivity.this)
                        .edit()
                        .putFloat("key_int", 3.14f)
                        .apply();
            }
        });
        findViewById(R.id.insert_boolean).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBKV.getInstance(DBKVSampleActivity.this)
                        .edit()
                        .putBoolean("key_boolean", true)
                        .apply();
            }
        });
        findViewById(R.id.delete_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBKV dbkv = DBKV.getInstance(DBKVSampleActivity.this);
                //删除所有
                dbkv.edit().clear().apply();
                //查询一下
                Map<String, ?> resultMap = dbkv.getAll();
                //为空，则删除成功
                if (resultMap.isEmpty()) {
                    toast("删除成功");
                } else {
                    toast("删除失败");
                }
            }
        });
        findViewById(R.id.get_string).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = DBKV.getInstance(DBKVSampleActivity.this)
                        .getString("key_string", "default string");
                toast(value);
            }
        });
        findViewById(R.id.get_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, ?> resultMap = DBKV.getInstance(DBKVSampleActivity.this).getAll();
                toast(resultMap.toString());
            }
        });
        findViewById(R.id.is_contains).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isContains = DBKV.getInstance(DBKVSampleActivity.this).contains("key_string");
                toast("isContains => " + isContains);
            }
        });
        //注册变化监听
        DBKV.getInstance(DBKVSampleActivity.this).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销变化监听
        DBKV.getInstance(DBKVSampleActivity.this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        toast(key + " => 被更改了");
    }

    private void toast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}