package com.zx.freetime.ui.picture;


import com.zx.freetime.base.BasePresenter;
import com.zx.freetime.base.BaseView;
import com.zx.freetime.bean.picture.PictureBean;

import java.util.List;

/**
 * Created by zhangxin on 2016/10/28.
 * <p>
 * Description :
 */

public class PictureContract {
    interface View extends BaseView {
        void load(List<PictureBean.ResultBean> datas);

        void showErrorView();

        void showNormalView();

        //用来订阅;
        PictureFragment getFragment();
    }

    interface Presenter extends BasePresenter {
        void getPictures(int page);
    }
}
