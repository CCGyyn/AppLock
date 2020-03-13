package com.miki.applock.bean;

import org.litepal.crud.LitePalSupport;

/**
 * @author：cai_gp on 2020/3/10
 */
public class LockInfo extends LitePalSupport{

    private long id;
    private String packageName;
    private boolean isLock;

    public LockInfo() {
    }

    public LockInfo(String packageName) {
        this.packageName = packageName;
        this.isLock = true;
    }

    public LockInfo(long id, String packageName, boolean isLock) {
        this.id = id;
        this.packageName = packageName;
        this.isLock = isLock;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isLock() {
        return isLock;
    }

    public void setLock(boolean lock) {
        isLock = lock;
    }

}
