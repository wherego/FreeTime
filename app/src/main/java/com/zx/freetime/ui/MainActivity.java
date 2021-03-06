package com.zx.freetime.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zx.freetime.R;
import com.zx.freetime.adapter.MyFragmentPagerAdapter;
import com.zx.freetime.base.BaseSkinActivity;
import com.zx.freetime.rx.RxBus;
import com.zx.freetime.rx.RxBusBaseMessage;
import com.zx.freetime.rx.RxCodeConstants;
import com.zx.freetime.ui.chat.ChatFragment;
import com.zx.freetime.ui.menu.NavAboutActivity;
import com.zx.freetime.ui.menu.NavDeedBackActivity;
import com.zx.freetime.ui.menu.NavSettingActivity;
import com.zx.freetime.ui.news.NewsFragment;
import com.zx.freetime.ui.picture.PictureFragment;
import com.zx.freetime.widget.ZCircleImageView.CircleImageDrawable;

import java.util.ArrayList;

import rx.functions.Action1;

public class MainActivity extends BaseSkinActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private FrameLayout llTitleMenu;

    private DrawerLayout drawerLayout;
    private NavigationView navView;

    Toolbar toolbar;
    private ViewPager vpContent; //toolBar下面的全体部分就是这个ViewPager;

    private ImageView llTitleGank;
    private ImageView llTitleOne;
    private ImageView llTitleDou;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initContentFragment();
        initDrawerLayout();
        initListener();
        initRxBus();
    }

    private void initView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navView = (NavigationView) findViewById(R.id.nav_view);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        vpContent = (ViewPager) findViewById(R.id.vp_content);

        llTitleGank = (ImageView) findViewById(R.id.iv_title_gank);
        llTitleOne = (ImageView) findViewById(R.id.iv_title_one);
        llTitleDou = (ImageView) findViewById(R.id.iv_title_dou);
        llTitleMenu = (FrameLayout) findViewById(R.id.ll_title_menu);

    }

    private void initDrawerLayout() {
        navView.inflateHeaderView(R.layout.nav_header_main);
        View headerView = navView.getHeaderView(0);

        ImageView ivAvatar = (ImageView) headerView.findViewById(R.id.iv_avatar);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.avatar);
        ivAvatar.setImageDrawable(new CircleImageDrawable(bitmap));

        LinearLayout llNavSetting = (LinearLayout) headerView.findViewById(R.id.ll_nav_setting);
        LinearLayout llNavHomepage = (LinearLayout) headerView.findViewById(R.id.ll_nav_homepage);
        LinearLayout llNavDeedback = (LinearLayout) headerView.findViewById(R.id.ll_nav_deedback);
        LinearLayout llNavAbout = (LinearLayout) headerView.findViewById(R.id.ll_nav_about);
        llNavHomepage.setOnClickListener(this);
        llNavSetting.setOnClickListener(this);
        llNavDeedback.setOnClickListener(this);
        llNavAbout.setOnClickListener(this);
    }

    private void initListener() {
        llTitleMenu.setOnClickListener(this);
        llTitleGank.setOnClickListener(this);
        llTitleOne.setOnClickListener(this);
        llTitleDou.setOnClickListener(this);

    }


    //使用的一个Activity,多Fragment的架构;核心中的核心,使用多Fragment架构;
    private void initContentFragment() {
        ArrayList<Fragment> mFragmentList = new ArrayList<>();
        mFragmentList.add(new NewsFragment()); //主页面
        mFragmentList.add(new PictureFragment());  //电影推荐
        mFragmentList.add(new ChatFragment()); //文艺文学
        // 注意使用的是：getSupportFragmentManager
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragmentList);
        vpContent.setAdapter(adapter);
        // 设置ViewPager最大缓存的页面个数(cpu消耗少),因为这里ViewPager的数量是三个,所以在这里无所谓缓存不缓存;
        vpContent.setOffscreenPageLimit(2);
        vpContent.addOnPageChangeListener(this);
        llTitleGank.setSelected(true);
        vpContent.setCurrentItem(0);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_title_menu:// 开启菜单
                drawerLayout.openDrawer(GravityCompat.START);
                // 关闭
//                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.iv_title_gank:// 干货栏
                if (vpContent.getCurrentItem() != 0) {//不然cpu会有损耗
                    llTitleGank.setSelected(true);
                    llTitleOne.setSelected(false);
                    llTitleDou.setSelected(false);
                    vpContent.setCurrentItem(0);
                }
                break;
            case R.id.iv_title_one:// 电影栏
                if (vpContent.getCurrentItem() != 1) {
                    llTitleOne.setSelected(true);
                    llTitleGank.setSelected(false);
                    llTitleDou.setSelected(false);
                    vpContent.setCurrentItem(1);
                }
                break;
            case R.id.iv_title_dou:// 书籍栏
                if (vpContent.getCurrentItem() != 2) {
                    llTitleDou.setSelected(true);
                    llTitleOne.setSelected(false);
                    llTitleGank.setSelected(false);
                    vpContent.setCurrentItem(2);
                }
                break;
            case R.id.ll_nav_homepage:// 主页
                drawerLayout.closeDrawer(GravityCompat.START);
                drawerLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                        intent.putExtra("url", "https://github.com/zachaxy/FreeTime");
                        startActivity(intent);
                    }
                }, 360);

                break;

            case R.id.ll_nav_setting://个性设置界面
                drawerLayout.closeDrawer(GravityCompat.START);
                drawerLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        NavSettingActivity.start(MainActivity.this);
                    }
                }, 360);
                break;
            case R.id.ll_nav_deedback:// 问题反馈
                drawerLayout.closeDrawer(GravityCompat.START);
                drawerLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        NavDeedBackActivity.start(MainActivity.this);
                    }
                }, 360);
                break;
            case R.id.ll_nav_about:// 关于自己
                drawerLayout.closeDrawer(GravityCompat.START);
                drawerLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        NavAboutActivity.start(MainActivity.this);
                    }
                }, 360);
                break;
            default:
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    Toast.makeText(MainActivity.this, "暂时还未完成,回收抽屉", Toast.LENGTH_LONG).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                break;
        }
    }

    //################监听ViewPager滑动改变的回调##################
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                llTitleGank.setSelected(true);
                llTitleOne.setSelected(false);
                llTitleDou.setSelected(false);
                break;
            case 1:
                llTitleOne.setSelected(true);
                llTitleGank.setSelected(false);
                llTitleDou.setSelected(false);
                break;
            case 2:
                llTitleDou.setSelected(true);
                llTitleOne.setSelected(false);
                llTitleGank.setSelected(false);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void initRxBus() {
        RxBus.getDefault().toObservable(RxCodeConstants.JUMP_TO_PARENT, RxBusBaseMessage.class)
                .subscribe(new Action1<RxBusBaseMessage>() {
                    @Override
                    public void call(RxBusBaseMessage msg) {
                        vpContent.setCurrentItem(msg.getCode());
                    }
                });
    }
}
