package com.southernbox.inf.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.southernbox.inf.R;
import com.southernbox.inf.util.DayNightHelper;
import com.southernbox.inf.util.RequestServes;
import com.southernbox.inf.util.ServerAPI;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by nanquan.lin on 2017/2/20 0020.
 * Activity基类
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected Context mContext;
    protected DayNightHelper mDayNightHelper;
    protected Realm mRealm;
    protected RequestServes requestServes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        //设置主题
        mDayNightHelper = new DayNightHelper(this);
        if (mDayNightHelper.isDay()) {
            setTheme(R.style.DayTheme);
        } else {
            setTheme(R.style.NightTheme);
        }

        //初始化Realm
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder().build();
        try {
            mRealm = Realm.getInstance(realmConfig);
        } catch (RuntimeException e) {
            //删除数据库后重新初始化
            Realm.deleteRealm(realmConfig);
            mRealm = Realm.getInstance(realmConfig);
        }

        //初始化Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerAPI.BASE_URL)
                //增加返回值为String的支持
                .addConverterFactory(ScalarsConverterFactory.create())
                //增加返回值为实体类的支持
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        requestServes = retrofit.create(RequestServes.class);
    }

}
