package com.miki.applock.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miki.applock.R;
import com.miki.applock.base.AppConstants;
import com.miki.applock.base.BaseActivity;
import com.miki.applock.bean.LockStage;
import com.miki.applock.contract.GestureCreateContract;
import com.miki.applock.p.GestureCreatePresenter;
import com.miki.applock.utils.LockPatternUtils;
import com.miki.applock.view.widget.LockPatternView;
import com.miki.applock.view.widget.LockPatternViewPattern;

import java.util.List;

public class CreatePwdActivity extends BaseActivity implements GestureCreateContract.View, View.OnClickListener{

    private TextView mLockTip;
    private LockPatternView mLockPatternView;
    private TextView mBtnReset;
    //图案锁相关
    private LockStage mUiStage = LockStage.Introduction;
    protected List<LockPatternView.Cell> mChosenPattern = null; //密码
    private LockPatternUtils mLockPatternUtils;
    private LockPatternViewPattern mPatternViewPattern;
    private GestureCreateContract.Presenter mGestureCreatePresenter;
    private RelativeLayout mTopLayout;

    @Override
    public int getLayoutId() {
        return R.layout.activity_create_pwd;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mLockPatternView = (LockPatternView) findViewById(R.id.lock_pattern_view);
        mLockTip = (TextView) findViewById(R.id.lock_tip);
        mBtnReset = (TextView) findViewById(R.id.btn_reset);
        mTopLayout = (RelativeLayout) findViewById(R.id.top_layout);
//        mTopLayout.setPadding(0, SystemBarHelper.getStatusBarHeight(this),0,0);
    }

    @Override
    protected void initData() {
        mGestureCreatePresenter = new GestureCreatePresenter(this, this);
        initLockPatternView();
        Intent intent = getIntent();
        String s = intent.getStringExtra(AppConstants.LOCK_FROM);
        switch (s) {
            case AppConstants.LOCK_CREATE_PWD:
                mLockTip.setText(getResources().getString(R.string.lock_tip_create_pwd));
                break;
            case AppConstants.LOCK_CHANGE_PWD:
                mLockTip.setText(getResources().getString(R.string.lock_tip_change_pwd));
                break;
            default:
        }
    }

    @Override
    protected void initAction() {
        mBtnReset.setOnClickListener(this);
    }

    /**
     * 更新当前锁的状态
     */
    @Override
    public void updateUiStage(LockStage stage) {
        mUiStage = stage;
    }

    /**
     * 更新当前密码
     */
    @Override
    public void updateChosenPattern(List<LockPatternView.Cell> mChosenPattern) {
        this.mChosenPattern = mChosenPattern;
    }

    /**
     * 更新提示信息
     */
    @Override
    public void updateLockTip(String text, boolean isToast) {
        mLockTip.setText(text);
    }

    @Override
    public void setHeaderMessage(int headerMessage) {
        mLockTip.setText(headerMessage);
    }

    @Override
    public void lockPatternViewConfiguration(boolean patternEnabled, LockPatternView.DisplayMode displayMode) {
        if (patternEnabled) {
            mLockPatternView.enableInput();
        } else {
            mLockPatternView.disableInput();
        }
        mLockPatternView.setDisplayMode(displayMode);
    }

    @Override
    public void Introduction() {
        clearPattern();
    }

    @Override
    public void HelpScreen() {

    }

    /**
     * 路径太短
     */
    @Override
    public void ChoiceTooShort() {
        mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);  //路径太短
        mLockPatternView.removeCallbacks(mClearPatternRunnable);
        mLockPatternView.postDelayed(mClearPatternRunnable, 500);
    }

    private Runnable mClearPatternRunnable = new Runnable() {
        @Override
        public void run() {
            mLockPatternView.clearPattern();
        }
    };

    @Override
    public void moveToStatusTwo() {

    }

    /**
     * 清空控件
     */
    @Override
    public void clearPattern() {
        mLockPatternView.clearPattern();
    }

    /**
     * 两次所画不一样
     */
    @Override
    public void ConfirmWrong() {
        mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);  //路径太短
        mLockPatternView.removeCallbacks(mClearPatternRunnable);
        mLockPatternView.postDelayed(mClearPatternRunnable, 500);
    }

    /**
     * 画成功
     */
    @Override
    public void ChoiceConfirmed() {
        mLockPatternUtils.saveLockPattern(mChosenPattern); //保存密码
        clearPattern();
        gotoLockMainActivity();
    }

    private void gotoLockMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 初始化锁屏控件
     */
    private void initLockPatternView() {
        mLockPatternUtils = new LockPatternUtils(this);
        mPatternViewPattern = new LockPatternViewPattern(mLockPatternView);
        mPatternViewPattern.setPatternListener(pattern -> mGestureCreatePresenter.onPatternDetected(pattern, mChosenPattern, mUiStage));
        mLockPatternView.setOnPatternListener(mPatternViewPattern);
        mLockPatternView.setTactileFeedbackEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reset:
                setStepOne();
                break;
            default:
                break;
        }
    }

    private void setStepOne() {
        mGestureCreatePresenter.updateStage(LockStage.Introduction);
        mLockTip.setText(getString(R.string.lock_recording_intro_header));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGestureCreatePresenter.onDestroy();
    }
}
