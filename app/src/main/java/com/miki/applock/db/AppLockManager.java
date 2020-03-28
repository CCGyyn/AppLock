package com.miki.applock.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.miki.applock.bean.LockInfo;
import com.miki.applock.utils.LockDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author：cai_gp on 2020/3/10
 */
public class AppLockManager {

    private SQLiteDatabase db = null;
    private String table = "LockInfo";

    public AppLockManager(Context context) {
        db = LockDatabaseHelper.getInstance(context.getApplicationContext()).getWritableDatabase();
    }

    public synchronized List<LockInfo> queryAllLockApp() {
        List<LockInfo> list = new ArrayList<>();
        Cursor cursor = db.query(table, null, null, null, null,
                null, null);
        if(cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndex("id"));
                String packageName = cursor.getString(cursor.getColumnIndex("packageName"));
                boolean isLock = cursor.getInt(cursor.getColumnIndex("isLock")) == 1;
                LockInfo lockInfo = new LockInfo(id, packageName, isLock);
                list.add(lockInfo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public synchronized LockInfo queryById(long id) {
        Cursor cursor = db.query(table, null, "id = ?", new String[]{String.valueOf(id)},
                null, null, null);
        if(cursor.moveToFirst()) {
            do {
                long id2 = cursor.getLong(cursor.getColumnIndex("id"));
                String packageName = cursor.getString(cursor.getColumnIndex("packageName"));
                boolean isLock = cursor.getInt(cursor.getColumnIndex("isLock")) == 1;
                if(id2 == id) {
                    LockInfo lockInfo = new LockInfo(id, packageName, isLock);
                    cursor.close();
                    return lockInfo;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return null;
    }

    public synchronized Cursor queryBySQLWithPkg(String packageName) {
        Cursor cursor = db.rawQuery("select * from LockInfo " +
                "where packageName = ?", new String[]{packageName});
        return cursor;
    }

    public synchronized void insertLockInfo(String packageName) {
        ContentValues values = new ContentValues();
        values.put("packageName", packageName);
        values.put("isLock", 1);
        db.insert(table, null, values);
    }

    public synchronized void deleteLockInfoByPkg(String packageName) {
        db.delete(table, "packageName = ?", new String[]{packageName});
    }

    public synchronized void deleteLockInfoById(long id) {
        db.delete(table, "id = ?", new String[]{String.valueOf(id)});
    }

    public synchronized void updateLockInfoById(long id, boolean lock) {
        ContentValues values = new ContentValues();
        values.put("isLock", lock ? 1:0);
        db.update(table, values, "id = ?", new String[]{String.valueOf(id)});
    }

    public synchronized int updateLockInfoById(long id, ContentValues values) {
        return db.update(table, values, "id = ?", new String[]{String.valueOf(id)});
    }

    public synchronized int updateAllStatus(ContentValues values) {
        return db.update(table, values, null, null);
    }

}
