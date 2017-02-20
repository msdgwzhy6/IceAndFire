package com.southernbox.inf.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.southernbox.inf.R;
import com.southernbox.inf.entity.TabDTO;
import com.southernbox.inf.pager.ItemViewPager;
import com.southernbox.inf.util.DayNightHelper;
import com.southernbox.inf.util.ToastUtil;

import java.util.List;

/**
 * Created SouthernBox on 2016/3/27.
 * 主页
 */

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final static String TYPE_PERSON = "person";
    private final static String TYPE_HOUSE = "house";
    private final static String TYPE_HISTORY = "history";
    private final static String TYPE_CASTLE = "castle";

    private Toolbar mToolbar;
    private DrawerLayout drawer;
    private List<TabDTO> tabList;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolBar();
        initDrawerLayout();
        initNavigationView();

        //从缓存加载数据
        tabList = mRealm.where(TabDTO.class).findAll();
        if (tabList != null) {
            initViewPager("人物", TYPE_PERSON);
        }
    }

    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initDrawerLayout() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void initNavigationView() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View navigationHeader = navigationView.getHeaderView(0);
        if (mDayNightHelper.isDay()) {
            navigationHeader.setBackgroundResource(R.drawable.side_nav_bar_day);
        } else {
            navigationHeader.setBackgroundResource(R.drawable.side_nav_bar_night);
        }

        Menu menu = navigationView.getMenu();
        MenuItem nightItem = menu.findItem(R.id.nav_night);
        View nightView = MenuItemCompat.getActionView(nightItem);
        SwitchCompat switchCompat = (SwitchCompat) nightView.findViewById(R.id.switch_compat);

        if (mDayNightHelper.isDay()) {
            switchCompat.setChecked(false);
        } else {
            switchCompat.setChecked(true);
        }

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mDayNightHelper.setMode(DayNightHelper.DayNight.NIGHT);
                    setTheme(R.style.NightTheme);
                    refreshUI(false);
                } else {
                    mDayNightHelper.setMode(DayNightHelper.DayNight.DAY);
                    setTheme(R.style.DayTheme);
                    refreshUI(true);
                }
                showAnimation();
            }
        });
    }

    /**
     * 展示一个切换动画
     */
    private void showAnimation() {
        final View decorView = getWindow().getDecorView();
        Bitmap cacheBitmap = getCacheBitmapFromView(decorView);
        if (decorView instanceof ViewGroup && cacheBitmap != null) {
            final View view = new View(this);
            view.setBackground(new BitmapDrawable(getResources(), cacheBitmap));
            ViewGroup.LayoutParams layoutParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            ((ViewGroup) decorView).addView(view, layoutParam);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
            objectAnimator.setDuration(300);
            objectAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    ((ViewGroup) decorView).removeView(view);
                }
            });
            objectAnimator.start();
        }
    }

    /**
     * 获取 View 的缓存视图
     *
     * @param view 对应的View
     * @return 对应View的缓存视图
     */
    private Bitmap getCacheBitmapFromView(View view) {
        final boolean drawingCacheEnabled = true;
        view.setDrawingCacheEnabled(drawingCacheEnabled);
        view.buildDrawingCache(drawingCacheEnabled);
        final Bitmap drawingCache = view.getDrawingCache();
        Bitmap bitmap;
        if (drawingCache != null) {
            bitmap = Bitmap.createBitmap(drawingCache);
            view.setDrawingCacheEnabled(false);
        } else {
            bitmap = null;
        }
        return bitmap;
    }

    /**
     * 刷新UI界面
     */
    private void refreshUI(boolean isDay) {
//        TypedValue background = new TypedValue();//背景色
//        TypedValue textColor = new TypedValue();//字体颜色
//        Resources.Theme theme = getTheme();
//        theme.resolveAttribute(R.attr.clockBackground, background, true);
//        theme.resolveAttribute(R.attr.clockTextColor, textColor, true);

//        mHeaderLayout.setBackgroundResource(background.resourceId);
//        for (RelativeLayout layout : mLayoutList) {
//            layout.setBackgroundResource(background.resourceId);
//        }
//        for (CheckBox checkBox : mCheckBoxList) {
//            checkBox.setBackgroundResource(background.resourceId);
//        }
//        for (TextView textView : mTextViewList) {
//            textView.setBackgroundResource(background.resourceId);
//        }

//        Resources resources = getResources();
//        for (TextView textView : mTextViewList) {
//            textView.setTextColor(resources.getColor(textColor.resourceId));
//        }

//        int childCount = mRecyclerView.getChildCount();
//        for (int childIndex = 0; childIndex < childCount; childIndex++) {
//            ViewGroup childView = (ViewGroup) mRecyclerView.getChildAt(childIndex);
//            childView.setBackgroundResource(background.resourceId);
//            View infoLayout = childView.findViewById(R.id.info_layout);
//            infoLayout.setBackgroundResource(background.resourceId);
//            TextView nickName = (TextView) childView.findViewById(R.id.tv_nickname);
//            nickName.setBackgroundResource(background.resourceId);
//            nickName.setTextColor(resources.getColor(textColor.resourceId));
//            TextView motto = (TextView) childView.findViewById(R.id.tv_motto);
//            motto.setBackgroundResource(background.resourceId);
//            motto.setTextColor(resources.getColor(textColor.resourceId));
//        }

        //让 RecyclerView 缓存在 Pool 中的 Item 失效
        //那么，如果是ListView，要怎么做呢？这里的思路是通过反射拿到 AbsListView 类中的 RecycleBin 对象，然后同样再用反射去调用 clear 方法
//        Class<RecyclerView> recyclerViewClass = RecyclerView.class;
//        try {
//            Field declaredField = recyclerViewClass.getDeclaredField("mRecycler");
//            declaredField.setAccessible(true);
//            Method declaredMethod = Class.forName(RecyclerView.Recycler.class.getName()).getDeclaredMethod("clear", (Class<?>[]) new Class[0]);
//            declaredMethod.setAccessible(true);
//            declaredMethod.invoke(declaredField.get(mRecyclerView), new Object[0]);
//            RecyclerView.RecycledViewPool recycledViewPool = mRecyclerView.getRecycledViewPool();
//            recycledViewPool.clear();
//
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }

        TypedValue colorPrimary = new TypedValue();
        Resources.Theme theme = getTheme();
        theme.resolveAttribute(R.attr.colorPrimary, colorPrimary, true);
        //更新Toolbar的UI
        mToolbar.setBackgroundResource(colorPrimary.resourceId);
        //更新TabLayout的UI
        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mTabLayout.setBackgroundResource(colorPrimary.resourceId);

        View navigationHeader = navigationView.getHeaderView(0);
        if (isDay) {
            navigationHeader.setBackgroundResource(R.drawable.side_nav_bar_day);
        } else {
            navigationHeader.setBackgroundResource(R.drawable.side_nav_bar_night);
        }

        refreshStatusBar();
    }

    /**
     * 刷新 StatusBar
     */
    private void refreshStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = getTheme();
            theme.resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
            getWindow().setStatusBarColor(ContextCompat.getColor(mContext, typedValue.resourceId));
        }
    }

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void initViewPager(String title, String type) {
        tabList = mRealm.where(TabDTO.class)
                .equalTo("firstType", type)
                .findAll();
        if (tabList != null) {
            new ItemViewPager(mContext, title, tabList).initData();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_person) {
            initViewPager("人物", TYPE_PERSON);
        } else if (id == R.id.nav_house) {
            initViewPager("家族", TYPE_HOUSE);
        } else if (id == R.id.nav_history) {
            initViewPager("历史", TYPE_HISTORY);
        } else if (id == R.id.nav_castles) {
            initViewPager("城堡", TYPE_CASTLE);
        } else {
            return true;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - mExitTime > 2000) {
                ToastUtil.show(this, "再按一次退出");
                mExitTime = System.currentTimeMillis();
            } else {
                ToastUtil.cancel();
                finish();
            }
        }
        return true;
    }
}
