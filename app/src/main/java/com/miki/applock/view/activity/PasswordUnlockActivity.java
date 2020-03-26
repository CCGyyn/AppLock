package com.miki.applock.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miki.applock.R;
import com.miki.applock.contract.UnlockContract;
import com.miki.applock.p.UnlockPresenter;
import com.miki.applock.utils.FingerHelper;
import com.miki.applock.utils.LockPatternUtils;
import com.miki.applock.view.widget.LockPatternView;
import com.miki.applock.view.widget.LockPatternViewPattern;

import org.litepal.LitePal;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * @author：cai_gp on 2020/3/9
 */
public class PasswordUnlockActivity extends Activity implements UnlockContract.View, FingerHelper.FingerPrintCallBack {

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
    private ViewStub fingerView;
    private FingerHelper fingerHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        initViews(savedInstanceState);
        initData();
        initAction();
        LitePal.getDatabase();
    }

    public int getLayoutId() {
        return R.layout.activity_password_unlock;
    }

    protected void initViews(Bundle savedInstanceState) {
        unLockIcon = (ImageView)findViewById(R.id.unlock_icon);
        unLockName = (TextView)findViewById(R.id.unlock_name);
        unLockTip = (TextView)findViewById(R.id.unlock_tip);
        unLockLayout = (RelativeLayout)findViewById(R.id.unlock_layout);
        mLockPatternView = (LockPatternView)findViewById(R.id.unlock_pattern_view);
        presenter = new UnlockPresenter(this, this);
        fingerHelper = new FingerHelper(this, this);
        fingerHelper.hasFinger();
    }

    protected void initData() {
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        String[] s = data.split("-");
        pkg = s[0];
        id = Long.parseLong(s[1]);
        packageManager = getPackageManager();
        presenter.loadUnlockAppInfo(id);
    }

    protected void initAction() {
        initLockPatternView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fingerHelper.stopFingerPrint();
    }

    @Override
    public void onBackPressed() {
        Intent intent= new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void unLock() {
        goBackByPkg();
    }

    private void goBackByPkg() {
        Intent goBack = packageManager.getLaunchIntentForPackage(pkg);
        startActivity(goBack);
        finishAffinity();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
                unLockTip.setText(getResources().getString(R.string.password_error_count));
                unLockTip.setTextColor(Color.RED);
                TranslateAnimation translateAnimation = new TranslateAnimation(-5, 5, 0, 0);
                translateAnimation.setDuration(100);
                translateAnimation.setRepeatCount(Animation.REVERSE);
                unLockTip.startAnimation(translateAnimation);
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

    @Override
    public void onAuthenticationSucceeded() {
        presenter.updateLockInfo(id);
    }

    @Override
    public void onAuthenticationFailed(int count) {
        unLockTip.setText(getResources().getString(R.string.fingerprint_wrong));
        unLockTip.setTextColor(Color.RED);
        TranslateAnimation translateAnimation = new TranslateAnimation(-5, 5, 0, 0);
        translateAnimation.setDuration(100);
        translateAnimation.setRepeatCount(Animation.REVERSE);
        unLockTip.startAnimation(translateAnimation);
    }

    @Override
    public void onAuthenticationError() {
        unLockTip.setText(getResources().getString(R.string.password_gestrue_tips));
        fingerView.setVisibility(View.INVISIBLE);
        Toast.makeText(this, getResources().getString(R.string.fingerprint_authentication_error),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void fingerClosed() {
        unLockTip.setText(getResources().getString(R.string.password_gestrue_tips));
    }

    @Override
    public void fingerOpen() {
        fingerView = (ViewStub)findViewById(R.id.finger_view);
        fingerView.setVisibility(View.VISIBLE);
        if(fingerHelper != null) {
            fingerHelper.startFingerPrint();
        }
        unLockTip.setText(getResources().getString(R.string.gesture_or_fingerprint));
    }

}
