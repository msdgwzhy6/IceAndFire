package com.southernbox.inf.pager;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.southernbox.inf.R;
import com.southernbox.inf.activity.MainActivity;
import com.southernbox.inf.adapter.MyFragmentPagerAdapter;
import com.southernbox.inf.entity.TabDTO;
import com.southernbox.inf.fragment.ItemFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SouthernBox on 2016/3/28.
 * 首页ViewPager
 */

public class ItemViewPager {

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ArrayList<Fragment> fragments;
    private String title;
    private String[] tabTitles;
    //    private Option option;
    private List<TabDTO> tabList;
    private MainActivity mainActivity;

    public ItemViewPager(Context mContext, String title, List<TabDTO> tabList) {
        this.title = title;
        this.tabList = tabList;
        mainActivity = (MainActivity) mContext;
        initView();
    }

    private void initView() {
        mToolbar = (Toolbar) mainActivity.findViewById(R.id.main_toolbar);
        mTabLayout = (TabLayout) mainActivity.findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) mainActivity.findViewById(R.id.view_pager);
        mViewPager.removeAllViews();
    }

    private void initFragment() {
        fragments = new ArrayList<>();
        int size = tabList.size();
        tabTitles = new String[size];
        for (int i = 0; i < size; i++) {
            tabTitles[i] = tabList.get(i).getTitle();
            ItemFragment fragment = new ItemFragment();
            // 传递参数到Fragment中
            Bundle bundle = new Bundle();
            bundle.putString("type", tabList.get(i).getSecondType());
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
    }

    public void initData() {
        initFragment();

        mToolbar.setTitle(title);

        mViewPager.setAdapter(new MyFragmentPagerAdapter(
                mainActivity.getSupportFragmentManager(),
                fragments, tabTitles));
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
