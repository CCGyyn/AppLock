package com.miki.applock.view.activity;

import android.os.Bundle;

import com.miki.applock.base.BaseActivity;
import com.miki.applock.contract.SplashContract;
import com.miki.applock.p.SplashPresenter;
import com.miki.applock.utils.CommonUtil;

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
        presenter = new SplashPresenter(this);
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
