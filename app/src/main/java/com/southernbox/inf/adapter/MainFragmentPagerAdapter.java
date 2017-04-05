package com.southernbox.inf.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.TextUtils;

import com.southernbox.inf.entity.TabDTO;
import com.southernbox.inf.fragment.MainFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SouthernBox on 2016/3/27.
 * 首页ViewPager适配器
 */

public class MainFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<MainFragment> fragments;
    private List<TabDTO> tabList;

    public MainFragmentPagerAdapter(FragmentManager fm,
                                    ArrayList<MainFragment> fragments,
                                    List<TabDTO> tabList) {
        super(fm);
        this.fragments = fragments;
        this.tabList = tabList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (!TextUtils.isEmpty(tabList.get(position).getTitle())) {
            return tabList.get(position).getTitle();
        } else {
            return "";
        }
    }
}
