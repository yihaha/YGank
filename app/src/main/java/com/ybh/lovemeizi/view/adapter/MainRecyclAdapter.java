package com.ybh.lovemeizi.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ybh.lovemeizi.R;
import com.ybh.lovemeizi.model.GankData;
import com.ybh.lovemeizi.utils.DateUtil;
import com.ybh.lovemeizi.view.activity.MainActivity;
import com.ybh.lovemeizi.view.activity.ShowPicActivity;

import java.util.List;


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
        holder.setView(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class MeiziListViewHolder extends RecyclerView.ViewHolder {
        ImageView meizi_img;
        TextView meizi_title;
        TextView createTime;
        private String url;

        public MeiziListViewHolder(View itemView) {
            super(itemView);
            meizi_img = (ImageView) itemView.findViewById(R.id.meizi_img);
            meizi_title = (TextView) itemView.findViewById(R.id.meizi_title);
            createTime = (TextView) itemView.findViewById(R.id.time_id);
            itemView.findViewById(R.id.null_view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(mActivity, ShowPicActivity.class);
//                    intent.putExtra("imgUrl", url);
//                    mContext.startActivity(intent);
//                    mActivity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                    Toast.makeText(mActivity,"将打开视频",Toast.LENGTH_SHORT).show();
                }
            });

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
            createTime.setText(DateUtil.onDate2String(gankData.createdAt));
            Glide.with(mContext)
                    .load(gankData.url)
                    .centerCrop()
                    .dontAnimate()
                    .into(meizi_img);
        }

    }
}
