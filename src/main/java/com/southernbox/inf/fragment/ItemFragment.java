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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.southernbox.inf.R;
import com.southernbox.inf.adapter.MainAdapter;
import com.southernbox.inf.bean.ContentBean;
import com.southernbox.inf.util.CacheUtils;
import com.southernbox.inf.util.Dp2PxUtil;
import com.southernbox.inf.util.MyStringRequest;
import com.southernbox.inf.util.ServerAPI;

/**
 * Fragment界面
 */
public class ItemFragment extends Fragment {
    private Context mContext;
    private String jsonUrl;
    private View rootView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RequestQueue mQueue;
    private MainAdapter adapter;
    public ContentBean jsonBean;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        jsonUrl = getArguments().getString("json_url");
        mQueue = Volley.newRequestQueue(mContext);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = LayoutInflater.from(mContext).inflate(R.layout.fragment_home, null);

            initSwipeRefreshLayout();
            initRecyclerView();
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }

    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.home_refresh_srl);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);
        mSwipeRefreshLayout.setProgressViewOffset(false, Dp2PxUtil.getPx(mContext, -50), Dp2PxUtil.getPx(mContext, 20));
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                                     @Override
                                                     public void onRefresh() {
                                                         loadDatas();
                                                     }
                                                 }
        );
    }

    private void loadDatas() {
        // 优先加载本地缓存数据
        String cacheData = CacheUtils.getString(mContext, jsonUrl,
                null);
        if (!TextUtils.isEmpty(cacheData)) {
            processData(cacheData);
        }

        // 从网络加载数据
        MyStringRequest stringRequest = new MyStringRequest(ServerAPI.BASE_URL + jsonUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                CacheUtils.putString(mContext, jsonUrl, s);
                if (!TextUtils.isEmpty(s)) {
                    processData(s);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
        mQueue.add(stringRequest);
    }

    private void processData(String json) {
        Gson gson = new Gson();
        this.jsonBean = gson.fromJson(json, ContentBean.class);
        if (jsonBean != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.setDatas(jsonBean.data);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        }
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.home_content_rv);

        // 设置布局管理器，否则会报错
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        adapter = new MainAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        // 加载数据
        loadDatas();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


}

















