package com.ybh.lovemeizi.module.category.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ybh.lovemeizi.R;
import com.ybh.lovemeizi.YApp;
import com.ybh.lovemeizi.model.kanzhihu.KanzhihuBean;
import com.ybh.lovemeizi.module.home.ui.WebActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by y on 2016/5/31.
 */
public class ZhihuAdapterCopy extends RecyclerView.Adapter {
    private List<KanzhihuBean> mList;

    public ZhihuAdapterCopy(List<KanzhihuBean> zhihuList) {
        this.mList = zhihuList;
    }

    public void setList(List<KanzhihuBean> zhihuList){
        this.mList=zhihuList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.zhihuf_item, parent, false);
        return new ZhihuHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ZhihuHolder zhihuHolder = (ZhihuHolder) holder;
        KanzhihuBean zhihu = mList.get(position);
        Glide.with(zhihuHolder.avator_icon.getContext())
                .load(zhihu.avatar)
//                .placeholder(R.mipmap.default_avator)
                .into(zhihuHolder.avator_icon);
        zhihuHolder.avator_name.setText(String.format(YApp.yContext.getResources().getString(R.string.zhiitemname), zhihu.authorname));
        zhihuHolder.voteCount.setText(zhihu.vote);
        zhihuHolder.question.setText(zhihu.title);
        zhihuHolder.answer.setText(zhihu.summary);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ZhihuHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.avator_icon)
        CircleImageView avator_icon;
        @Bind(R.id.avator_name)
        TextView avator_name;
        @Bind(R.id.vote)
        TextView voteCount;
        @Bind(R.id.question)
        TextView question;
        @Bind(R.id.answer)
        TextView answer;

        public ZhihuHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.zhiitem_layout)
        void toAnswerPage(View view) {
            //因为有头轮播布局,所以减1
            KanzhihuBean bean = mList.get(getLayoutPosition()-1);
//            String url = String.format(YApp.yContext.getResources()
//                    .getString(R.string.zhihuAnswerUrl), bean.questionid, bean.answerid);

            String url = String.format("http://www.zhihu.com/question/%s/answer/%s", bean.questionid, bean.answerid);
            Intent intent = WebActivity.newIntent(view.getContext(), bean.title, bean.summary, url);
            view.getContext().startActivity(intent);
        }
    }
}
