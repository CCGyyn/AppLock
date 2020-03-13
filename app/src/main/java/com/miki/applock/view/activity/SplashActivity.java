package com.miki.applock.view.activity;

import android.content.Intent;
import android.os.Bundle;

import com.miki.applock.R;
import com.miki.applock.base.BaseActivity;
import com.miki.applock.contract.SplashContract;
import com.miki.applock.p.SplashPresenter;

public class SplashActivity extends BaseActivity implements SplashContract.View {

    private SplashContract.Presenter presenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
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
        startActivity(new Intent(this, GestureSelfUnlockActivity.class));
    }

    @Override
    public void gotoMain() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
