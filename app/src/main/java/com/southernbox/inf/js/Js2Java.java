package com.southernbox.inf.js;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.southernbox.inf.activity.DetailActivity;
import com.southernbox.inf.entity.ContentDTO;

import io.realm.Realm;
import io.realm.RealmConfiguration;

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
    public void goDetail(String id) {
        RealmConfiguration realmConfig = new RealmConfiguration.Builder().build();
        Realm mRealm = Realm.getInstance(realmConfig);

        ContentDTO content = mRealm.where(ContentDTO.class)
                .equalTo("id", id)
                .findFirst();

        if (content != null) {
            DetailActivity.show(
                    mContext,
                    content.getName(),
                    content.getImg(),
                    content.getHtml());
        }
    }
}
