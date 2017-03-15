package com.southernbox.inf.pager;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;

import com.southernbox.inf.R;
import com.southernbox.inf.activity.MainActivity;
import com.southernbox.inf.adapter.MainFragmentPagerAdapter;
import com.southernbox.inf.entity.ContentDTO;
import com.southernbox.inf.entity.TabDTO;
import com.southernbox.inf.fragment.MainFragment;
import com.southernbox.inf.util.DisplayUtil;
import com.southernbox.inf.util.RequestServes;
import com.southernbox.inf.util.ServerAPI;
import com.southernbox.inf.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by SouthernBox on 2016/3/28.
 * 首页ViewPager
 */

public class MainViewPager {

    private MainActivity mainActivity;
    private Context mContext;

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ViewPager mViewPager;

    private ArrayList<MainFragment> fragments;
    private String title;
    private String[] tabTitles;
    private List<TabDTO> tabList;
    private Realm mRealm;

    public MainViewPager(MainActivity mainActivity, String title, List<TabDTO> tabList) {
        this.title = title;
        this.tabList = tabList;
        this.mainActivity = mainActivity;
        this.mContext = mainActivity;
        Realm.init(mainActivity);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder().build();
        mRealm = Realm.getInstance(realmConfig);
        initView();
    }

    private void initView() {
        mToolbar = (Toolbar) mainActivity.findViewById(R.id.main_toolbar);
        mTabLayout = (TabLayout) mainActivity.findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) mainActivity.findViewById(R.id.view_pager);
        //滑动时禁用SwipeRefreshLayout
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:// 经测试，ViewPager的DOWN事件不会被分发下来
                    case MotionEvent.ACTION_MOVE:
                        mSwipeRefreshLayout.setEnabled(false);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        mSwipeRefreshLayout.setEnabled(true);
                        break;
                }
                return false;
            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout) mainActivity.findViewById(R.id.switch_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);
        mSwipeRefreshLayout.setProgressViewOffset(false,
                DisplayUtil.getPx(mContext, -50), DisplayUtil.getPx(mContext, 20));
        SwipeRefreshLayout.OnRefreshListener refreshListener =
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        loadData();
                    }
                };
        mSwipeRefreshLayout.setOnRefreshListener(refreshListener);
    }

    /**
     * 加载网络数据
     */
    private void loadData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerAPI.BASE_URL)
                //增加返回值为Gson的支持(以实体类返回)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestServes requestServes = retrofit.create(RequestServes.class);
        Call<List<ContentDTO>> call = requestServes.getContent();
        call.enqueue(new Callback<List<ContentDTO>>() {
            @Override
            public void onResponse(Call<List<ContentDTO>> call,
                                   retrofit2.Response<List<ContentDTO>> response) {
                mSwipeRefreshLayout.setRefreshing(false);
                List<ContentDTO> list = response.body();
                if (list != null) {
                    //缓存到数据库
                    mRealm.beginTransaction();
                    mRealm.copyToRealmOrUpdate(list);
                    mRealm.commitTransaction();
                }
                refreshFragment();
            }

            @Override
            public void onFailure(Call<List<ContentDTO>> call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
                ToastUtil.show(mContext, "网络连接失败");
            }
        });
    }

    private void refreshFragment() {
        for (MainFragment fragment : fragments) {
            if (fragment.isAdded()) {
                fragment.showData();
            }
        }
    }

    public void initData() {
        initFragment();
        mToolbar.setTitle(title);
        mViewPager.setAdapter(new MainFragmentPagerAdapter(
                mainActivity.getSupportFragmentManager(),
                fragments, tabTitles));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initFragment() {
        fragments = new ArrayList<>();
        int size = tabList.size();
        tabTitles = new String[size];
        for (int i = 0; i < size; i++) {
            TabDTO tab = tabList.get(i);
            tabTitles[i] = tab.getTitle();
            MainFragment fragment = MainFragment
                    .newInstance(tab.getFirstType(), tab.getSecondType());
            fragments.add(fragment);
        }
    }

    public void refreshUI() {
        for (MainFragment fragment : fragments) {
            fragment.refreshUI();
        }
    }
}
