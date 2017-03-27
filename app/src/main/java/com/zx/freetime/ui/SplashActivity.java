package com.zx.freetime.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zx.freetime.R;
import com.zx.freetime.utils.Constants;
import com.zx.freetime.widget.ZSplash.WowSplashView;
import com.zx.freetime.widget.ZSplash.WowView;

import java.util.Random;

public class SplashActivity extends AppCompatActivity {

    private WowSplashView mWowSplashView;
    private WowView mWowView;

    private ImageView splashImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashImg = (ImageView) findViewById(R.id.splash_bg);
        mWowSplashView = (WowSplashView) findViewById(R.id.wowSplash);
        mWowView = (WowView) findViewById(R.id.wowView);

        //首先splashView开始动画,其实是先画铁塔
        mWowSplashView.startAnimate();

//        int i = new Random().nextInt(Constants.TRANSITION_URLS.length);
        Glide.with(this)
                .load("http://img2.3lian.com/2014/c7/25/d/40.jpg")   //网络加载字符串类型的url;
                .placeholder(R.drawable.img_transition_default) //网络加载时设置的等待图片;
                .error(R.drawable.img_transition_default)
                .into(splashImg);

        //同时监听动画是否结束,如果结束了,那么就将铁塔的view设置为gone;同时将我们要显示的view显示出来,设置为visible;
        //同时显示的时候也是需要一个动画的;
        mWowSplashView.setOnEndListener(new WowSplashView.OnEndListener() {
            @Override
            public void onEnd(WowSplashView wowSplashView) {
                //动画结束之后,铁塔的位置就不要了;
                mWowSplashView.setVisibility(View.GONE);
                //然后开始显示洞图;
                mWowView.setVisibility(View.VISIBLE);
                //显示动画;
                mWowView.startAnimate(wowSplashView.getDrawingCache());

            }
        });


        mWowView.setOnEndListener(new WowView.OnEndListener() {
            @Override
            public void onEnd(WowView wowView) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                //跳转页面还是用了动画,进入的动画是缩小,退出的界面是out;
                //Activity切换效果,在startActivity(intent)之后调该方法即可;
                // 可能会产生不起作用的情况:在android2.0一下,这个情况不用担心,已经不再对4.0一下做兼容了;要么就是该方法的调用位置不对,不能在嵌套的子Activity中;
                overridePendingTransition(R.anim.screen_zoom_out, R.anim.screen_zoom_in);
                finish();

            }
        });

    }
}
