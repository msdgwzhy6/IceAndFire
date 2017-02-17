package com.southernbox.inf.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.southernbox.inf.R;
import com.southernbox.inf.adapter.MainAdapter;
import com.southernbox.inf.entity.Content;
import com.southernbox.inf.util.Dp2PxUtil;
import com.southernbox.inf.util.RequestServes;
import com.southernbox.inf.util.ServerAPI;
import com.southernbox.inf.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by SouthernBox on 2016/3/27.
 * 首页Fragment
 */

public class ItemFragment extends Fragment {
    private Context mContext;
    private String jsonUrl;
    private View rootView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private MainAdapter adapter;
    public List<Content> contentList = new ArrayList<>();
    private Realm mRealm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        jsonUrl = getArguments().getString("json_url");
        if (!TextUtils.isEmpty(jsonUrl) && jsonUrl.length() > 0) {
            jsonUrl = jsonUrl.substring(1, jsonUrl.length());
        }
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
                Dp2PxUtil.getPx(mContext, -50), Dp2PxUtil.getPx(mContext, 20));
        SwipeRefreshLayout.OnRefreshListener refreshListener =
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        loadData();
                    }
                };
        mSwipeRefreshLayout.setOnRefreshListener(refreshListener);
        mSwipeRefreshLayout.setRefreshing(true);
        refreshListener.onRefresh();
    }

    private void loadData() {
        if (TextUtils.isEmpty(jsonUrl)) {
            mSwipeRefreshLayout.setRefreshing(false);
            return;
        }

        //加载本地缓存数据
        contentList.clear();
        List<Content> cacheList = mRealm
                .where(Content.class)
                .findAll();
        contentList.addAll(cacheList);
        adapter.notifyDataSetChanged();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerAPI.BASE_URL + "/")
                //增加返回值为String的支持
                .addConverterFactory(ScalarsConverterFactory.create())
//                //增加返回值为Gson的支持(以实体类返回)
//                .addConverterFactory(GsonConverterFactory.create())
//                //增加返回值为Oservable<T>的支持
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        RequestServes requestServes = retrofit.create(RequestServes.class);
        Call<String> call = requestServes.getPerson(jsonUrl);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                mSwipeRefreshLayout.setRefreshing(false);
                String responseString = response.body();

                Gson gson = new Gson();
                List<Content> list = gson.fromJson(responseString,
                        new TypeToken<List<Content>>() {
                        }.getType());
                if (list != null) {
                    contentList.clear();
                    contentList.addAll(list);
                }

                mRealm.beginTransaction();
                mRealm.copyToRealmOrUpdate(contentList);
                mRealm.commitTransaction();

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
                ToastUtil.show(mContext, "网络连接失败");
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


}

















