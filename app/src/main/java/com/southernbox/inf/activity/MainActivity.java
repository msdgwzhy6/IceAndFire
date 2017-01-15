package com.southernbox.inf.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.southernbox.inf.R;
import com.southernbox.inf.bean.OptionBean;
import com.southernbox.inf.pager.ItemViewPager;
import com.southernbox.inf.util.CacheUtils;
import com.southernbox.inf.util.MyStringRequest;
import com.southernbox.inf.util.ServerAPI;
import com.southernbox.inf.util.ToastUtil;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Context mContext;
    private Toolbar mToolbar;
    private DrawerLayout drawer;
    private OptionBean jsonBean;
    private RequestQueue mQueue;
    public static ArrayList<ItemViewPager> viewPagers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolBar();
        initDrawerLayout();
        initNavigationView();

        mContext = this;
        mQueue = Volley.newRequestQueue(this);

        // 优先加载本地缓存数据
        String cacheData = CacheUtils.getString(mContext, ServerAPI.OPTION_URL,
                null);
        if (!TextUtils.isEmpty(cacheData)) {
            processData(cacheData);
        }

        // 从网络加载数据
        MyStringRequest stringRequest = new MyStringRequest(ServerAPI.OPTION_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                CacheUtils.putString(mContext, ServerAPI.OPTION_URL, s);
                processData(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.toastShow(mContext, "网络连接失败");
            }
        });
        mQueue.add(stringRequest);
    }

    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initDrawerLayout() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    private void initNavigationView() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

    private void processData(String json) {
        Gson gson = new Gson();
        this.jsonBean = gson.fromJson(json, OptionBean.class);
        if (jsonBean != null) {
            initViewPager();
        }
    }

    private void initViewPager() {
        viewPagers = new ArrayList<ItemViewPager>();
        int size = jsonBean.data.size();
        for (int i = 0; i < size; i++) {
            viewPagers.add(new ItemViewPager(mContext, jsonBean.data.get(i)));
        }
        viewPagers.get(0).initData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int position = 0;

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_person) {
            position = 0;
        } else if (id == R.id.nav_house) {
            position = 1;
        } else if (id == R.id.nav_history) {
            position = 2;
        } else if (id == R.id.nav_castles) {
            position = 3;
        }

        viewPagers.get(position).initData();
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - mExitTime > 2000) {
                ToastUtil.toastShow(this, "再按一次退出");
                mExitTime = System.currentTimeMillis();
            } else {
                ToastUtil.toastCancel();
                finish();
            }
        }
        return true;
    }
}