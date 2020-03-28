package com.miki.applock.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.miki.applock.bean.LockSwitch;
import com.miki.applock.utils.LockDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author：cai_gp on 2020/3/11
 */
public class LockSwitchManager {

    private SQLiteDatabase db = null;
    private String table = "LockSwitch";

    public LockSwitchManager(Context context) {
        db = LockDatabaseHelper.getInstance(context.getApplicationContext()).getWritableDatabase();
    }

    public synchronized List<LockSwitch> queryAll() {
        List<LockSwitch> list = new ArrayList<>();
        Cursor cursor = db.query(table, null, null, null, null,
                null, null);
        if(cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndex("id"));
                boolean lockstitch = cursor.getInt(cursor.getColumnIndex("lockstitch")) == 1;
                LockSwitch lockSwitch = new LockSwitch(id, lockstitch);
                list.add(lockSwitch);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public synchronized Cursor queryAllBySQL() {
        Cursor cursor = db.rawQuery("select * from LockSwitch", null);
        return cursor;
    }

    /**
     * 修改全部
     * @param status
     */
    public synchronized void updateStatus(boolean status) {
        ContentValues values = new ContentValues();
        values.put("lockstitch", status ? 1:0);
        db.update(table, values, null, null);
    }

    public synchronized void insert(LockSwitch lockSwitch) {
        ContentValues values = new ContentValues();
        values.put("lockstitch", lockSwitch.isLockstitch() ? 1:0);
        db.insert(table, null, values);
    }
}
