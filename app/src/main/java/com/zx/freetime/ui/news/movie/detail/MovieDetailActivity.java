package com.zx.freetime.ui.news.movie.detail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.zx.freetime.R;
import com.zx.freetime.adapter.MovieDetailAdapter;
import com.zx.freetime.base.BaseHeaderActivity;
import com.zx.freetime.bean.movie.MovieDetailBean;
import com.zx.freetime.bean.movie.SubjectsBean;
import com.zx.freetime.http.HttpUtils;
import com.zx.freetime.utils.CommonUtils;
import com.zx.freetime.utils.StatusBarUtil;
import com.zx.freetime.utils.StatusBarUtils;
import com.zx.freetime.utils.StringFormatUtil;

import jp.wasabeef.glide.transformations.BlurTransformation;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MovieDetailActivity extends BaseHeaderActivity {

    private SubjectsBean subjectsBean;
    private String mMoreUrl;
    private String mMovieName;
    // 这个是高斯图背景的高度
    private int imageBgHeight;
    // 滑动多少距离后标题不透明
    private int slidingDistance;

    private ImageView imgLoading;
    private AnimationDrawable loagdingDrawable;
    private LinearLayout loadError;
    private LinearLayout loadProgress;

    private ImageView imgItemBg;
    private ImageView imgOnePhoto;
    private TextView tvOneRattingRate;
    private TextView tvOneRatingNumber;
    private TextView tvOneDirector;
    private TextView tvOneCasts;
    private TextView tvOneGenres;

    private TextView tvOneDay;
    private TextView tvOneCity;
    private RecyclerView rvCast;
    private Toolbar tbBaseTitle;
    private ImageView ivBaseTitlebarBg;

    private TextView tvOneTitle;
    private TextView tvOneSummary;

    private MovieDetailAdapter mAdapter;

    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_movie_detail);

        setToolBar();

        if (getIntent() != null) {
            subjectsBean = (SubjectsBean) getIntent().getSerializableExtra("bean");
            initHeader();
        }


