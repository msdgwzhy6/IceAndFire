package com.southernbox.inf.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.southernbox.inf.R;
import com.southernbox.inf.adapter.MainAdapter;
import com.southernbox.inf.entity.ContentDTO;
import com.southernbox.inf.util.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by SouthernBox on 2016/3/27.
 * 首页Fragment
 */

public class ItemFragment extends Fragment {
    private Context mContext;
    private String type;
    private View rootView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private MainAdapter adapter;
    public List<ContentDTO> contentList = new ArrayList<>();
    private Realm mRealm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        type = getArguments().getString("type");
        Realm.init(getContext());
        RealmConfiguration realmConfig = new RealmConfiguration.Builder().build();
        mRealm = Realm.getInstance(realmConfig);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = LayoutInflater.from(mContext)
                    .inflate(R.layout.fragment_home, container, false);
            initRecyclerView();
            initSwipeRefreshLayout();
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.home_content_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new MainAdapter(getActivity(), contentList);
        recyclerView.setAdapter(adapter);
    }

    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.home_refresh_srl);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);
        mSwipeRefreshLayout.setProgressViewOffset(false,
                DisplayUtil.getPx(mContext, -50), DisplayUtil.getPx(mContext, 20));
        SwipeRefreshLayout.OnRefreshListener refreshListener =
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        loadData();
                    }
                };
        mSwipeRefreshLayout.setOnRefreshListener(refreshListener);
        refreshListener.onRefresh();
    }

    private void loadData() {
        mSwipeRefreshLayout.setRefreshing(false);

        //加载本地缓存数据
        contentList.clear();
        List<ContentDTO> cacheList = mRealm
                .where(ContentDTO.class)
                .equalTo("secondType",type)
                .findAll();
        contentList.clear();
        contentList.addAll(cacheList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


}

















