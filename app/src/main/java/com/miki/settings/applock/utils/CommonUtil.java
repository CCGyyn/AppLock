package com.miki.settings.applock.utils;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.provider.Settings;

import java.util.List;

import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

/**
 * @author：cai_gp on 2020/3/10
 */
public class CommonUtil {

    /**
     * 判断是否已获得 有权查看使用情况的应用程序 权限
     * @param context
     * @return
     */
    public static boolean isStatAccessPermissionSet(Context context) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                PackageManager packageManager = context.getPackageManager();
                ApplicationInfo info = packageManager.getApplicationInfo(context.getPackageName(), 0);
                AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
                appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, info.uid, info.packageName);
                return appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, info.uid, info.packageName) == AppOpsManager.MODE_ALLOWED;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return true;
        }
    }

    /**
     * 查看是否存在查看使用情况的应用程序界面
     *
     * @return
     */
    public static boolean isNoOption(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            PackageManager packageManager = context.getPackageManager();
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            return list.size() > 0;
        }
        return false;
    }

    public static boolean isStringEmpty(String s) {
        return (s == null || s.length() <= 0);
    }

    public static void startActivityWithAnimAfterFinish(Activity packageContext, Class<?> cls) {
        packageContext.startActivity(new Intent(packageContext, cls));
        packageContext.finish();
        packageContext.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public static void startActivityWithAnimAfterFinish(Activity packageContext, Class<?> cls, String tag, String extra) {
        Intent intent = new Intent(packageContext, cls);
        intent.putExtra(tag, extra);
        packageContext.startActivity(intent);
        packageContext.finish();
        packageContext.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public static void startActivityWithAnim(Activity packageContext, Class<?> cls) {
        packageContext.startActivity(new Intent(packageContext, cls));
        packageContext.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public static void startActivityWithAnim(Activity packageContext, Class<?> cls, String tag, String extra) {
        Intent intent = new Intent(packageContext, cls);
        intent.putExtra(tag, extra);
        packageContext.startActivity(intent);
        packageContext.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public static boolean hasFinger(Activity activity) {
        FingerprintManagerCompat fingerprintManagerCompat = FingerprintManagerCompat.from(activity);
        if (!fingerprintManagerCompat.isHardwareDetected()
                || !fingerprintManagerCompat.hasEnrolledFingerprints()) {
            return false;
        }
        return true;
    }
}
