package com.miki.applock.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.miki.applock.R;
import com.miki.applock.base.BaseActivity;
import com.miki.applock.utils.CommonUtil;
import com.miki.applock.utils.LockPatternUtils;
import com.miki.applock.view.widget.LockPatternView;
import com.miki.applock.view.widget.LockPatternViewPattern;

public class GestureSelfUnlockActivity extends BaseActivity {

    private LockPatternView mLockPatternView;
    private LockPatternUtils mLockPatternUtils;
    private LockPatternViewPattern mPatternViewPattern;
    private TextView unLockTip;
    private int mFailedPatternAttemptsSinceLastTimeout = 0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_gesture_self_unlock;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mLockPatternView = (LockPatternView) findViewById(R.id.unlock_lock_view);
        unLockTip = (TextView)findViewById(R.id.lock_tip_self);
    }

    @Override
    protected void initData() {
        initLockPatternView();
    }

    @Override
    protected void initAction() {

    }

    /**
     * 初始化解锁控件
     */
    private void initLockPatternView() {
        mLockPatternUtils = new LockPatternUtils(this);
        mPatternViewPattern = new LockPatternViewPattern(mLockPatternView);
        mPatternViewPattern.setPatternListener(pattern -> {
            if (mLockPatternUtils.checkPattern(pattern)) { //解锁成功,更改数据库状态
                mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Correct);
                CommonUtil.startActivityWithAnimAfterFinish(this, MainActivity.class);
            } else {
                unLockTip.setText(getResources().getString(R.string.password_error_count));
                unLockTip.setTextColor(Color.RED);
                TranslateAnimation translateAnimation = new TranslateAnimation(-5, 5, 0, 0);
                translateAnimation.setDuration(100);
                translateAnimation.setRepeatCount(Animation.REVERSE);
                unLockTip.startAnimation(translateAnimation);
                mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
                /*if (pattern.size() >= LockPatternUtils.MIN_PATTERN_REGISTER_FAIL) {
                    mFailedPatternAttemptsSinceLastTimeout++;
                    int retry = LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT - mFailedPatternAttemptsSinceLastTimeout;
                    if (retry >= 0) {
                    }
                } else {
                }*/
                mLockPatternView.postDelayed(mClearPatternRunnable, 500);
            }
        });
        mLockPatternView.setOnPatternListener(mPatternViewPattern);
        mLockPatternView.setTactileFeedbackEnabled(true);
    }

    private Runnable mClearPatternRunnable = new Runnable() {
        @Override
        public void run() {
            mLockPatternView.clearPattern();
        }
    };
}
