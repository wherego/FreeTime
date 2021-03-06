package com.zx.freetime.ui.news.top;

import com.zx.freetime.bean.technews.AndroidNewsBean;
import com.zx.freetime.bean.topnews.TopNewsBean;
import com.zx.freetime.http.RequestImpl;
import com.zx.freetime.model.AndroidNewsModel;
import com.zx.freetime.model.TopNewsModel;
import com.zx.freetime.ui.news.tech.TechNewsContract;

import rx.Subscription;

/**
 * Created by zhangxin on 2017/3/26 0026.
 * <p>
 * Description :
 */

public class TopNewsPresenter implements TopNewsContract.Presenter {
    private TopNewsContract.View mView;
    private TopNewsModel mModel = new TopNewsModel();

    private RequestImpl mRequest;

    public TopNewsPresenter(TopNewsContract.View view) {
        mView = view;
        mRequest = new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                //NOTE:所有的view中load的参数都是对应的List<item>
                mView.load(((TopNewsBean) object).getResult().getData());
            }

            @Override
            public void loadFailed() {
                mView.showErrorView();
            }

            @Override
            public void loadComplete() {
                mView.showNormalView();
            }

            @Override
            public void addSubscription(Subscription subscription) {
                mView.getFragment().addSubscription(subscription);
            }
        };
    }

    @Override
    public void getTopNewsFromNet(Long lastTime) {
        //延时时间为10分钟吧....
        if (System.currentTimeMillis() - lastTime < 1000 * 60 * 10) {
            mView.sendDelay();
        } else {
            mModel.getTopNews(mRequest);
        }
    }

    @Override
    public void getTopNewsFromCache(String cacheName) {

    }

    @Override
    public void start() {
        //如果是start的话,直接获取,不判断时间;
        mModel.getTopNews(mRequest);
    }
}
