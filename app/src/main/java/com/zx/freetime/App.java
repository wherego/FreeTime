package com.zx.freetime;

import android.app.Application;

import com.zx.freetime.http.HttpUtils;

/**
 * Created by zhangxin on 2017/3/22 0022.
 * <p>
 * Description :
 */

public class App extends Application{
    private static App app;

    public static App getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        //这里并不打算一开始就初始化网络加载模块,因为那个类太大了,我们的原则是能延时到是什么就什么时候用;
        HttpUtils.getInstance().setContext(getApplicationContext());
    }
}
