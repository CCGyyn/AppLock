# AppLock
在activity中oncreate进行添加

        Uri uriLockSwitch = Uri.parse("content://com.miki.applock.provider/LockSwitch");
        Cursor cursorLockSwitch = getContentResolver().query(uriLockSwitch, null, null,
                null, null);
        if(cursorLockSwitch != null) {
            while(cursorLockSwitch.moveToNext()) {
                boolean lockStatus = (cursorLockSwitch.getInt(cursorLockSwitch.getColumnIndex("lockstitch")) == 1);
                if(lockStatus) {
                    String packageName = getPackageName();
                    Uri uriAppLock = Uri.parse("content://com.miki.applock.provider/LockInfo");
                    Cursor cursorAppLock = getContentResolver().query(uriAppLock, null, "packageName",
                            new String[]{packageName}, null);
                    if(cursorAppLock != null) {
                        while (cursorAppLock.moveToNext()) {
                            String pkg = cursorAppLock.getString(cursorAppLock.getColumnIndex("packageName"));
                            if(pkg.equals(packageName)) {
                                boolean appLockStatus = (cursorAppLock.getInt(cursorAppLock.getColumnIndex("isLock")) == 1);
                                if(!appLockStatus) {
                                    break;
                                }
                                long id = cursorAppLock.getLong(cursorAppLock.getColumnIndex("id"));
                                ComponentName componentName = new ComponentName(
                                        "com.miki.applock",
                                        "com.miki.applock.view.activity.PasswordUnlockActivity"
                                        );
                                Intent applock = new Intent();
                                applock.setComponent(componentName);
                                applock.putExtra("data", packageName + "-" + id);
                                startActivity(applock);
                                finish();
                            }
                        }
                    }
                }
            }
        }
 在frameworks/base/services/core/java/com/android/server/display/DisplayPowerController.java 手机息屏后进行重新上锁
 
import android.database.Cursor;

import android.os.AsyncTask;

import android.content.ContentValues;

                 case MSG_SCREEN_OFF_UNBLOCKED:
                    UpdateLockTask updateLockTask = new UpdateLockTask();
                    updateLockTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    if (mPendingScreenOffUnblocker == msg.obj) {
                     

    private class UpdateLockTask extends AsyncTask{

        @Override
        protected Object doInBackground(Object... voids) {
            android.util.Log.d("ccgDisplay", "AsyncTask");
            Uri uriLockSwitch = Uri.parse("content://com.miki.applock.provider/LockSwitch");
            Cursor cursorLockSwitch = mContext.getContentResolver().query(uriLockSwitch, null, null,
                    null, null);
            if(cursorLockSwitch != null) {
                while(cursorLockSwitch.moveToNext()) {
                    boolean lockStatus = (cursorLockSwitch.getInt(cursorLockSwitch.getColumnIndex("lockstitch")) == 1);
                    if(lockStatus) {
                        Uri uriAppLock = Uri.parse("content://com.miki.applock.provider/LockInfo");
                        ContentValues values = new ContentValues();
                        values.put("isLock", 1);
                        mContext.getContentResolver().update(uriAppLock, values, null, null);
                    }
                }
            }
            return null;
        }
    }
