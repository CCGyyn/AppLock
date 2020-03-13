package com.miki.applock.p;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;

import com.miki.applock.bean.AppInfo;
import com.miki.applock.bean.LockInfo;
import com.miki.applock.bean.LockSwitch;
import com.miki.applock.contract.MainContract;
import com.miki.applock.db.AppLockManager;
import com.miki.applock.db.LockSwitchManager;
import com.miki.applock.utils.CommonUtil;
import com.miki.applock.utils.DataUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author：cai_gp on 2020/3/10
 */
public class MainPresenter implements MainContract.Presenter{

    private static final int CREATE_NEW_PWD = 0;
    private static final int CHANGE_LOCK_STATUS = 1;

    private MainContract.View mView;
    private Context mContext;
    private PackageManager mPackageManager;
    private LoadAppInfoAsyncTask loadAppInfoAsyncTask;
    private ChangeAppLockStatusTask changeAppLockStatusTask;
    private LoadLockSwitchStatusTask loadLockSwitchStatusTask;
    private AppLockManager appLockManager;
    private LockSwitchManager lockSwitchManager;

    public MainPresenter(MainContract.View mView, Context mContext) {
        this.mView = mView;
        this.mContext = mContext;
        mPackageManager = mContext.getPackageManager();
        appLockManager = new AppLockManager();
    }

    @Override
    public void loadAppInfo() {
        loadAppInfoAsyncTask = new LoadAppInfoAsyncTask();
        loadAppInfoAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void loadLockSwitchStatus() {
        loadLockSwitchStatusTask = new LoadLockSwitchStatusTask();
        loadLockSwitchStatusTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void changeAppLockStatus(boolean lockStatus) {
        changeAppLockStatusTask = new ChangeAppLockStatusTask();
        changeAppLockStatusTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, lockStatus);
    }

    private class LoadAppInfoAsyncTask extends AsyncTask<Void, String, List<AppInfo>> {

        @Override
        protected List<AppInfo> doInBackground(Void... voids) {

            String currentPkg = mContext.getPackageName();
            List<AppInfo> list = new ArrayList<>();

            // 获取手机上全部应用
            Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            List<ResolveInfo> resolveInfos = mPackageManager.queryIntentActivities(intent, 0);
            for (ResolveInfo resolveInfo : resolveInfos) {
                String packageName = resolveInfo.activityInfo.packageName;
                if(currentPkg.equals(packageName)) {
                    continue;
                }
                ApplicationInfo applicationInfo = null;
                String appName = null;
                try {
                    applicationInfo = mPackageManager.getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
                    appName = mPackageManager.getApplicationLabel(applicationInfo).toString();
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                boolean isSysApp = false;
                if((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                    isSysApp = true;
                }
                list.add(new AppInfo(packageName, appName, applicationInfo, isSysApp));
            }
            list = DataUtil.clearRepeatCommInfo(list);

            List<LockInfo> lockList = appLockManager.queryAllLockApp();
            int count = 0;
            StringBuffer sb = new StringBuffer();
            for (AppInfo app:list
                 ) {
                if(lockList.size() > 0) {
                    for(int i = 0;i < lockList.size();i++) {
                        if(app.getPackageName().equals(lockList.get(i).getPackageName())) {
                            app.setLocked(true);
                            lockList.remove(i);
                            sb.append(count + "-");
                            break;
                        }
                    }
                } else {
                    break;
                }
                count++;
            }
            // 清理无用lock信息
            if(lockList.size() > 0) {
                lockList.forEach(lock -> appLockManager.deleteLockInfoById(lock.getId()));
            }
            // 将已锁定的app排在最上面
            String[] nums = sb.toString().split("-");
            for(int i = 0;i < nums.length;i++) {
                if(!CommonUtil.isStringEmpty(nums[i])) {
                    list.add(0, list.remove(Integer.parseInt(nums[i])));
                }
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<AppInfo> appInfos) {
            super.onPostExecute(appInfos);
            mView.loadAppInfoSuccess(appInfos);
        }

    }

    private class ChangeAppLockStatusTask extends AsyncTask<Boolean, String, Integer> {

        private boolean lockStatus;

        @Override
        protected Integer doInBackground(Boolean... booleans) {
            lockStatus = booleans[0];
            lockSwitchManager = new LockSwitchManager();
            List<LockSwitch> lockSwitchList = lockSwitchManager.queryAll();
            if(lockSwitchList.size() != 0) {
                lockSwitchManager.updateStatus(lockStatus);
                if(lockStatus) {
                    return CREATE_NEW_PWD;
                }
            } else {
                return CREATE_NEW_PWD;
            }
            return CHANGE_LOCK_STATUS;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if(result == CREATE_NEW_PWD) {
                mView.gotoCreatePwd();
            } else if(result == CHANGE_LOCK_STATUS) {
                mView.updateAppListViewByLockStatus(lockStatus);
            }

        }
    }

    private class LoadLockSwitchStatusTask extends AsyncTask<Void, String, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            lockSwitchManager = new LockSwitchManager();
            List<LockSwitch> list = lockSwitchManager.queryAll();
            if(list.size() != 0) {
                return list.get(list.size() - 1).isLockstitch();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            mView.loadLockSwitchStatus(result);
        }
    }
}
