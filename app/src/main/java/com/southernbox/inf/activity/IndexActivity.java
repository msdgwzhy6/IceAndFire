package com.southernbox.inf.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.southernbox.inf.R;

/**
 * Created SouthernBox on 2016/3/27.
 * 启动页面
 */

public class IndexActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ImageView ivIndex = (ImageView) findViewById(R.id.iv_index);
        Animation animation = AnimationUtils.loadAnimation(this,
                R.anim.anim_valar_morghulis);
        animation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                SystemClock.sleep(500);
                startActivity(new Intent(IndexActivity.this,
                        MainActivity.class));
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

        });
        SystemClock.sleep(200);
        ivIndex.startAnimation(animation);

    }

}
