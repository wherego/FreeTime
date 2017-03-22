package com.zx.freetime.http;

import com.zx.freetime.bean.technews.AndroidNewsBean;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by zhangxin on 2017/3/22 0022.
 * <p>
 * Description :
 */

public interface Api {
    /**
     * 分类数据: http://gank.io/api/data/数据类型/请求个数/第几页
     * 数据类型： 福利 | Android | iOS | 休息视频 | 拓展资源 | 前端 | all
     * 请求个数： 数字，大于0
     * 第几页：数字，大于0
     * eg: http://gank.io/api/data/Android/10/1
     */
    @GET("{pre_page}/{page}")
    Observable<AndroidNewsBean> getAndroidNews(@Path("page") int page, @Path("pre_page") int pre_page);
}
