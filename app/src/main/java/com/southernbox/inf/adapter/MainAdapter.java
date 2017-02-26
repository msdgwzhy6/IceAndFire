package com.southernbox.inf.adapter;

import android.content.Context;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.southernbox.inf.R;
import com.southernbox.inf.activity.DetailActivity;
import com.southernbox.inf.activity.MainActivity;
import com.southernbox.inf.entity.ContentDTO;
import com.southernbox.inf.util.ServerAPI;

import java.util.List;

/**
 * Created by SouthernBox on 2016/3/27.
 * 首页列表适配器
 */

public class MainAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private MainActivity mainActivity;
    private List<ContentDTO> mList;

    public MainAdapter(Context context, List<ContentDTO> list) {
        this.mContext = context;
        this.mList = list;
        mainActivity = (MainActivity) mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.item_list, parent, false);

        MyViewHolder holder = new MyViewHolder(rootView);
        holder.ivImg = (ImageView) rootView.findViewById(R.id.iv_img);
        holder.tvName = (TextView) rootView.findViewById(R.id.tv_name);
        holder.tvDesc = (TextView) rootView.findViewById(R.id.tv_desc);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MyViewHolder viewHolder = (MyViewHolder) holder;
        final ContentDTO content = mList.get(position);

        viewHolder.tvName.setText(content.getName());
        viewHolder.tvDesc.setText(content.getIntro());

        Glide
                .with(mContext)
                .load(ServerAPI.BASE_URL + content.getImg())
                .override(480, 270)
                .crossFade()
                .into(viewHolder.ivImg);

        final View itemRoot = viewHolder.itemView;
        itemRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(content, viewHolder);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void onItemClick(ContentDTO content, MyViewHolder holder) {
        Pair[] pairs = new Pair[]{new Pair(holder.ivImg, "tran_01")};
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(mainActivity, pairs);

        DetailActivity.show(
                mContext,
                options,
                content.getName(),
                content.getImg(),
                content.getHtml()
        );
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImg;
        TextView tvName;
        TextView tvDesc;

        MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
