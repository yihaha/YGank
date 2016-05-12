package com.ybh.lovemeizi.module.category.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ybh.lovemeizi.Contant;
import com.ybh.lovemeizi.R;
import com.ybh.lovemeizi.model.gankio.GankData;
import com.ybh.lovemeizi.module.YBaseLoadingAdapter;
import com.ybh.lovemeizi.module.home.ui.WebActivity;
import com.ybh.lovemeizi.utils.DateUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by y on 2016/5/10.
 */
public class AndroidIosAdapter extends YBaseLoadingAdapter<GankData> {

    public AndroidIosAdapter(RecyclerView recyclerView,List<GankData> gankDatas) {
        super(recyclerView,gankDatas);
    }

    @Override
    public RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fr_and_ios_item, parent, false);
        return new AnIosHolder(view);
    }

    @Override
    public void onBindNormalViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        AnIosHolder anIosHolder= (AnIosHolder) viewHolder;
        GankData gankData = mList.get(position);
        anIosHolder.itemTitle.setText(gankData.desc);
        anIosHolder.itemName.setText(gankData.who);
        anIosHolder.itemDate.setText(DateUtil.onDate2String(gankData.publishedAt));
    }


    class AnIosHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.desc_view)
        TextView itemTitle;
        @Bind(R.id.name_view)
        TextView itemName;
        @Bind(R.id.dateview)
        TextView itemDate;

        public AnIosHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.fr_item_layout)
        void toWebPage(View view){
            GankData gankData = mList.get(getLayoutPosition());
            Intent intent = WebActivity.newIntent(view.getContext(), gankData.desc, gankData.type, gankData.url);
            view.getContext().startActivity(intent);

        }

    }
}
