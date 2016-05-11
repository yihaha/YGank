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
import com.ybh.lovemeizi.YApp;
import com.ybh.lovemeizi.model.kanzhihu.KanzhihuBean;
import com.ybh.lovemeizi.module.home.ui.WebActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by y on 2016/5/10.
 */
public class ZhihuAdapter extends RecyclerView.Adapter<ZhihuAdapter.ViewHolder> {
    private List<KanzhihuBean> mZhihuList;

    public ZhihuAdapter(List<KanzhihuBean> zhihuList) {
        this.mZhihuList = zhihuList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.zhihuf_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        KanzhihuBean zhihu = mZhihuList.get(position);
        Glide.with(holder.avator_icon.getContext())
                .load(zhihu.avatar)
                .into(holder.avator_icon);
        holder.avator_name.setText(String.format(YApp.yContext.getResources().getString(R.string.zhiitemname), zhihu.authorname));
        holder.voteCount.setText(zhihu.vote);
        holder.question.setText(zhihu.title);
        holder.answer.setText(zhihu.summary);
    }

    @Override
    public int getItemCount() {
        return mZhihuList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.zhiitem_layout)
        void toAnswerPage(View view) {
            KanzhihuBean bean = mZhihuList.get(getLayoutPosition());
            Intent intent = new Intent(view.getContext(), WebActivity.class);
//            String url = String.format(YApp.yContext.getResources()
//                    .getString(R.string.zhihuAnswerUrl), bean.questionid, bean.answerid);

            String url = String.format("http://www.zhihu.com/question/%s/answer/%s", bean.questionid, bean.answerid);
            intent.putExtra(Contant.SHARE_URL, url);
            intent.putExtra(Contant.SHARE_DESC, bean.summary);
            intent.putExtra(Contant.SHARE_TITLE, bean.title);
            view.getContext().startActivity(intent);
        }
    }
}
