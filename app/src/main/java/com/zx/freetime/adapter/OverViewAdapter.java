package com.zx.freetime.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zx.freetime.R;

import java.util.ArrayList;

import static com.zx.freetime.R.id.tv;

/**
 * Created by zhangxin on 2017/3/22 0022.
 * <p>
 * Description :
 */

public class OverViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    ArrayList<String> list;

    //设计一下:1个的情况:美图/电影/闲聊;
    //2个的情况下:头条/科技新闻;
    private static final int TYPE_TITLE = 1; // title
    private static final int TYPE_ONE = 2;// 一张图
    private static final int TYPE_TWO = 3;// 二张图
    private static final int TYPE_THREE = 4;// 三张图

    public OverViewAdapter(Context mContext, ArrayList<String> list) {
        super();
        this.mContext = mContext;
        this.list = list;
    }

    //这里就写死了啊
    @Override
    public int getItemViewType(int position) {
        if (position < 2) {  //0,1;
            return TYPE_TWO;
        } else {
            return TYPE_ONE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ONE) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_overview_one, parent, false);
            return new OverViewOneHolder(v);
        } else {
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_overview_two, parent, false);
            return new OverViewTwoHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //holder.tv.setText(list.get(position));
        if (position < 2) {
            OverViewTwoHolder holder2 = (OverViewTwoHolder) holder;
            if (position == 0) {
                holder2.tv_title.setText("今日头条");
                holder2.img_title.setImageResource(R.drawable.home_title_top);
            } else if (position == 1) {
                holder2.tv_title.setText("科技范儿");
                holder2.img_title.setImageResource(R.drawable.home_title_android);
            }
            holder2.img1.setImageResource(R.drawable.home_title_meizi);
            holder2.tv1.setText("111111111");
            holder2.img2.setImageResource(R.drawable.home_title_meizi);
            holder2.tv2.setText("222222222");

        } else {
            OverViewOneHolder holder1 = (OverViewOneHolder) holder;
            if (position == 2) {
                holder1.tv_title.setText("热门电影");
                holder1.img_title.setImageResource(R.drawable.home_title_movie);
                holder1.img1.setImageResource(R.drawable.home_movie);
                holder1.tv1.setText("全球热门电影尽在其中O(∩_∩)O~");
            } else if (position == 3) {
                //使用缓存吧;
                holder1.tv_title.setText("精美图片");
                holder1.img_title.setImageResource(R.drawable.home_title_meizi);
                holder1.img1.setImageResource(R.drawable.home_chat);
                holder1.tv1.setText("给你的双眼一次放松吧");
            } else if (position == 4) {
                holder1.tv_title.setText("实在无聊");
                holder1.img_title.setImageResource(R.drawable.home_title_app);
                holder1.img1.setImageResource(R.drawable.home_chat);
                holder1.tv1.setText("图灵机器人陪你度过闲暇时间");
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class OverViewOneHolder extends RecyclerView.ViewHolder {
        ImageView img_title;
        TextView tv_title;
        LinearLayout ll_more;

        ImageView img1;
        TextView tv1;


        public OverViewOneHolder(View itemView) {
            super(itemView);
            img_title = (ImageView) itemView.findViewById(R.id.iv_title_type);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title_type);
            ll_more = (LinearLayout) itemView.findViewById(R.id.ll_title_more);

            img1 = (ImageView) itemView.findViewById(R.id.iv_one_photo);
            tv1 = (TextView) itemView.findViewById(R.id.tv_one_photo_title);
        }
    }


    public static class OverViewTwoHolder extends RecyclerView.ViewHolder {
        ImageView img_title;
        TextView tv_title;
        LinearLayout ll_more;

        ImageView img1;
        TextView tv1;
        ImageView img2;
        TextView tv2;


        public OverViewTwoHolder(View itemView) {
            super(itemView);
            img_title = (ImageView) itemView.findViewById(R.id.iv_title_type);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title_type);
            ll_more = (LinearLayout) itemView.findViewById(R.id.ll_title_more);

            img1 = (ImageView) itemView.findViewById(R.id.iv_two_one_one);
            tv1 = (TextView) itemView.findViewById(R.id.tv_two_one_one_title);

            img2 = (ImageView) itemView.findViewById(R.id.iv_two_one_two);
            tv2 = (TextView) itemView.findViewById(R.id.tv_two_one_two_title);
        }
    }

}

