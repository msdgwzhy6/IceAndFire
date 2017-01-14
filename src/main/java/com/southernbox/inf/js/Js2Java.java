package com.southernbox.inf.js;

import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.southernbox.inf.bean.ContentBean;
import com.southernbox.inf.fragment.ItemFragment;
import com.southernbox.inf.activity.DetailActivity;
import com.southernbox.inf.activity.MainActivity;
import com.southernbox.inf.pager.ItemViewPager;

/**
 * Created by SouthernBox on 2016/4/1.
 */
public class Js2Java {
    Context mContext;
    ContentBean.Content bean;
    ItemViewPager viewPager;
    ItemFragment fragment;

    public Js2Java(Context context) {
        this.mContext = context;
    }

    @JavascriptInterface
    public void goDetail(String name, String pic, String htmlUrl) {
        bean = new ContentBean().new Content();
        bean.name = name;
        bean.pic = pic;
        bean.htmlUrl = htmlUrl;

        Intent intent = new Intent(mContext, DetailActivity.class);
        intent.putExtra("content", bean);
        mContext.startActivity(intent);
    }
}