//        initRecyclerView();

        initSlideShapeTheme();

        setTitles();

        loadMovieDetail();
    }


    @Override
    protected void intiView() {
        Log.e("###", "开始初始化view");
        imgItemBg = (ImageView) findViewById(R.id.img_item_bg);
        tbBaseTitle = (Toolbar) findViewById(R.id.tb_base_title);
        ivBaseTitlebarBg = (ImageView) findViewById(R.id.iv_base_titlebar_bg);
        loadProgress = (LinearLayout) findViewById(R.id.ll_progress_bar);
        loadError = (LinearLayout) findViewById(R.id.ll_error_refresh);

        //进入的时候先启动加载动画
        imgLoading = (ImageView) findViewById(R.id.img_progress);
        loagdingDrawable = (AnimationDrawable) imgLoading.getDrawable();
        if (!loagdingDrawable.isRunning()) {
            loagdingDrawable.start();
        }

        if (headerView != null) {
            imgOnePhoto = (ImageView) headerView.findViewById(R.id.iv_one_photo);
            tvOneRattingRate = (TextView) headerView.findViewById(R.id.tv_one_rating_rate);
            tvOneRatingNumber = (TextView) headerView.findViewById(R.id.tv_one_rating_number);
            tvOneDirector = (TextView) headerView.findViewById(R.id.tv_one_directors);
            tvOneCasts = (TextView) headerView.findViewById(R.id.tv_one_casts);
            tvOneGenres = (TextView) headerView.findViewById(R.id.tv_one_genres);
            tvOneDay = (TextView) headerView.findViewById(R.id.tv_one_day);
            tvOneCity = (TextView) headerView.findViewById(R.id.tv_one_city);
        }

        if (contentView != null) {
            Log.e("###", "初始化content中的view");
            tvOneTitle = (TextView) contentView.findViewById(R.id.tv_one_title);
            tvOneSummary = (TextView) contentView.findViewById(R.id.tv_one_summary);
            rvCast = (RecyclerView) contentView.findViewById(R.id.xrv_cast);
            if (rvCast == null) {
                Log.e("###", "RecyclerView是null");
            }
        }


    }

    @Override
    protected int getHeaderLayout() {
        return R.layout.movie_header;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.movie_content;
    }


    void initRecyclerView() {
        rvCast.setVisibility(View.VISIBLE);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(MovieDetailActivity.this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCast.setLayoutManager(mLayoutManager);

        // 需加，不然滑动不流畅
        rvCast.setNestedScrollingEnabled(false);
//        rvCast.setHasFixedSize(false);

        mAdapter = new MovieDetailAdapter();
        rvCast.setAdapter(mAdapter);
    }

    /**
     * 设置toolbar
     */
    protected void setToolBar() {
        setSupportActionBar(tbBaseTitle);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        // 手动设置才有效果
        tbBaseTitle.setTitleTextAppearance(this, R.style.ToolBar_Title);
        tbBaseTitle.setSubtitleTextAppearance(this, R.style.Toolbar_SubTitle);
        tbBaseTitle.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
//        setTitles();
    }

    private void initHeader() {
        //加载电影图片;
        Glide.with(imgOnePhoto.getContext())
                .load(subjectsBean.getImages().getLarge())
                .crossFade(500)  //淡入淡出
                .override((int) CommonUtils.getDimens(R.dimen.movie_detail_width), (int) CommonUtils.getDimens(R
                        .dimen.movie_detail_height))  //不适应图片大小,而是使用这个尺寸;
                .placeholder(R.drawable.img_default_meizi)
                .error(R.drawable.img_default_meizi)
                .into(imgOnePhoto);

        tvOneRattingRate.setText("评分:" + subjectsBean.getRating().getAverage());
        tvOneRatingNumber.setText(subjectsBean.getCollect_count() + "人评");
        tvOneDirector.setText(StringFormatUtil.formatName(subjectsBean.getDirectors()));
        tvOneCasts.setText(StringFormatUtil.formatName(subjectsBean.getCasts()));
        tvOneGenres.setText("类型:" + StringFormatUtil.formatGenres(subjectsBean.getGenres()));

        // "23":模糊度；"4":图片缩放4倍后再进行模糊,对背景进行模糊;
        Glide.with(this)
                .load(subjectsBean.getImages().getMedium())
                .error(R.drawable.stackblur_default)
                .placeholder(R.drawable.stackblur_default)
                .crossFade(500)
                .bitmapTransform(new BlurTransformation(this, 23, 4))
                .into(imgItemBg);
    }

    void loadMovieDetail() {
        subscription = HttpUtils.getInstance().getMovieDetailClient().getMovieDetail(subjectsBean.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MovieDetailBean>() {
                    @Override
                    public void onCompleted() {
                        showContent();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        showError();
                    }

                    @Override
                    public void onNext(final MovieDetailBean movieDetailBean) {
                        //这是UI线程;
                        // 上映日期
                        tvOneDay.setText(String.format("上映日期：%s", movieDetailBean.getYear()));
                        // 制片国家
                        tvOneCity.setText(String.format("制片国家/地区：%s", StringFormatUtil.formatGenres
                                (movieDetailBean.getCountries())));

                        tvOneTitle.setText(StringFormatUtil.formatGenres(movieDetailBean.getAka()));
                        tvOneSummary.setText(movieDetailBean.getSummary());

                        mMoreUrl = movieDetailBean.getAlt();
                        mMovieName = movieDetailBean.getTitle();

//                        initHeader();

                        //transformData(movieDetailBean);
                        for (int i = 0; i < movieDetailBean.getDirectors().size(); i++) {
                            movieDetailBean.getDirectors().get(i).setType("导演");
                        }
                        for (int i = 0; i < movieDetailBean.getCasts().size(); i++) {
                            movieDetailBean.getCasts().get(i).setType("演员");
                        }
                        mAdapter = new MovieDetailAdapter();
                        mAdapter.addAll(movieDetailBean.getDirectors());
                        mAdapter.addAll(movieDetailBean.getCasts());

                        setAdapter(mAdapter);
                    }
                });
    }


    protected void setTitles() {
//        Log.e("###", "setTitles: " + subjectsBean.getTitle());
        tbBaseTitle.setTitle(subjectsBean.getTitle());
        tbBaseTitle.setSubtitle(String.format("主演：%s", StringFormatUtil.formatName(subjectsBean.getCasts())));
    }

    void showContent() {
        if (loagdingDrawable.isRunning()) {
            loagdingDrawable.stop();
        }

        if (loadProgress.getVisibility() != View.GONE) {
            loadProgress.setVisibility(View.GONE);
        }
        if (loadError.getVisibility() != View.GONE) {
            loadError.setVisibility(View.GONE);
        }

        if (headContainer.getVisibility() != View.VISIBLE) {
            headContainer.setVisibility(View.VISIBLE);
        }

        if (contentContainer.getVisibility() != View.VISIBLE) {
            contentContainer.setVisibility(View.VISIBLE);
        }

//        mAdapter.notifyDataSetChanged();
    }

    void showError() {
        if (loagdingDrawable.isRunning()) {
            loagdingDrawable.stop();
        }

        if (loadProgress.getVisibility() != View.GONE) {
            loadProgress.setVisibility(View.GONE);
        }
        if (loadError.getVisibility() != View.VISIBLE) {
            loadError.setVisibility(View.VISIBLE);
        }

        if (headContainer.getVisibility() != View.GONE) {
            headContainer.setVisibility(View.GONE);
        }

        if (contentContainer.getVisibility() != View.GONE) {
            contentContainer.setVisibility(View.GONE);
        }
    }

    void initSlideShapeTheme() {
        //设置title栏的背景;
        setImgHeaderBg();

        // toolbar 的高
        int toolbarHeight = tbBaseTitle.getLayoutParams().height;
        final int headerBgHeight = toolbarHeight + StatusBarUtil.getStatusBarHeight(this);

        // 使背景图向上移动到图片的最低端，保留（titlebar+statusbar）的高度
        ViewGroup.LayoutParams params = ivBaseTitlebarBg.getLayoutParams();
        ViewGroup.MarginLayoutParams ivTitleHeadBgParams = (ViewGroup.MarginLayoutParams) ivBaseTitlebarBg
                .getLayoutParams();
        int marginTop = params.height - headerBgHeight;
        ivTitleHeadBgParams.setMargins(0, -marginTop, 0, 0);

        ivBaseTitlebarBg.setImageAlpha(0);
        StatusBarUtils.setTranslucentImageHeader(this, 0, tbBaseTitle);

        // 上移背景图片，使空白状态栏消失(这样下方就空了状态栏的高度)
        if (imgItemBg != null) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) imgItemBg.getLayoutParams();
            layoutParams.setMargins(0, -StatusBarUtil.getStatusBarHeight(this), 0, 0);

            ViewGroup.LayoutParams imgItemBgparams = imgItemBg.getLayoutParams();
            // 获得高斯图背景的高度
            imageBgHeight = imgItemBgparams.height;
        }

        // 变色
        initScrollViewListener();
        initNewSlidingParams();
    }

    /**
     * 加载titlebar背景
     */
    private void setImgHeaderBg() {
        if (subjectsBean == null) {
            return;
        }
        String imgUrl = subjectsBean.getImages().getMedium();
        Log.e("###", "拿到的url" + imgUrl);
        if (!TextUtils.isEmpty(imgUrl)) {

            // 高斯模糊背景 原来 参数：12,5  23,4
            Glide.with(this).load(imgUrl)
                    // .placeholder(R.drawable.stackblur_default)
                    .error(R.drawable.stackblur_default)
                    .bitmapTransform(new BlurTransformation(this, 23, 4))
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean
                                isFirstResource) {
                            e.printStackTrace();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable>
                                target,
                                                       boolean isFromMemoryCache, boolean isFirstResource) {
                            tbBaseTitle.setBackgroundColor(Color.TRANSPARENT);
                            ivBaseTitlebarBg.setImageAlpha(0);
                            ivBaseTitlebarBg.setVisibility(View.VISIBLE);
                            return false;
                        }
                    }).into(ivBaseTitlebarBg);
        }
    }

    private void initScrollViewListener() {
        // 为了兼容23以下
        ((NestedScrollView) findViewById(R.id.mns_base)).setOnScrollChangeListener(new NestedScrollView
                .OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                scrollChangeHeader(scrollY);
            }
        });
    }

    private void initNewSlidingParams() {
        int titleBarAndStatusHeight = (int) (CommonUtils.getDimens(R.dimen.nav_bar_height) + StatusBarUtil
                .getStatusBarHeight(this));
        // 减掉后，没到顶部就不透明了
        slidingDistance = imageBgHeight - titleBarAndStatusHeight - (int) (CommonUtils.getDimens(R.dimen
                .base_header_activity_slide_more));
    }

    /**
     * 根据页面滑动距离改变Header方法
     */
    private void scrollChangeHeader(int scrolledY) {
        if (scrolledY < 0) {
            scrolledY = 0;
        }
        float alpha = Math.abs(scrolledY) * 1.0f / (slidingDistance);

        Drawable drawable = ivBaseTitlebarBg.getDrawable();

        if (drawable == null) {
            return;
        }
        if (scrolledY <= slidingDistance) {
            // title部分的渐变
            drawable.mutate().setAlpha((int) (alpha * 255));
            ivBaseTitlebarBg.setImageDrawable(drawable);
        } else {
            drawable.mutate().setAlpha(255);
            ivBaseTitlebarBg.setImageDrawable(drawable);
        }
    }


    private void setAdapter(MovieDetailAdapter mAdapter) {
        rvCast.setVisibility(View.VISIBLE);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(MovieDetailActivity.this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCast.setLayoutManager(mLayoutManager);

        // 需加，不然滑动不流畅
        rvCast.setNestedScrollingEnabled(false);
        rvCast.setHasFixedSize(false);

        rvCast.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }


    /**
     * @param context      activity
     * @param positionData bean
     * @param imageView    imageView
     */
    public static void start(Activity context, SubjectsBean positionData, ImageView imageView) {
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra("bean", positionData);
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(context,
                        imageView, "transition_movie_img");//与xml文件对应
        ActivityCompat.startActivity(context, intent, options.toBundle());
    }
}
