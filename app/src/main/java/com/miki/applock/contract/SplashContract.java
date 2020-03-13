package com.miki.applock.contract;

import com.miki.applock.base.BasePresenter;
import com.miki.applock.base.BaseView;

/**
 * @author：cai_gp on 2020/3/12
 */
public interface SplashContract {

    interface View extends BaseView {

        void gotoGestureSelf();

        void gotoMain();
    }

    interface Presenter extends BasePresenter {

        void isLock();
    }
}
