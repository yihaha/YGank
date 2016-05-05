package com.ybh.lovemeizi.module.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ybh.lovemeizi.Contant;
import com.ybh.lovemeizi.R;
import com.ybh.lovemeizi.model.GankData;
import com.ybh.lovemeizi.module.home.ui.DetailActivity;
import com.ybh.lovemeizi.utils.DateUtil;
import com.ybh.lovemeizi.module.home.ui.MainActivity;
import com.ybh.lovemeizi.module.home.ui.ShowPicActivity;

import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by y on 2016/4/12.
 */
public class MainRecyclAdapter extends RecyclerView.Adapter<MainRecyclAdapter.MeiziListViewHolder> {
    public Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<GankData> mList;
    private MainActivity mActivity;

    public MainRecyclAdapter(Context context) {
        this.mContext = context;
        mActivity = (MainActivity) context;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void setRefresh(List<GankData> dataList) {
        this.mList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public MeiziListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.main_recyc_item, parent, false);
        return new MeiziListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MeiziListViewHolder holder, int position) {
        final GankData gankData = mList.get(position);
        final Date publishedAt = gankData.publishedAt;
        holder.setView(gankData);
        holder.descView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, DetailActivity.class);
                intent.putExtra(Contant.Y_DATE,publishedAt.getTime()+"");
                mContext.startActivity(intent);
                mActivity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                Toast.makeText(mActivity,"将打开视频",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class MeiziListViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.meizi_img)
        ImageView meizi_img;
        @Bind(R.id.meizi_title)
        TextView meizi_title;
        @Bind(R.id.time_id)
        TextView createTime;
        @Bind(R.id.null_view)
        View descView;
        private String url;

        public MeiziListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            meizi_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, ShowPicActivity.class);
                    intent.putExtra("imgUrl", url);
                    mContext.startActivity(intent);
                    mActivity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }
            });
        }

        public void setView(GankData gankData) {
            url = gankData.url;
            meizi_title.setText(gankData.desc);
//            createTime.setText(DateUtil.onDate2String(gankData.createdAt));//这个创建时间可能不准
            createTime.setText(DateUtil.onDate2String(gankData.publishedAt));
            Glide.with(mContext)
                    .load(gankData.url)
                    .centerCrop()
                    .placeholder(R.mipmap.defaultmeizi)
                    .dontAnimate()
                    .into(meizi_img);
        }

    }
}
