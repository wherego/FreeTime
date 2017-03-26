package com.zx.freetime.http;

import com.zx.freetime.bean.chat.ChatBean;
import com.zx.freetime.bean.movie.HotMovieBean;
import com.zx.freetime.bean.picture.PictureBean;
import com.zx.freetime.bean.technews.AndroidNewsBean;
import com.zx.freetime.bean.topnews.TopNewsBean;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by zhangxin on 2017/3/22 0022.
 * <p>
 * Description :
 */

public interface Api {

    @GET("toutiao/index?type=top&key=dc087d711c303a78873c604ff888c32f")
    Observable<TopNewsBean> getTopNews();

    /**
     * 分类数据: http://gank.io/api/data/数据类型/请求个数/第几页
     * 数据类型： 福利 | Android | iOS | 休息视频 | 拓展资源 | 前端 | all
     * 请求个数： 数字，大于0
     * 第几页：数字，大于0
     * eg: http://gank.io/api/data/Android/10/1
     */
    @GET("{pre_page}/{page}")
    Observable<AndroidNewsBean> getAndroidNews(@Path("page") int page, @Path("pre_page") int pre_page);

    /*
       * 用来获取美图的api
       * page:服务器的第几页,如果你想获取最新的图片,那么就设置为1吧
       * pre_page:你希望这次请求返回几张图片的url,服务器显示最多的50个,一般我们在调用的时候设为20;
       * */
    @GET("{pre_page}/{page}")
    Observable<PictureBean> getPicture(@Path("page") int page, @Path("pre_page") int pre_page);


    /**
     * 豆瓣热映电影，每日更新
     */
    @GET("v2/movie/in_theaters")
    Observable<HotMovieBean> getHotMovie();


    /***
     * 图灵机器人,使用的是聚合的API;
     * @param text
     * @param key
     * @return
     */
    @GET("robot/index?")
    Observable<ChatBean> talk(@Query("info") String text, @Query("key") String key);
}
