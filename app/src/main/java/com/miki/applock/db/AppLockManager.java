package com.miki.applock.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.miki.applock.bean.LockInfo;

import org.litepal.LitePal;

import java.util.List;

/**
 * @author：cai_gp on 2020/3/10
 */
public class AppLockManager {

    public synchronized List<LockInfo> queryAllLockApp() {
        List<LockInfo> list = LitePal.findAll(LockInfo.class);
        return list;
    }

    public synchronized LockInfo queryById(long id) {
        return LitePal.find(LockInfo.class, id);
    }

    public synchronized Cursor queryBySQLWithPkg(String packageName) {
        Cursor cursor = LitePal.findBySQL("select * from LockInfo " +
                "where packageName = ?", packageName);
        return cursor;
    }

    public synchronized void insertLockInfo(String packageName) {
        LockInfo lockInfo = new LockInfo(packageName);
        lockInfo.save();
    }

    public synchronized void deleteLockInfoByPkg(String packageName) {
        LitePal.deleteAll(LockInfo.class, "packageName = ?", packageName);
    }

    public synchronized void deleteLockInfoById(long id) {
        LitePal.delete(LockInfo.class, id);
    }

    @Deprecated
    public synchronized void deleteLockInfoByIds(String ids) {
        LitePal.deleteAll(LockInfo.class, "id in ?", ids);
    }

    public synchronized void updateLockInfoById(long id, boolean lock) {
        ContentValues values = new ContentValues();
        values.put("isLock", lock ? 1:0);
        LitePal.update(LockInfo.class, values, id);
    }

    public synchronized int updateAllStatus(boolean lock) {
        ContentValues values = new ContentValues();
        values.put("isLock", lock ? 1:0);
        return LitePal.updateAll(LockInfo.class, values);
    }

    public synchronized int updateAllStatus(ContentValues values) {
        return LitePal.updateAll(LockInfo.class, values);
    }

}
