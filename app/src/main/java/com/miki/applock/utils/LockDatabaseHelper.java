package com.miki.applock.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * @author：cai_gp on 2020/3/27
 */
public class LockDatabaseHelper extends SQLiteOpenHelper {

    private volatile static LockDatabaseHelper mLockDatabaseHelper = null;

    private static final String CREATE_LOCK_INFO = "create table LockInfo(" +
            "id integer primary key autoincrement," +
            "packageName text," +
            "isLock integer)";

    private static final String CREATE_LOCK_SWITCH = "create table LockSwitch(" +
            "id integer primary key autoincrement," +
            "lockstitch integer)";

    private LockDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static LockDatabaseHelper getInstance(Context context) {
        if(mLockDatabaseHelper == null) {
            synchronized (LockDatabaseHelper.class) {
                if(mLockDatabaseHelper == null) {
                    mLockDatabaseHelper = new LockDatabaseHelper(context, "Lock.db",
                            null, 1);
                }
            }
        }
        return mLockDatabaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_LOCK_INFO);
        db.execSQL(CREATE_LOCK_SWITCH);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
