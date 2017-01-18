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
import com.southernbox.inf.bean.OptionBean;
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
    private OptionBean.Option option;
    private MainActivity mainActivity;

    public ItemViewPager(Context mContext, OptionBean.Option option) {
        this.option = option;
        mainActivity = (MainActivity) mContext;
        initView();
    }

    private void initView() {
        mToolbar = (Toolbar) mainActivity.findViewById(R.id.main_toolbar);
        mTabLayout = (TabLayout) mainActivity.findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) mainActivity.findViewById(R.id.view_pager);
        initFragment();
    }

    private void initFragment() {
        fragments = new ArrayList<>();
        int size = option.secondOptionList.size();
        titles = new String[size];
        for (int i = 0; i < size; i++) {
            titles[i] = option.secondOptionList.get(i).title;
            ItemFragment fragment = new ItemFragment();
            // 传递参数到Fragment中
            Bundle bundle = new Bundle();
            bundle.putString("json_url", option.secondOptionList.get(i).jsonUrl);
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
    }

    public void initData() {
        mToolbar.setTitle(option.title);

        mViewPager.setAdapter(new MyFragmentPagerAdapter(mainActivity.getSupportFragmentManager(), fragments, titles));
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
