package com.ybh.lovemeizi.module.home.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ybh.lovemeizi.R;
import com.ybh.lovemeizi.model.GankData;
import com.ybh.lovemeizi.utils.StringUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by y on 2016/5/5.
 */
public class DetailAdapter extends AnimRecyclerViewAdapter<DetailAdapter.ViewHolder> {
    private List<GankData> mDatas;

    public DetailAdapter(List<GankData> gankDatas) {
        this.mDatas = gankDatas;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_recycleview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final GankData gankData = mDatas.get(position);
        if (position==0){  //在第一个要显示分类标题
            holder.cateGory_title.setVisibility(View.VISIBLE);
        }else {
            //跟前一个不同时才显示分类标题
            boolean isShow = mDatas.get(position - 1).type.equals(gankData.type);
            if (isShow){
                holder.cateGory_title.setVisibility(View.GONE);
            }else {
                holder.cateGory_title.setVisibility(View.VISIBLE);
            }
        }

        //作者字体加入了样式
        SpannableStringBuilder spanBuilder = new SpannableStringBuilder(gankData.desc)
                .append(StringUtil.setStringStyle(holder.cateGory_title.getContext()
                        ," (via. "+gankData.who+") ",R.style.YTextStyle));
        holder.cateGory_title.setText(gankData.type);
        //加这句后才有效果
        CharSequence desc = spanBuilder.subSequence(0, spanBuilder.length());
        holder.detail_desc.setText(desc);
        showItemAnim(holder.detail_desc,position);//执行动画
        holder.detail_desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(holder.detail_desc.getContext(),gankData.desc,0).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.category_text)
        TextView cateGory_title;
        @Bind(R.id.detail_desc)
        TextView detail_desc;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }


}
