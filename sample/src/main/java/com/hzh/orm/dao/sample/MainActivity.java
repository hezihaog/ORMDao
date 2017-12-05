package com.hzh.orm.dao.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hzh.logger.L;
import com.hzh.orm.dao.DaoManager;
import com.hzh.orm.dao.base.BaseDaoFactory;
import com.hzh.orm.dao.sample.dao.UserDao;
import com.hzh.orm.dao.sample.model.User;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String DB_NAME = "orm_dao.db";
    private static final String userName = "wally";

    private Button insert;
    private Button delete;
    private Button update;
    private Button queryAll;
    private Button queryAllAsc;
    private Button queryAllDesc;
    private Button queryAllPaging;
    private UserDao mUserDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.insert = (Button) findViewById(R.id.insert);
        this.delete = (Button) findViewById(R.id.delete);
        this.update = (Button) findViewById(R.id.update);
        this.queryAll = (Button) findViewById(R.id.queryAll);
        this.queryAllAsc = (Button) findViewById(R.id.queryAllAsc);
        this.queryAllDesc = (Button) findViewById(R.id.queryAllDesc);
        this.queryAllPaging = (Button) findViewById(R.id.queryAllPaging);

        insert.setOnClickListener(this);
        delete.setOnClickListener(this);
        update.setOnClickListener(this);
        queryAll.setOnClickListener(this);
        queryAllAsc.setOnClickListener(this);
        queryAllDesc.setOnClickListener(this);
        queryAllPaging.setOnClickListener(this);

        DaoManager.init(new File(getFilesDir(), DB_NAME).getAbsolutePath());
        mUserDao = BaseDaoFactory.getInstance().createDao(UserDao.class, User.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.insert:
                insert();
                break;
            case R.id.delete:
                delete();
                break;
            case R.id.update:
                update();
                break;
            case R.id.queryAll:
                queryAll();
                break;
            case R.id.queryAllAsc:
                queryAllAsc();
                break;
            case R.id.queryAllDesc:
                queryAllDesc();
                break;
            case R.id.queryAllPaging:
                queryAllPaging();
                break;
        }
    }

    private void toast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 添加一条数据
     */
    private void insert() {
        User user = new User(userName, "i am wally", "23");
        Long id = mUserDao.insert(user);
        toast("添加了" + (id != -1 ? 1 : 0) + "条数据");
    }

    /**
     * 删除一条数据
     */
    private void delete() {
        User where = new User(userName);
        Integer delete = mUserDao.delete(where);
        toast("删除了" + delete + "条数据");
    }

    /**
     * 更新一条数据
     */
    private void update() {
        User user = new User("barry", "i love u");
        User where = new User();
        where.setUserName(userName);
        Integer update = mUserDao.update(user, where);
        toast("修改了" + update + "条数据");
    }

    /**
     * 查询所有数据
     */
    private void queryAll() {
        User where = new User();
        where.setUserName(userName);
        List<User> list = mUserDao.query(where);
        int query = list == null ? 0 : list.size();
        toast("查出了" + query + "条数据");
        for (User user : list) {
            L.d(user);
        }
    }

    /**
     * 查询所有数据（正序）
     */
    private void queryAllAsc() {
        List<User> list = mUserDao.query(null, "tb_age asc");
        int query = list == null ? 0 : list.size();
        toast("查出了" + query + "条数据");
        for (User user : list) {
            L.d(user);
        }
    }

    /**
     * 查询所有数据（反序）
     */
    private void queryAllDesc() {
        List<User> list = mUserDao.query(null, "tb_age desc");
        int query = list == null ? 0 : list.size();
        toast("查出了" + query + "条数据");
        for (User user : list) {
            L.d(user);
        }
    }

    /**
     * 查询所有数据（分页）
     */
    private void queryAllPaging() {
        User where = new User();
        List<User> list = mUserDao.query(where, null, 1, 2);
        int query = list == null ? 0 : list.size();
        toast("查出了" + query + "条数据");
        for (User user : list) {
            L.d(user);
        }
    }
}
