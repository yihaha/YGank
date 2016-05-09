package com.ybh.lovemeizi.module.home.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ybh.lovemeizi.Contant;
import com.ybh.lovemeizi.R;
import com.ybh.lovemeizi.model.gankio.GankData;
import com.ybh.lovemeizi.module.home.ui.WebActivity;
import com.ybh.lovemeizi.utils.StringUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by y on 2016/5/5.
 */
public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolder> {
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

        //'作者'字体加入了样式
        SpannableStringBuilder spanBuilder = new SpannableStringBuilder(gankData.desc)
                .append(StringUtil.setStringStyle(holder.cateGory_title.getContext()
                        ," (via. "+gankData.who+") ",R.style.YTextStyle));
        holder.cateGory_title.setText(gankData.type);
        //加这句后才有效果
        CharSequence desc = spanBuilder.subSequence(0, spanBuilder.length());
        holder.detail_desc.setText(desc);
//        showItemAnim(holder.detail_desc,position);//执行动画

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
//        @Bind(R.id.detail_ll)
//        LinearLayout descParentView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.detail_ll)
        void goWeb(View view){
            GankData gankData = mDatas.get(getLayoutPosition());
            Intent intent = new Intent(view.getContext(), WebActivity.class);
//            intent.putExtra("url", gankData.url);
//            intent.putExtra("desc",gankData.desc);
            intent.putExtra(Contant.Y_GANKDATA,gankData);
            view.getContext().startActivity(intent);
        }

    }


}
