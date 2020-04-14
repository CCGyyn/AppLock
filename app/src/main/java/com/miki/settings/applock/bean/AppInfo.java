package com.miki.settings.applock.bean;

import android.content.pm.ApplicationInfo;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author：cai_gp on 2020/3/10
 */
public class AppInfo implements Parcelable {

    private String packageName;
    private String appName;
    private boolean isLocked;
    private ApplicationInfo applicationInfo;
    private boolean isSysApp;

    public AppInfo(String packageName, String appName, ApplicationInfo applicationInfo, boolean isSysApp) {
        this.packageName = packageName;
        this.appName = appName;
        this.isLocked = false;
        this.applicationInfo = applicationInfo;
        this.isSysApp = isSysApp;
    }

    public AppInfo(String packageName, String appName, boolean isLocked, ApplicationInfo applicationInfo, boolean isSysApp) {
        this.packageName = packageName;
        this.appName = appName;
        this.isLocked = isLocked;
        this.applicationInfo = applicationInfo;
        this.isSysApp = isSysApp;
    }

    protected AppInfo(Parcel in) {
        packageName = in.readString();
        appName = in.readString();
        isLocked = in.readByte() != 0;
        applicationInfo = in.readParcelable(ApplicationInfo.class.getClassLoader());
        isSysApp = in.readByte() != 0;
    }

    public static final Creator<AppInfo> CREATOR = new Creator<AppInfo>() {
        @Override
        public AppInfo createFromParcel(Parcel in) {
            return new AppInfo(in);
        }

        @Override
        public AppInfo[] newArray(int size) {
            return new AppInfo[size];
        }
    };

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public ApplicationInfo getApplicationInfo() {
        return applicationInfo;
    }

    public void setApplicationInfo(ApplicationInfo applicationInfo) {
        this.applicationInfo = applicationInfo;
    }

    public boolean isSysApp() {
        return isSysApp;
    }

    public void setSysApp(boolean sysApp) {
        isSysApp = sysApp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(packageName);
        dest.writeString(appName);
        dest.writeByte((byte) (isLocked ? 1 : 0));
        dest.writeParcelable(applicationInfo, flags);
        dest.writeByte((byte) (isSysApp ? 1 : 0));
    }

}
