package com.southernbox.inf.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

import com.southernbox.inf.R;
import com.southernbox.inf.databinding.ActivityIndexBinding;
import com.southernbox.inf.entity.ContentDTO;
import com.southernbox.inf.entity.TabDTO;
import com.southernbox.inf.util.ToastUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created SouthernBox on 2016/3/27.
 * 启动页面
 */

public class IndexActivity extends BaseActivity {

    private boolean animationComplete;
    private boolean loadTabComplete;
    private boolean loadContentComplete;

    private ActivityIndexBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_index);
        showAnimation();
    }

    /**
     * 显示启动页动画
     */
    private void showAnimation() {
        Animation animation = AnimationUtils
                .loadAnimation(this, R.anim.anim_valar_morghulis);
        animation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                SystemClock.sleep(500);
                animationComplete = true;
                goMainPage();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

        });
        SystemClock.sleep(200);
        binding.tvIndex.startAnimation(animation);
        loadTabData();
        loadContentData();
    }

    /**
     * 获取标签数据
     */
    private void loadTabData() {
        Call<List<TabDTO>> call = requestServes.getTab();
        call.enqueue(new Callback<List<TabDTO>>() {
            @Override
            public void onResponse(@NonNull Call<List<TabDTO>> call,
                                   @NonNull retrofit2.Response<List<TabDTO>> response) {
                List<TabDTO> tabList = response.body();
                if (tabList != null) {
                    //缓存到数据库
                    mRealm.beginTransaction();
                    mRealm.copyToRealmOrUpdate(tabList);
                    mRealm.commitTransaction();
                }
                loadTabComplete = true;
                goMainPage();
            }

            @Override
            public void onFailure(@NonNull Call<List<TabDTO>> call, @NonNull Throwable t) {
                List<TabDTO> tabList = mRealm.where(TabDTO.class).findAll();
                //有缓存数据可正常跳转，没有则提示点击重试
                if (tabList != null) {
                    ToastUtil.show(mContext, "网络连接失败");
                    loadTabComplete = true;
                    goMainPage();
                } else {
                    netError();
                }
            }
        });
    }

    /**
     * 获取内容数据
     */
    private void loadContentData() {
        Call<List<ContentDTO>> call = requestServes.getContent();
        call.enqueue(new Callback<List<ContentDTO>>() {
            @Override
            public void onResponse(@NonNull Call<List<ContentDTO>> call,
                                   @NonNull retrofit2.Response<List<ContentDTO>> response) {
                List<ContentDTO> contentList = response.body();
                if (contentList != null) {
                    //缓存到数据库
                    mRealm.beginTransaction();
                    mRealm.copyToRealmOrUpdate(contentList);
                    mRealm.commitTransaction();
                }
                loadContentComplete = true;
                goMainPage();
            }

            @Override
            public void onFailure(@NonNull Call<List<ContentDTO>> call, @NonNull Throwable t) {
                List<ContentDTO> contentList = mRealm.where(ContentDTO.class).findAll();
                //有缓存数据可正常跳转，没有则提示点击重试
                if (contentList != null) {
                    ToastUtil.show(mContext, "网络连接失败");
                    loadContentComplete = true;
                    goMainPage();
                } else {
                    netError();
                }
            }
        });
    }

    private void goMainPage() {
        if (animationComplete && loadTabComplete && loadContentComplete) {
            startActivity(new Intent(IndexActivity.this, MainActivity.class));
            finish();
        }
    }

    private void netError() {
        ToastUtil.show(mContext, "网络连接失败，请点击重试");
        binding.tvIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadTabData();
                loadContentData();
                binding.tvIndex.setClickable(false);
            }
        });
    }

}
