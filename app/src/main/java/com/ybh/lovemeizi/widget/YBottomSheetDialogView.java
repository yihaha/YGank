package com.ybh.lovemeizi.widget;

import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ybh.lovemeizi.R;

/**
 * Created by y on 2016/4/25.
 */
public class YBottomSheetDialogView {
    private String[] shareNames = {"微信", "朋友圈", "QQ", "QQ空间", "新浪微博"};
    private int[] shareImgs = {R.mipmap.ssdk_oks_classic_wechat
            , R.mipmap.ssdk_oks_classic_wechatmoments
            , R.mipmap.ssdk_oks_classic_qq
            , R.mipmap.ssdk_oks_classic_qzone
            , R.mipmap.ssdk_oks_classic_sinaweibo};

    private BottomClickListener bottomClickListener;
    private final BottomSheetDialog bottomDialog;

    public YBottomSheetDialogView(Context context, int dayNightMode) {
        bottomDialog = new BottomSheetDialog(context);
//        bottomDialog.getDelegate().setLocalNightMode(dayNightMode);
        View view = LayoutInflater.from(context).inflate(R.layout.sharelayout, null);

        //取消弹出
        view.findViewById(R.id.cancel_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomClickListener!=null){
                    bottomClickListener.cancleShare();
                }
            }
        });

        RecyclerView mRecyView = (RecyclerView) view.findViewById(R.id.srecyview);
        //水平滚动的RecycleView
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(context,5,GridLayoutManager.VERTICAL,false);
        mRecyView.setLayoutManager(linearLayoutManager);
        mRecyView.setAdapter(new MyShareAdapter());
        bottomDialog.setContentView(view);
        bottomDialog.show();
    }

    public interface BottomClickListener{
        void cancleShare();
        void onItemcListener(String  item);
    }

    public void bottomClickCallback(BottomClickListener bottomClickListener){
        this.bottomClickListener=bottomClickListener;
    }

    public class MyShareAdapter extends RecyclerView.Adapter<ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.share_item, null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.shareImg.setImageResource(shareImgs[position]);
            holder.shareName.setText(shareNames[position]);
            holder.shareImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bottomClickListener!=null){
                        bottomClickListener.onItemcListener(shareNames[position]);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return shareImgs.length;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView shareImg;
        TextView shareName;

        public ViewHolder(View itemView) {
            super(itemView);
            shareName = (TextView) itemView.findViewById(R.id.category_text);
            shareImg = (ImageView) itemView.findViewById(R.id.category_img);

        }
    }

    public void dismissDialog(){
        bottomDialog.dismiss();
    }
}
