package com.miki.settings.applock.view.activity;

import android.os.Bundle;

import com.miki.settings.applock.base.BaseActivity;
import com.miki.settings.applock.contract.SplashContract;
import com.miki.settings.applock.p.SplashPresenter;
import com.miki.settings.applock.utils.CommonUtil;

public class SplashActivity extends BaseActivity implements SplashContract.View {

    private SplashContract.Presenter presenter;

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {
        presenter = new SplashPresenter(this, this);
        presenter.isLock();
    }

    @Override
    protected void initAction() {

    }

    @Override
    public void gotoGestureSelf() {
        CommonUtil.startActivityWithAnimAfterFinish(this, GestureSelfUnlockActivity.class);
    }

    @Override
    public void gotoMain() {
        CommonUtil.startActivityWithAnimAfterFinish(this, MainActivity.class);
    }
}
