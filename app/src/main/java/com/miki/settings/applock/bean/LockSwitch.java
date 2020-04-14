package com.miki.settings.applock.bean;

/**
 * @author：cai_gp on 2020/3/11
 */
public class LockSwitch{

    private long id;
    private boolean lockstitch;

    public LockSwitch() {
    }

    public LockSwitch(long id, boolean lockstitch) {
        this.id = id;
        this.lockstitch = lockstitch;
    }

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
