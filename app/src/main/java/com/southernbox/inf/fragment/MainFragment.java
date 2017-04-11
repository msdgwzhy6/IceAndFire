package com.southernbox.inf.fragment;


import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.southernbox.inf.R;
import com.southernbox.inf.adapter.MainAdapter;
import com.southernbox.inf.entity.ContentDTO;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by SouthernBox on 2016/3/27.
 * 首页Fragment
 */

public class MainFragment extends Fragment {
    private Context mContext;
    private String firstType;
    private String secondType;
    private MainAdapter adapter;
    private List<ContentDTO> contentList = new ArrayList<>();
    private Realm mRealm;
    private FrameLayout flContent;
    private RecyclerView mRecyclerView;

    /**
     * 获取对应的首页Fragment
     *
     * @param firstType  一级分类
     * @param secondType 二级分类
     * @return 对应的Fragment
     */
    public static MainFragment newInstance(String firstType, String secondType) {
        MainFragment fragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putString("firstType", firstType);
        bundle.putString("secondType", secondType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        Bundle bundle = getArguments();
        firstType = bundle.getString("firstType");
        secondType = bundle.getString("secondType");
        Realm.init(getContext());
        RealmConfiguration realmConfig = new RealmConfiguration.Builder().build();
        mRealm = Realm.getInstance(realmConfig);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        initView(rootView);
        showData();
        return rootView;
    }

    private void initView(View rootView) {
        flContent = (FrameLayout) rootView.findViewById(R.id.fl_content);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new MainAdapter(getActivity(), contentList);
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * 展示数据
     */
    public void showData() {
        if (isRemoving()) {
            return;
        }
        //从本地数据库获取
        contentList.clear();
        final List<ContentDTO> cacheList = mRealm
                .where(ContentDTO.class)
                .equalTo("firstType", firstType)
                .equalTo("secondType", secondType)
                .findAll();
        contentList.clear();
        contentList.addAll(cacheList);
        adapter.notifyDataSetChanged();
    }

    public void refreshUI() {
        if (mContext != null) {
            Resources.Theme theme = mContext.getTheme();
            TypedValue pagerBackground = new TypedValue();
            theme.resolveAttribute(R.attr.pagerBackground, pagerBackground, true);
            TypedValue colorBackground = new TypedValue();
            theme.resolveAttribute(R.attr.colorBackground, colorBackground, true);
            TypedValue darkTextColor = new TypedValue();
            theme.resolveAttribute(R.attr.darkTextColor, darkTextColor, true);

            //更新背景颜色
            flContent.setBackgroundResource(pagerBackground.resourceId);
            //更新Item的背景及字体颜色
            int childCount = mRecyclerView.getChildCount();
            for (int position = 0; position < childCount; position++) {
                ViewGroup childView = (ViewGroup) mRecyclerView.getChildAt(position);
                View vContent = childView.findViewById(R.id.ll_content);
                vContent.setBackgroundResource(colorBackground.resourceId);
                TextView tvName = (TextView) childView.findViewById(R.id.tv_name);
                tvName.setTextColor(ContextCompat.getColor(mContext, darkTextColor.resourceId));
                TextView tvDesc = (TextView) childView.findViewById(R.id.tv_desc);
                tvDesc.setTextColor(ContextCompat.getColor(mContext, darkTextColor.resourceId));
            }

            //让 RecyclerView 缓存在 Pool 中的 Item 失效
            Class<RecyclerView> recyclerViewClass = RecyclerView.class;
            try {
                Field declaredField = recyclerViewClass.getDeclaredField("mRecycler");
                declaredField.setAccessible(true);
                Method declaredMethod = Class.forName(RecyclerView.Recycler.class.getName())
                        .getDeclaredMethod("clear", new Class[0]);
                declaredMethod.setAccessible(true);
                declaredMethod.invoke(declaredField.get(mRecyclerView), new Object[0]);
                RecyclerView.RecycledViewPool recycledViewPool = mRecyclerView.getRecycledViewPool();
                recycledViewPool.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

















