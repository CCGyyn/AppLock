package com.miki.settings.applock.contract;

import com.miki.settings.applock.base.BasePresenter;
import com.miki.settings.applock.base.BaseView;

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
