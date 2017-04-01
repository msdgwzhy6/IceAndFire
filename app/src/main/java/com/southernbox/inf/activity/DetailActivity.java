package com.southernbox.inf.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;

import com.bumptech.glide.Glide;
import com.southernbox.inf.R;
import com.southernbox.inf.databinding.ActivityDetailBinding;
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

    private String title;
    private String img;
    private String html;

    private ActivityDetailBinding binding;

    public static void show(Context context, ActivityOptionsCompat options,
                            String title, String img, String html) {
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        Bundle bundle = getIntent().getExtras();
        title = bundle.getString("title");
        img = bundle.getString("img");
        html = bundle.getString("html");
        initView();
        initData();
    }

    private void initView() {
        Resources.Theme theme = mContext.getTheme();
        TypedValue lightTextColor = new TypedValue();
        theme.resolveAttribute(R.attr.lightTextColor, lightTextColor, true);
        binding.collapsingToolbar
                .setCollapsedTitleTextColor(ContextCompat
                        .getColor(mContext, lightTextColor.resourceId));
        binding.collapsingToolbar
                .setExpandedTitleTextColor(ContextCompat
                        .getColorStateList(mContext, lightTextColor.resourceId));
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.addJavascriptInterface(new Js2Java(this), "Android");
        // 支持多窗口
        binding.webView.getSettings().setSupportMultipleWindows(true);
        // 开启 DOM storage API 功能
        binding.webView.getSettings().setDomStorageEnabled(true);
        // 开启 Application Caches 功能
        binding.webView.getSettings().setAppCacheEnabled(true);

        binding.toolbar.post(new Runnable() {
            @Override
            public void run() {
                //设置Toolbar的图标颜色
                Drawable navigationIcon = binding.toolbar.getNavigationIcon();
                if (navigationIcon != null) {
                    if (mDayNightHelper.isDay()) {
                        binding.toolbar.getNavigationIcon().setAlpha(255);
                    } else {
                        binding.toolbar.getNavigationIcon().setAlpha(128);
                    }
                }
            }
        });
    }

    private void initData() {
        binding.toolbar.setTitle(title);

        Glide
                .with(this)
                .load(ServerAPI.BASE_URL + img)
                .override(480, 270)
                .crossFade()
                .into(binding.imageView);

        Call<String> call = requestServes.get(html);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.body() != null) {
                    String htmlData = response.body();
                    if (mDayNightHelper.isNight()) {
                        htmlData = htmlData.replace("p {",
                                "p {color:#9F9F9F;");
                        htmlData = htmlData.replace("<body>", "<body bgcolor=\"#4F4F4F\">");
                    }
                    binding.webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
                    binding.webView.loadDataWithBaseURL(
                            "file:///android_asset/", htmlData, "text/html", "utf-8", null);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ToastUtil.show(mContext, "网络连接失败，请重试");
                binding.webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            }
        });

        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.webView.setVisibility(View.GONE);
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            binding.webView.setVisibility(View.GONE);
            onBackPressed();
        }
        return true;
    }
}
