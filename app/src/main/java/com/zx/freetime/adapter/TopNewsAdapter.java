package com.zx.freetime.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zx.freetime.R;
import com.zx.freetime.bean.topnews.TopNewsItem;
import com.zx.freetime.ui.WebViewActivity;

import java.util.List;

/**
 * Created by zhangxin on 2017/3/26 0026.
 * <p>
 * Description :
 * TODO:未完成的属性:我不想直接显示一个webView,而是使用动画来显示一个自己的view,只显示body
 */

public class TopNewsAdapter extends RecyclerView.Adapter<TopNewsAdapter.TopNewsHolder> {
    private List<TopNewsItem> mItems;
    private Context mContext;

    public TopNewsAdapter(Context context, List<TopNewsItem> items) {
        mContext = context;
        mItems = items;
    }

    @Override
    public TopNewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_top_news, parent, false);
        return new TopNewsHolder(v);
    }

    @Override
    public void onBindViewHolder(TopNewsHolder holder, int position) {
        TopNewsItem bean = mItems.get(position);
        Log.e("###", bean.toString());
        holder.textView.setText(bean.getTitle());
        holder.sourceTextview.setText(bean.getAuthor_name());
        Glide.with(mContext)
                .load(bean.getThumbnail_pic_s())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.imageView);

        final String url = bean.getUrl();//这个才是详情页...
        holder.topNewsItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("url", url);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    static class TopNewsHolder extends RecyclerView.ViewHolder {
        TextView textView;
        LinearLayout topNewsItem;
        TextView sourceTextview;
        ImageView imageView;

        TopNewsHolder(View itemView) {
            super(itemView);
            topNewsItem = (LinearLayout) itemView.findViewById(R.id.top_item_layout);
            imageView = (ImageView) itemView.findViewById(R.id.item_image_id);
            textView = (TextView) itemView.findViewById(R.id.item_text_id);
            sourceTextview = (TextView) itemView.findViewById(R.id.item_text_source_id);
        }
    }
}

