package com.southernbox.inf.js;

import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.southernbox.inf.activity.DetailActivity;
import com.southernbox.inf.entity.Content;
import com.southernbox.inf.fragment.ItemFragment;
import com.southernbox.inf.pager.ItemViewPager;

/**
 * Created by SouthernBox on 2016/4/1.
 * 实现和JavaScript交互
 */

public class Js2Java {
    Context mContext;
    Content content;
    ItemViewPager viewPager;
    ItemFragment fragment;

    public Js2Java(Context context) {
        this.mContext = context;
    }

    @JavascriptInterface
    public void goDetail(String name, String pic, String htmlUrl) {
        content = new Content();
        content.setName(name);
        content.setPic(pic);
        content.setHtmlUrl(htmlUrl);

        Intent intent = new Intent(mContext, DetailActivity.class);
        intent.putExtra("content", content);
        mContext.startActivity(intent);
    }
}
