package com.southernbox.inf.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.southernbox.inf.R;
import com.southernbox.inf.js.Js2Java;
import com.southernbox.inf.util.ServerAPI;
import com.southernbox.inf.util.ToastUtil;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created SouthernBox on 2016/3/27.
 * 详情页面
 */

@SuppressLint("SetJavaScriptEnabled")
public class DetailActivity extends BaseActivity {

    private Toolbar mToolbar;
    private ImageView mImageView;
    private WebView mWebView;
    private String title;
    private String img;
    private String html;

    public static void show(Context context, ActivityOptionsCompat options, String title, String img, String html) {
        Intent intent = new Intent(context, DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("img", img);
        bundle.putString("html", html);
        intent.putExtras(bundle);
        ActivityCompat.startActivity(context, intent, options.toBundle());
    }

    public static void show(Context context, String title, String img, String html) {
        Intent intent = new Intent(context, DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("img", img);
        bundle.putString("html", html);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Bundle bundle = getIntent().getExtras();
        title = bundle.getString("title");
        img = bundle.getString("img");
        html = bundle.getString("html");
        initView();
        initData();
    }

    public void initView() {
        Resources.Theme theme = mContext.getTheme();
        TypedValue lightTextColor = new TypedValue();
        theme.resolveAttribute(R.attr.lightTextColor, lightTextColor, true);
        CollapsingToolbarLayout mCollapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout
                .setCollapsedTitleTextColor(ContextCompat
                        .getColor(mContext, lightTextColor.resourceId));
        mCollapsingToolbarLayout
                .setExpandedTitleTextColor(ContextCompat
                        .getColorStateList(mContext, lightTextColor.resourceId));
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mImageView = (ImageView) findViewById(R.id.image_view);
        mWebView = (WebView) findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new Js2Java(this), "Android");

        mToolbar.post(new Runnable() {
            @Override
            public void run() {
                //设置Toolbar的图标颜色
                Drawable navigationIcon = mToolbar.getNavigationIcon();
                if (navigationIcon != null) {
                    if (mDayNightHelper.isDay()) {
                        mToolbar.getNavigationIcon().setAlpha(255);
                    } else {
                        mToolbar.getNavigationIcon().setAlpha(128);
                    }
                }
            }
        });
    }

    public void initData() {
        mToolbar.setTitle(title);

        Glide
                .with(this)
                .load(ServerAPI.BASE_URL + img)
                .override(480, 270)
                .crossFade()
                .into(mImageView);

        Call<String> call = requestServes.get(html);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.body() != null) {
                    String htmlData = response.body();
                    if (mDayNightHelper.isNight()) {
                        htmlData = htmlData.replace("}\n\t\t</style>\n\t</head>",
                                "color:#9F9F9F;}\n\t\t</style>\n\t</head>");
                        htmlData = htmlData.replace("<body>", "<body bgcolor=\"#4F4F4F\">");
                    }
                    mWebView.loadDataWithBaseURL("file:///android_asset/", htmlData, "text/html", "utf-8", null);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ToastUtil.show(mContext, "网络连接失败，请重试");
            }
        });

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.setVisibility(View.GONE);
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mWebView.setVisibility(View.GONE);
            onBackPressed();
        }
        return true;
    }
}
