package com.southernbox.inf.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * ViewPager适配器
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter{

    private ArrayList<Fragment> fragments;
    private String[] titles;

    public MyFragmentPagerAdapter(FragmentManager fm,
                                  ArrayList<Fragment> fragments,
                                  String[] titles) {

        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }

    @Override
    public long getItemId(int position) {
        return fragments.get(position).hashCode();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    // tablayout显示时用到此方法
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
