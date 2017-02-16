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
import com.southernbox.inf.entity.Option;
import com.southernbox.inf.fragment.ItemFragment;

import java.util.ArrayList;

/**
 * Created by SouthernBox on 2016/3/28.
 * 首页ViewPager
 */

public class ItemViewPager {
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ArrayList<Fragment> fragments;
    private String[] titles;
    private Option option;
    private MainActivity mainActivity;

    public ItemViewPager(Context mContext, Option option) {
        this.option = option;
        mainActivity = (MainActivity) mContext;
        initView();
    }

    private void initView() {
        mToolbar = (Toolbar) mainActivity.findViewById(R.id.main_toolbar);
        mTabLayout = (TabLayout) mainActivity.findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) mainActivity.findViewById(R.id.view_pager);
    }

    private void initFragment() {
        fragments = new ArrayList<>();
        int size = option.getSecondOptionList().size();
        titles = new String[size];
        for (int i = 0; i < size; i++) {
            titles[i] = option.getSecondOptionList().get(i).getTitle();
            ItemFragment fragment = new ItemFragment();
            // 传递参数到Fragment中
            Bundle bundle = new Bundle();
            bundle.putString("json_url", option.getSecondOptionList().get(i).getJsonUrl());
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
    }

    public void initData() {
        initFragment();

        mToolbar.setTitle(option.getTitle());

        mViewPager.setAdapter(new MyFragmentPagerAdapter(mainActivity.getSupportFragmentManager(), fragments, titles));
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
