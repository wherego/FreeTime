package com.zx.freetime.base;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zx.freetime.R;

/**
 * Created by zhangxin on 2017/3/22 0022.
 * <p>
 * Description :
 */

public abstract class BaseFragment extends Fragment {

    // fragment是否显示了,叫做isViewCreate更直接吧;
    protected boolean mIsVisible = false;

    // 加载中,一进来是有的,数据加载完设置为gone;
    private LinearLayout mLlProgressBar;

    // 加载失败
    private LinearLayout mLlRefresh;

    // 内容布局
    protected RelativeLayout mRlContainer;

    // 动画
    private AnimationDrawable mAnimationDrawable;

    private ImageView img;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View baseView = inflater.inflate(R.layout.fragment_base, container, false);

        //先把要准备的加载的布局初始化好;
        mLlProgressBar = (LinearLayout) baseView.findViewById(R.id.ll_progress_bar);
        mLlRefresh = (LinearLayout) baseView.findViewById(R.id.ll_error_refresh);
        mRlContainer = (RelativeLayout) baseView.findViewById(R.id.container);
        img = (ImageView) baseView.findViewById(R.id.img_progress);
        // 加载动画
        mAnimationDrawable = (AnimationDrawable) img.getDrawable();
        //NOTE:不知道这一步有没有问题;
        View contentView = inflater.inflate(getFragmentContent(), null);
        initContentView(contentView);
        mRlContainer.addView(contentView);
        return baseView;
    }

    public abstract void initContentView(View contentView);

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        // 点击加载失败布局
        mLlRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                onRefresh();
            }
        });

        //showLoading();  //别在这调用,没用...一下子就执行了;
        // TODO: 2017/3/22 0022  
        showContentView();
    }

    /**
     * 为BaseFragment设置内同;
     */
    public abstract int getFragmentContent();

    public abstract void onRefresh();

    /**
     * 显示加载中状态,主要是播放动画;什么时候调用,在界面显示后,加载数据之前调用;
     */
    public void showLoading() {
        Log.d("###", "showloading....");
        if (mLlProgressBar.getVisibility() != View.VISIBLE) {
            mLlProgressBar.setVisibility(View.VISIBLE);
        }
        // 开始动画
        if (!mAnimationDrawable.isRunning()) {
            mAnimationDrawable.start();
        }
        if (mRlContainer.getVisibility() != View.GONE) {
            mRlContainer.setVisibility(View.GONE);
        }
        if (mLlRefresh.getVisibility() != View.GONE) {
            mLlRefresh.setVisibility(View.GONE);
        }
    }

    /**
     * 加载完成的状态,加载中==>gone;动画播放停止;无法加载==>gone; bindingView显示出来...
     */
    public void showContentView() {
        if (mLlProgressBar.getVisibility() != View.GONE) {
            mLlProgressBar.setVisibility(View.GONE);
        }
        // 停止动画
        if (mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
        }
        if (mLlRefresh.getVisibility() != View.GONE) {
            mLlRefresh.setVisibility(View.GONE);
        }
        if (mRlContainer.getVisibility() != View.VISIBLE) {
            mRlContainer.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 加载失败点击重新加载的状态
     */
    public void showError() {
        if (mLlProgressBar.getVisibility() != View.GONE) {
            mLlProgressBar.setVisibility(View.GONE);
        }
        // 停止动画
        if (mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
        }
        if (mLlRefresh.getVisibility() != View.VISIBLE) {
            mLlRefresh.setVisibility(View.VISIBLE);
        }
        if (mRlContainer.getVisibility() != View.GONE) {
            mRlContainer.setVisibility(View.GONE);
        }
    }


}
