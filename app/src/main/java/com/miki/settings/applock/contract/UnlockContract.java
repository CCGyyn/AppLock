package com.miki.settings.applock.contract;

import android.content.pm.ApplicationInfo;

import com.miki.settings.applock.base.BasePresenter;
import com.miki.settings.applock.base.BaseView;

/**
 * @author：cai_gp on 2020/3/11
 */
public interface UnlockContract {

    interface View extends BaseView {
        void unLock();

        void loadUnlockAppInfoSuccess(ApplicationInfo applicationInfo);
    }

    interface Presenter extends BasePresenter {

        void loadUnlockAppInfo(long id);

        void updateLockInfo(long id);
    }
}
