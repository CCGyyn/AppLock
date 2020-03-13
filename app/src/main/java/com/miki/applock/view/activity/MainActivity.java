package com.miki.applock.view.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.miki.applock.R;
import com.miki.applock.base.AppConstants;
import com.miki.applock.base.BaseActivity;
import com.miki.applock.bean.AppInfo;
import com.miki.applock.contract.MainContract;
import com.miki.applock.p.MainPresenter;
import com.miki.applock.utils.CommonUtil;
import com.miki.applock.view.fragment.SysAppFragment;
import com.miki.applock.view.fragment.UserAppFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * @author：cai_gp on 2020/3/9
 */
public class MainActivity extends BaseActivity implements MainContract.View, View.OnClickListener {

    private static final int RESULT_ACTION_USAGE_ACCESS_SETTINGS = 0;
    private TextView lockSwitch;
    private TextView tvChangePwd;
    private ViewPager appListViewPager;
    private TabLayout mTabLayout;
    private MainContract.Presenter mainPresenter;
    private List<String> titles;
    private List<Fragment> fragmentList;
    private boolean lock = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        lockSwitch = (TextView)findViewById(R.id.lock_switch);
        tvChangePwd = (TextView)findViewById(R.id.tv_change_pwd);
        appListViewPager = (ViewPager)findViewById(R.id.app_list);
        mTabLayout = (TabLayout)findViewById(R.id.tab_layout);
        mainPresenter = new MainPresenter(this, this);
    }

    @Override
    protected void initData() {
        mainPresenter.loadAppInfo();
        mainPresenter.loadLockSwitchStatus();
    }

    @Override
    protected void initAction() {
        if(!CommonUtil.isStatAccessPermissionSet(MainActivity.this)
                && CommonUtil.isNoOption(MainActivity.this)) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivityForResult(intent, RESULT_ACTION_USAGE_ACCESS_SETTINGS);
        } else {
            mainPresenter.loadAppInfo();
        }
        lockSwitch.setOnClickListener(this);
        tvChangePwd.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_ACTION_USAGE_ACCESS_SETTINGS: {
                if (CommonUtil.isStatAccessPermissionSet(MainActivity.this)) {
                    mainPresenter.loadAppInfo();
                } else {
                    Toast.makeText(MainActivity.this, "申请失败", Toast.LENGTH_SHORT).show();
                }
            }
            default:
                break;
        }
    }

    @Override
    public void loadAppInfoSuccess(List<AppInfo> list) {
        int sysAppNum = 0;
        int userAppNum = 0;

        for (AppInfo app:list
             ) {
            if(app.isSysApp()) {
                sysAppNum++;
            } else {
                userAppNum++;
            }
        }

        titles = new ArrayList<>();
        titles.add("系统应用" + "(" + sysAppNum + ")");
        titles.add("第三方应用" + "(" + userAppNum + ")");

        SysAppFragment sysAppFragment = SysAppFragment.newInstance(list);
        UserAppFragment userAppFragment = UserAppFragment.newInstance(list);
        fragmentList = new ArrayList<>();
        fragmentList.add(sysAppFragment);
        fragmentList.add(userAppFragment);
        AppPageAdapter appPageAdapter = new AppPageAdapter(getSupportFragmentManager(),
                fragmentList, titles);
        appListViewPager.setAdapter(appPageAdapter);
        mTabLayout.setupWithViewPager(appListViewPager);
    }

    @Override
    public void loadLockSwitchStatus(boolean lockStatus) {
        lock = lockStatus;
        setLockSwitchDrawable(lockStatus);
        appListDisplay(lockStatus);
    }

    @Override
    public void gotoCreatePwd() {
        Intent intent = new Intent(this, CreatePwdActivity.class);
        intent.putExtra(AppConstants.LOCK_FROM, AppConstants.LOCK_CREATE_PWD);
        startActivity(intent);
        finish();
    }

    @Override
    public void updateAppListViewByLockStatus(boolean lockStatus) {
        setLockSwitchDrawable(lockStatus);
        appListDisplay(lockStatus);
    }

    private void setLockSwitchDrawable(boolean lockStatus) {
        Drawable drawable = ContextCompat.getDrawable(this,
                lockStatus ? R.drawable.switch_open : R.drawable.switch_close);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                 drawable.getMinimumHeight());
        lockSwitch.setCompoundDrawables(null, null, drawable, null);
    }

    private void appListDisplay(boolean lockStatus) {
        tvChangePwd.setVisibility(lockStatus ? View.VISIBLE:View.INVISIBLE);
        mTabLayout.setVisibility(lockStatus ? View.VISIBLE:View.INVISIBLE);
        appListViewPager.setVisibility(lockStatus ? View.VISIBLE:View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lock_switch: {
                lock = !lock;
                mainPresenter.changeAppLockStatus(lock);
                break;
            }
            case R.id.tv_change_pwd: {
                Intent intent = new Intent(this, CreatePwdActivity.class);
                intent.putExtra(AppConstants.LOCK_FROM, AppConstants.LOCK_CHANGE_PWD);
                startActivity(intent);
            }
            default:
                break;
        }
    }

    public class AppPageAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> fragmentList;
        private List<String> titles;

        public AppPageAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> titles) {
            super(fm);
            this.fragmentList = fragmentList;
            this.titles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        @Override
        public int getCount() {
            return titles.size();
        }

    }
}
