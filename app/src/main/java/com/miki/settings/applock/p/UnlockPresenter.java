package com.miki.settings.applock.p;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;

import com.miki.settings.applock.bean.LockInfo;
import com.miki.settings.applock.contract.UnlockContract;
import com.miki.settings.applock.db.AppLockManager;

/**
 * @author：cai_gp on 2020/3/11
 */
public class UnlockPresenter implements UnlockContract.Presenter {

    private UnlockContract.View mView;
    private AppLockManager appLockManager;
    private UpdateLockInfoTask updateLockInfoTask;
    private Context mContext;
    private LoadUnlockAppInfoTask loadUnlockAppInfoTask;


    public UnlockPresenter(UnlockContract.View mView, Context context) {
        this.mView = mView;
        this.mContext = context;
    }

    @Override
    public void loadUnlockAppInfo(long id) {
        loadUnlockAppInfoTask = new LoadUnlockAppInfoTask();
        loadUnlockAppInfoTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, id);
    }

    @Override
    public void updateLockInfo(long id) {
        updateLockInfoTask = new UpdateLockInfoTask(id);
        updateLockInfoTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private class UpdateLockInfoTask extends AsyncTask<Void, String, Integer> {

        private long id;

        public UpdateLockInfoTask(long id) {
            this.id = id;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            appLockManager = new AppLockManager(mContext);
            appLockManager.updateLockInfoById(id, false);
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            mView.unLock();
        }
    }

    private class LoadUnlockAppInfoTask extends AsyncTask<Long, String, ApplicationInfo>{

        @Override
        protected ApplicationInfo doInBackground(Long... longs) {
            long id = longs[0];
            appLockManager = new AppLockManager(mContext);
            LockInfo lockInfo = appLockManager.queryById(id);
            ApplicationInfo applicationInfo = null;
            try {
                applicationInfo = mContext.getPackageManager().getApplicationInfo(lockInfo.getPackageName(),
                        PackageManager.GET_UNINSTALLED_PACKAGES);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            return applicationInfo;
        }

        @Override
        protected void onPostExecute(ApplicationInfo applicationInfo) {
            super.onPostExecute(applicationInfo);
            mView.loadUnlockAppInfoSuccess(applicationInfo);
        }
    }
}
