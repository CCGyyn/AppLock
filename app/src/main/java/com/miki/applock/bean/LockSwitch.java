package com.miki.applock.bean;

import org.litepal.crud.LitePalSupport;

/**
 * @author：cai_gp on 2020/3/11
 */
public class LockSwitch extends LitePalSupport {

    private long id;
    private boolean lockstitch;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isLockstitch() {
        return lockstitch;
    }

    public void setLockstitch(boolean lockstitch) {
        this.lockstitch = lockstitch;
    }
}
