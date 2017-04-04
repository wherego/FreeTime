package com.zx.freetime.ui.news.top.detail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zx.freetime.R;
import com.zx.freetime.base.BaseHeaderActivity;
import com.zx.freetime.bean.movie.SubjectsBean;
import com.zx.freetime.bean.topnews.TopNewsItem;
import com.zx.freetime.ui.news.movie.detail.MovieDetailActivity;
import com.zx.freetime.utils.CommonUtils;
import com.zx.freetime.utils.StringFormatUtil;

import org.xml.sax.XMLReader;

import java.io.IOException;

import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TopNewsDetailActivity extends BaseHeaderActivity {

    TextView tvTopNewsDetail;
    ImageView imgTopNewsDetail;
    ImageView imgItemBg;
    TopNewsItem item;

    boolean first = true;
    int firstIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_top_news_detail);
        if (getIntent() != null) {
            item = (TopNewsItem) getIntent().getSerializableExtra("bean");
            initHeader();
        }

        initSlideShapeTheme(setHeaderImgUrl(), setHeaderImageView());
        setTitle(item.getTitle());

        loadTopNewsDetail();
    }

    private void loadTopNewsDetail() {
        OkHttpClient client = new OkHttpClient();
        String url = item.getUrl();
        Request request = new Request.Builder().url(url).build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showError();
                    }

                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String origin = response.body().string();
                String second = Html.fromHtml(origin, new Html.ImageGetter() {
                    @Override
                    public Drawable getDrawable(String source) {
                        return null;
                    }
                }, new Html.TagHandler() {
                    @Override
                    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
                        if (first && output.length() > 0 && opening) {
                            firstIndex = output.length();
                            first = false;
                            output.delete(0, output.length());
                        }
                    }
                }).toString();

                //把标题去掉...
                if (second.substring(0, firstIndex) == second.substring(firstIndex, firstIndex * 2)) {
                    second = second.substring(2 * firstIndex);
                }

                StringBuilder sb = new StringBuilder();
                sb.append("\n").append("\uFFFC").append(" ").append("\n");

                final String third = second.replace(sb, "");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvTopNewsDetail.setText(third);
                        showContentView();
                    }

                });
            }
        });

    }

    @Override
    protected void initHeaderView(View headerView) {
        imgTopNewsDetail = (ImageView) headerView.findViewById(R.id.iv_topnews_photo);
        imgItemBg = (ImageView) headerView.findViewById(R.id.img_item_bg);
    }

    @Override
    protected void initContentView(View contentView) {
        tvTopNewsDetail = (TextView) contentView.findViewById(R.id.tv_top_news_detail);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_top_news_detail;
    }

    @Override
    protected int getHeaderLayout() {
        return R.layout.activity_top_news_detail_header;
    }

    @Override
    protected String setHeaderImgUrl() {
        if (item == null) {
            return "";
        }
        return item.getThumbnail_pic_s();
    }

    @Override
    protected ImageView setHeaderImageView() {
        return imgItemBg;
    }

    private void initHeader() {
        //加载电影图片;
        Glide.with(this)
                .load(item.getThumbnail_pic_s())
//                .crossFade(500)  //淡入淡出
                /*.override((int) CommonUtils.getDimens(R.dimen.movie_detail_width), (int) CommonUtils.getDimens(R
                        .dimen.movie_detail_height))*/  //不适应图片大小,而是使用这个尺寸;
                .placeholder(R.drawable.img_default_meizi)
                .error(R.drawable.img_default_meizi)
//                .centerCrop()
                .into(imgTopNewsDetail);

        Glide.with(this)
                .load(item.getThumbnail_pic_s())
                .error(R.drawable.stackblur_default)
                .placeholder(R.drawable.stackblur_default)
                .crossFade(500)
                .bitmapTransform(new BlurTransformation(this, 23, 4))
                .into(imgItemBg);

    }

    /**
     * @param context      activity
     * @param positionData bean
     * @param imageView    imageView
     */
    public static void start(Activity context, TopNewsItem positionData, ImageView imageView) {
        Intent intent = new Intent(context, TopNewsDetailActivity.class);
        intent.putExtra("bean", positionData);
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(context,
                        imageView, "transition_news_img");//与xml文件对应
        ActivityCompat.startActivity(context, intent, options.toBundle());
    }
}
