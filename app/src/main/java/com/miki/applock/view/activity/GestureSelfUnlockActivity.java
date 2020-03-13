package com.miki.applock.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.TextureView;
import android.widget.RelativeLayout;

import com.miki.applock.R;
import com.miki.applock.base.BaseActivity;
import com.miki.applock.utils.LockPatternUtils;
import com.miki.applock.view.widget.LockPatternView;
import com.miki.applock.view.widget.LockPatternViewPattern;

public class GestureSelfUnlockActivity extends BaseActivity {

    private LockPatternView mLockPatternView;
    private LockPatternUtils mLockPatternUtils;
    private LockPatternViewPattern mPatternViewPattern;
    private int mFailedPatternAttemptsSinceLastTimeout = 0;
    private String pkgName; //解锁应用的包名
    private RelativeLayout mTopLayout;

    private TextureView mTextureView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_gesture_self_unlock;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mLockPatternView = (LockPatternView) findViewById(R.id.unlock_lock_view);
        mTopLayout = (RelativeLayout) findViewById(R.id.top_layout);
        mTextureView = (TextureView) findViewById(R.id.texture_view);
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
                startActivity(new Intent(this, MainActivity.class));
            } else {
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
