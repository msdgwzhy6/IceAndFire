package com.southernbox.inf.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.southernbox.inf.R;
import com.southernbox.inf.js.Js2Java;
import com.southernbox.inf.util.ServerAPI;

/**
 * Created SouthernBox on 2016/3/27.
 * 详情页面
 */

@SuppressLint("SetJavaScriptEnabled")
public class DetailActivity extends AppCompatActivity {

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
        mToolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        mImageView = (ImageView) findViewById(R.id.image_view);
        mWebView = (WebView) findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new Js2Java(this), "Android");
    }

    public void initData() {
        mToolbar.setTitle(title);

        Glide
                .with(this)
                .load(ServerAPI.BASE_URL + img)
                .override(480, 270)
                .crossFade()
                .into(mImageView);

        mWebView.loadUrl(ServerAPI.BASE_URL + html);

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
