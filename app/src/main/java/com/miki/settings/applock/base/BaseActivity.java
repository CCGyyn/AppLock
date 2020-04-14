package com.miki.settings.applock.base;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author：cai_gp on 2020/3/9
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getLayoutId() != 0) {
            setContentView(getLayoutId());
        }
        // 初始化控件
        initViews(savedInstanceState);
        initData();
        initAction();
        getSupportActionBar().hide();
        WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();

        localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);

    }

    public abstract int getLayoutId();

    protected abstract void initViews(Bundle savedInstanceState);

    protected abstract void initData();

    protected abstract void initAction();

}
