package com.miki.applock.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.miki.applock.bean.LockSwitch;

import org.litepal.LitePal;

import java.util.List;

/**
 * @author：cai_gp on 2020/3/11
 */
public class LockSwitchManager {

    public synchronized List<LockSwitch> queryAll() {
        return LitePal.findAll(LockSwitch.class);
    }

    public synchronized Cursor queryAllBySQL() {
        Cursor cursor = LitePal.findBySQL("select * from LockSwitch");
        return cursor;
    }

    /**
     * 修改全部
     * @param status
     */
    public synchronized void updateStatus(boolean status) {
        ContentValues values = new ContentValues();
        values.put("lockstitch", status ? 1:0);
        LitePal.updateAll(LockSwitch.class, values);
    }

    public synchronized void insert(LockSwitch lockSwitch) {
        lockSwitch.save();
    }
}
