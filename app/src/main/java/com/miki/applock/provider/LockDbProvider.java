package com.miki.applock.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.miki.applock.bean.LockInfo;
import com.miki.applock.bean.LockSwitch;
import com.miki.applock.db.AppLockManager;
import com.miki.applock.db.LockSwitchManager;

/**
 * @author：cai_gp on 2020/3/10
 */
public class LockDbProvider extends ContentProvider {

    private static final int LOCK_INFO = 0;
    private static final int LOCK_SWITCH = 1;
    private static final String AUTHORITY = "com.miki.applock.provider";

    private static UriMatcher uriMatcher;

    private AppLockManager appLockManager;
    private LockSwitchManager lockSwitchManager;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, LockInfo.class.getSimpleName(),
                LOCK_INFO);
        uriMatcher.addURI(AUTHORITY, LockSwitch.class.getSimpleName(),
                LOCK_SWITCH);
    }

    public LockDbProvider() {
    }

    @Override
    public boolean onCreate() {
        appLockManager = new AppLockManager();
        lockSwitchManager = new LockSwitchManager();
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        switch (uriMatcher.match(uri)) {
            case LOCK_INFO: {
                if(selection != null && selection.contains("packageName")) {
                    Cursor cursor = appLockManager.queryBySQLWithPkg(selectionArgs[0]);
                    return cursor;
                }
                break;
            }
            case LOCK_SWITCH: {
                Cursor cursor = lockSwitchManager.queryAllBySQL();
                return cursor;
            }
            default:
                break;
        }
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case LOCK_INFO: {
                // 全部重新上锁
                return appLockManager.updateAllStatus(true);
            }
            default:
                break;
        }
        return 0;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case LOCK_INFO: {
                return getDirMIME(LockInfo.class.getSimpleName());
            }
            case LOCK_SWITCH: {
                return getDirMIME(LockSwitch.class.getSimpleName());
            }
            default:
                break;
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private String getDirMIME(String path) {
        StringBuffer sb = new StringBuffer("vnd.android.cursor.dir/vnd.");
        sb.append(AUTHORITY);
        sb.append(".");
        sb.append(path);
        return sb.toString();
    }

    private String getItemMIME(String path) {
        StringBuffer sb = new StringBuffer("vnd.android.cursor.item/vnd.");
        sb.append(AUTHORITY);
        sb.append(".");
        sb.append(path);
        return sb.toString();
    }
}
