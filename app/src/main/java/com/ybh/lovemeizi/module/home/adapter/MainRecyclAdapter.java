package com.ybh.lovemeizi.module.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ybh.lovemeizi.R;
import com.ybh.lovemeizi.model.gankio.FewDayData;
import com.ybh.lovemeizi.module.home.ui.DetailActivity;
import com.ybh.lovemeizi.module.home.ui.MainActivity;
import com.ybh.lovemeizi.module.home.ui.ShowPicActivity;
import com.ybh.lovemeizi.utils.DateUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by y on 2016/4/12.
 */
public class MainRecyclAdapter extends RecyclerView.Adapter<MainRecyclAdapter.MeiziListViewHolder> {
    public Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<FewDayData.YData> mList;
    private MainActivity mActivity;

    public MainRecyclAdapter(Context context) {
        this.mContext = context;
        mActivity = (MainActivity) context;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void setRefresh(List<FewDayData.YData> dataList) {
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
        final FewDayData.YData yData = mList.get(position);
        holder.setView(yData);
        holder.descView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, DetailActivity.class);
                intent.putExtra("ydata", yData);
                mContext.startActivity(intent);
                mActivity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
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
                    FewDayData.YData yData = mList.get(getLayoutPosition());
                    if (yData==null){
                        return;
                    }
                    Intent intent = ShowPicActivity.newIntent(mActivity
                            , yData.imgUrl
                            , yData.desc
                            , DateUtil.onDate2String(yData.publishedAt, "yyyy/MM/dd"));
                    mContext.startActivity(intent);

//                    ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat
//                                                             .makeSceneTransitionAnimation(mActivity
//                                                            , meizi_img, ShowPicActivity.MEIZI_IMG);
//
//                    ActivityCompat.startActivity(mActivity,intent,activityOptionsCompat.toBundle());
                    mActivity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }
            });
        }

        public void setView( FewDayData.YData yData) {
            url = yData.imgUrl;
            meizi_title.setText(yData.desc);
            createTime.setText(DateUtil.onDate2String(yData.publishedAt));
            Glide.with(mContext)
                    .load(url)
                    .centerCrop()
                    .placeholder(R.mipmap.default_ygank)
                    .dontAnimate()
                    .into(meizi_img);
        }

    }
}
