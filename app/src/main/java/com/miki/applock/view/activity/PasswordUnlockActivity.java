package com.miki.applock.view.activity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miki.applock.R;
import com.miki.applock.base.BaseActivity;
import com.miki.applock.contract.UnlockContract;
import com.miki.applock.p.UnlockPresenter;
import com.miki.applock.utils.LockPatternUtils;
import com.miki.applock.view.widget.LockPatternView;
import com.miki.applock.view.widget.LockPatternViewPattern;

import androidx.annotation.RequiresApi;

/**
 * @author：cai_gp on 2020/3/9
 */
public class PasswordUnlockActivity extends BaseActivity implements UnlockContract.View {

    private Button btn_1;
    private PackageManager packageManager;
    private String pkg;
    private long id;
    private UnlockContract.Presenter presenter;
    private ImageView unLockIcon;
    private TextView unLockName;
    private TextView unLockTip;
    private RelativeLayout unLockLayout;
    private LockPatternView mLockPatternView;
    private LockPatternUtils mLockPatternUtils;
    private LockPatternViewPattern mPatternViewPattern;

    @Override
    public int getLayoutId() {
        return R.layout.activity_password_unlock;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        unLockIcon = (ImageView)findViewById(R.id.unlock_icon);
        unLockName = (TextView)findViewById(R.id.unlock_name);
        unLockTip = (TextView)findViewById(R.id.unlock_tip);
        unLockLayout = (RelativeLayout)findViewById(R.id.unlock_layout);
        mLockPatternView = (LockPatternView)findViewById(R.id.unlock_pattern_view);
        presenter = new UnlockPresenter(this, this);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        String[] s = data.split("-");
        pkg = s[0];
        id = Long.parseLong(s[1]);
        packageManager = getPackageManager();
        presenter.loadUnlockAppInfo(id);
    }

    @Override
    protected void initAction() {
        initLockPatternView();
/*        btn_1.setOnClickListener((v) -> {
            String oldPass = ((EditText) findViewById(R.id.passwordTextEdit)).getText().toString();

            if (oldPass.contentEquals("1234")) {
                presenter.updateLockInfo(id);
                return ;
            } else {
                Toast.makeText(getApplicationContext(), "Password is wrong!", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void unLock() {
        Intent goBack = packageManager.getLaunchIntentForPackage(pkg);
        startActivity(goBack);
        finishAffinity();
    }

    @Override
    public void loadUnlockAppInfoSuccess(ApplicationInfo applicationInfo) {
        if(applicationInfo != null) {
            String appName = packageManager.getApplicationLabel(applicationInfo).toString();
            Drawable icon = packageManager.getApplicationIcon(applicationInfo);
            unLockName.setText(appName);
            unLockIcon.setImageDrawable(icon);
        }
    }

    /**
     * 初始化解锁控件
     */
    private void initLockPatternView() {
        mLockPatternView.setLineColorRight(0x80ffffff);
        mLockPatternUtils = new LockPatternUtils(this);
        mPatternViewPattern = new LockPatternViewPattern(mLockPatternView);
        mPatternViewPattern.setPatternListener(pattern -> {
            if (mLockPatternUtils.checkPattern(pattern)) { //解锁成功,更改数据库状态
                mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Correct);
                presenter.updateLockInfo(id);
            } else {
                mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
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
