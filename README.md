# 概览

一款基于网易云音乐UI，整合gank.io及豆瓣API，遵循 Google Material Design的阅读类新闻客户端，整体采用MVP架构,并集成RxJava，Retrofit，Glide 等开源框架。



该项目属于练手项目,目的是通过重复造轮子对所学的知识点进行一个总结,因此该项目所以来的第三方库只有三个,其余功能均为手动实现;



# 知识点

- MVP整体架构设计(参照谷歌官方MVP实例代码)
- 单Activity多Fragment式设计
- Fragment懒加载
- RxBus组件间通信
- 插件式换肤
- 扩展RecyclerView,支持添加header,footer,下拉刷新,上拉加载更多
- 自定义ZoomImageView,实现图片的点击缩放,手指捏合缩放
- 自定义轮播图BannerView
- 自定义开关组件SwitchView



# 效果展示

<img width="320" height=“590” src="https://github.com/zachaxy/HotNews/blob/master/screenshot/GIF.gif"></img>



#  所使用的第三方库

- Retrofit
- RxJava
- Glide