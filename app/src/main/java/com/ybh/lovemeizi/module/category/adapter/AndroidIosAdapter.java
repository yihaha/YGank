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
import com.ybh.lovemeizi.module.home.ui.WebActivity;
import com.ybh.lovemeizi.utils.DateUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by y on 2016/5/10.
 */
public class AndroidIosAdapter extends RecyclerView.Adapter<AndroidIosAdapter.ViewHolder> {
    private List<GankData> mGankDatas;

    public AndroidIosAdapter(List<GankData> gankDatas) {
        this.mGankDatas = gankDatas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fr_and_ios_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GankData gankData = mGankDatas.get(position);
        holder.itemTitle.setText(gankData.desc);
        holder.itemName.setText(gankData.who);
        holder.itemDate.setText(DateUtil.onDate2String(gankData.publishedAt,"yyyy-MM-dd"));
    }

    @Override
    public int getItemCount() {
        return mGankDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.desc_view)
        TextView itemTitle;
        @Bind(R.id.name_view)
        TextView itemName;
        @Bind(R.id.dateview)
        TextView itemDate;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.fr_item_layout)
        void toWebPage(View view){
            GankData gankData = mGankDatas.get(getLayoutPosition());
            Intent intent = new Intent(view.getContext(), WebActivity.class);
            intent.putExtra(Contant.SHARE_URL, gankData.url);
            intent.putExtra(Contant.SHARE_DESC, gankData.type);
            intent.putExtra(Contant.SHARE_TITLE, gankData.desc);
            view.getContext().startActivity(intent);

        }

    }
}
