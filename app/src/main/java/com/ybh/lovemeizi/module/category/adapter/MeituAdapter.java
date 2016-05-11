package com.ybh.lovemeizi.module.category.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ybh.lovemeizi.Contant;
import com.ybh.lovemeizi.R;
import com.ybh.lovemeizi.model.gankio.GankData;
import com.ybh.lovemeizi.module.home.ui.ShowPicActivity;
import com.ybh.lovemeizi.utils.DateUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by y on 2016/5/10.
 */
public class MeituAdapter extends RecyclerView.Adapter<MeituAdapter.ViewHolder> {
    private List<GankData> mGankDatas;

    public MeituAdapter(List<GankData> gankDatas) {
        this.mGankDatas = gankDatas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fr_meitu_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GankData gankData = mGankDatas.get(position);
        Glide.with(holder.mImg.getContext()).load(gankData.url).into(holder.mImg);
        holder.mImgDate.setText(DateUtil.onDate2String(gankData.publishedAt));
    }

    @Override
    public int getItemCount() {
        return mGankDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.fr_meitu_img)
        ImageView mImg;
        @Bind(R.id.fr_meitu_date)
        TextView mImgDate;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.meitu_item_layout)
        void toPicActivity(View view){
            GankData gankData = mGankDatas.get(getLayoutPosition());
            Intent intent = new Intent(view.getContext(), ShowPicActivity.class);
            intent.putExtra(Contant.Y_GANKDATA,gankData);
            view.getContext().startActivity(intent);
        }
    }
}
