package com.hzh.orm.dao.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hzh.orm.dao.ORMDao;
import com.hzh.orm.dao.sample.dao.UserDao;
import com.hzh.orm.dao.sample.model.User;

import java.util.List;

/**
 * ORM例子
 */
public class ORMSampleActivity extends AppCompatActivity {
    private final String userName = "wally";

    private UserDao mUserDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orm);
        initView();
        initDb();
    }

    private void initView() {
        Button insertBtn = (Button) findViewById(R.id.insert);
        Button deleteBtn = (Button) findViewById(R.id.delete);
        Button updateBtn = (Button) findViewById(R.id.update);
        Button queryAllBtn = (Button) findViewById(R.id.queryAll);
        Button queryAllAscBtn = (Button) findViewById(R.id.queryAllAsc);
        Button queryAllDescBtn = (Button) findViewById(R.id.queryAllDesc);
        Button queryAllPagingBtn = (Button) findViewById(R.id.queryAllPaging);

        insertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert();
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
        queryAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryAll();
            }
        });
        queryAllAscBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryAllAsc();
            }
        });
        queryAllDescBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryAllDesc();
            }
        });
        queryAllPagingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryAllPaging();
            }
        });
    }

    private void initDb() {
        mUserDao = ORMDao.getDefault(this).getDao(UserDao.class, User.class);
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
        if (id != -1) {
            toast("添加成功：" + user.toString());
        } else {
            toast("添加失败");
        }
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
        if (update != -1) {
            toast("修改成功" + user.toString());
        } else {
            toast("修改失败");
        }
    }

    /**
     * 查询所有数据
     */
    private void queryAll() {
        User where = new User();
        where.setUserName(userName);
        List<User> list = mUserDao.query(where);
        toast(list.toString());
    }

    /**
     * 查询所有数据（正序）
     */
    private void queryAllAsc() {
        List<User> list = mUserDao.query(null, "tb_age asc");
        toast(list.toString());
    }

    /**
     * 查询所有数据（反序）
     */
    private void queryAllDesc() {
        List<User> list = mUserDao.query(null, "tb_age desc");
        toast(list.toString());
    }

    /**
     * 查询所有数据（分页）
     */
    private void queryAllPaging() {
        User where = new User();
        List<User> list = mUserDao.query(where, null, 1, 2);
        toast(list.toString());
    }
}