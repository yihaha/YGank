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
import com.ybh.lovemeizi.module.YBaseLoadingAdapter;
import com.ybh.lovemeizi.module.home.ui.ShowPicActivity;
import com.ybh.lovemeizi.utils.DateUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by y on 2016/5/10.
 */
public class MeituAdapter extends YBaseLoadingAdapter<GankData> {

    public MeituAdapter(RecyclerView recyclerView,List<GankData> gankDatas) {
        super(recyclerView,gankDatas);
    }

    @Override
    public RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fr_meitu_item, parent, false);
        return new MeiziHolder(view);
    }

    @Override
    public void onBindNormalViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        MeiziHolder meiziHolder= (MeiziHolder) viewHolder;
        GankData gankData = mList.get(position);
        Glide.with(meiziHolder.mImg.getContext()).load(gankData.url).into(meiziHolder.mImg);
        meiziHolder.mImgDate.setText(DateUtil.onDate2String(gankData.publishedAt));
    }


    class MeiziHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.fr_meitu_img)
        ImageView mImg;
        @Bind(R.id.fr_meitu_date)
        TextView mImgDate;


        public MeiziHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.meitu_item_layout)
        void toPicActivity(View view){
            GankData gankData = mList.get(getLayoutPosition());
            Intent intent = new Intent(view.getContext(), ShowPicActivity.class);
            intent.putExtra(Contant.Y_GANKDATA,gankData);
            view.getContext().startActivity(intent);
        }
    }
}
