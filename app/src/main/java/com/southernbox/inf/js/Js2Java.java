package com.southernbox.inf.js;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.southernbox.inf.activity.DetailActivity;

/**
 * Created by SouthernBox on 2016/4/1.
 * 实现和JavaScript交互
 */

public class Js2Java {

    private Context mContext;

    public Js2Java(Context context) {
        this.mContext = context;
    }

    @JavascriptInterface
    public void goDetail(String name, String img, String htmlUrl) {
        DetailActivity.show(mContext, name, img, htmlUrl);
    }
}
