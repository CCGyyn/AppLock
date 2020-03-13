package com.miki.applock.contract;

import com.miki.applock.base.BasePresenter;
import com.miki.applock.base.BaseView;
import com.miki.applock.bean.AppInfo;

import java.util.List;

/**
 * @author：cai_gp on 2020/3/9
 */
public interface MainContract {
    interface View extends BaseView {
        /**
         * 加载应用列表
         * @param list
         */
        void loadAppInfoSuccess(List<AppInfo> list);

        /**
         * 加载锁的状态
         * @param lockStatus
         */
        void loadLockSwitchStatus(boolean lockStatus);

        void gotoCreatePwd();

        void updateAppListViewByLockStatus(boolean lockStatus);
    }

    interface Presenter extends BasePresenter {
        void loadAppInfo();

        void loadLockSwitchStatus();

        void changeAppLockStatus(boolean lockStatus);
    }
}
