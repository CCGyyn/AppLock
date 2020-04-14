package com.miki.settings.applock.p;

import android.content.Context;
import android.os.AsyncTask;

import com.miki.settings.applock.bean.LockSwitch;
import com.miki.settings.applock.contract.SplashContract;
import com.miki.settings.applock.db.LockSwitchManager;

import java.util.List;

/**
 * @author：cai_gp on 2020/3/12
 */
public class SplashPresenter implements SplashContract.Presenter {

    private SplashContract.View mView;
    private LockSwitchManager lockSwitchManager;
    private CheckLockTask checkLockTask;
    private Context mContext;

    public SplashPresenter(SplashContract.View mView, Context context) {
        this.mView = mView;
        this.mContext = context;
    }

    @Override
    public void isLock() {
        checkLockTask = new CheckLockTask();
        checkLockTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private class CheckLockTask extends AsyncTask<Void, String, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            lockSwitchManager = new LockSwitchManager(mContext);
            List<LockSwitch> lockSwitchList = lockSwitchManager.queryAll();
            boolean result = (lockSwitchList.size() != 0 && lockSwitchList.get(lockSwitchList.size() - 1).isLockstitch());
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(!result) {
                mView.gotoMain();
            } else {
                mView.gotoGestureSelf();
            }
        }
    }
}
