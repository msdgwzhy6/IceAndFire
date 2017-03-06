package com.southernbox.inf.fragment;


import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.southernbox.inf.R;
import com.southernbox.inf.adapter.MainAdapter;
import com.southernbox.inf.entity.ContentDTO;
import com.southernbox.inf.util.DisplayUtil;
import com.southernbox.inf.util.RequestServes;
import com.southernbox.inf.util.ServerAPI;
import com.southernbox.inf.util.ToastUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by SouthernBox on 2016/3/27.
 * 首页Fragment
 */

public class MainFragment extends Fragment {
    private Context mContext;
    private String firstType;
    private String secondType;
    private View rootView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private MainAdapter adapter;
    private List<ContentDTO> contentList = new ArrayList<>();
    private Realm mRealm;
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
        rootView = LayoutInflater.from(mContext)
                .inflate(R.layout.fragment_main, container, false);
        initRecyclerView();
        initSwipeRefreshLayout();
        showData();
        return rootView;
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new MainAdapter(getActivity(), contentList);
        mRecyclerView.setAdapter(adapter);
    }

    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.switch_refresh_layout);
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
    }

    /**
     * 加载网络数据
     */
    private void loadData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerAPI.BASE_URL)
                //增加返回值为Gson的支持(以实体类返回)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestServes requestServes = retrofit.create(RequestServes.class);
        Call<List<ContentDTO>> call = requestServes.getContent();
        call.enqueue(new Callback<List<ContentDTO>>() {
            @Override
            public void onResponse(Call<List<ContentDTO>> call, retrofit2.Response<List<ContentDTO>> response) {
                mSwipeRefreshLayout.setRefreshing(false);
                List<ContentDTO> list = response.body();
                if (list != null) {
                    //缓存到数据库
                    mRealm.beginTransaction();
                    mRealm.copyToRealmOrUpdate(list);
                    mRealm.commitTransaction();
                }
                showData();
            }

            @Override
            public void onFailure(Call<List<ContentDTO>> call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
                ToastUtil.show(mContext, "网络连接失败");
            }
        });
    }

    /**
     * 展示数据
     */
    private void showData() {
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
            mSwipeRefreshLayout.setBackgroundResource(pagerBackground.resourceId);
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

















