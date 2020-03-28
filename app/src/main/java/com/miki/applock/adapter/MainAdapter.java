package com.miki.applock.adapter;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.miki.applock.R;
import com.miki.applock.bean.AppInfo;
import com.miki.applock.db.AppLockManager;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {

    private List<AppInfo> appInfos = new ArrayList<>();
    private Context mContext;
    private PackageManager packageManager;
    private AppLockManager appLockManager;

    public MainAdapter(Context mContext) {
        this.mContext = mContext;
        this.packageManager = mContext.getPackageManager();
        this.appLockManager = new AppLockManager(mContext);
    }

    public void setAppInfos(List<AppInfo> appInfos) {
        this.appInfos.clear();
        this.appInfos.addAll(appInfos);
        notifyDataSetChanged();
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_list, parent, false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MainViewHolder holder, final int position) {
        final AppInfo appinfo = appInfos.get(position);
        initData(holder.mAppName, holder.mAppSwitch, holder.mAppIcon, appinfo);
        holder.mAppSwitch.setOnClickListener((v) -> changeItemLockStatus(holder.mAppSwitch, appinfo));
    }

    /**
     * 初始化数据
     */
    private void initData(TextView tvAppName, CheckBox appSwitch, ImageView mAppIcon, AppInfo info) {
        tvAppName.setText(packageManager.getApplicationLabel(info.getApplicationInfo()));
        appSwitch.setChecked(info.isLocked());
        ApplicationInfo applicationInfo = info.getApplicationInfo();
        mAppIcon.setImageDrawable(packageManager.getApplicationIcon(applicationInfo));
    }

    public void changeItemLockStatus(CheckBox checkBox, AppInfo info) {
        if (checkBox.isChecked()) {
            info.setLocked(true);
            appLockManager.insertLockInfo(info.getPackageName());
        } else {
            info.setLocked(false);
            appLockManager.deleteLockInfoByPkg(info.getPackageName());
        }
    }

    @Override
    public int getItemCount() {
        return appInfos.size();
    }

    public class MainViewHolder extends RecyclerView.ViewHolder {
        private ImageView mAppIcon;
        private TextView mAppName;
        private CheckBox mAppSwitch;

        public MainViewHolder(View itemView) {
            super(itemView);
            mAppIcon = (ImageView) itemView.findViewById(R.id.app_icon);
            mAppName = (TextView) itemView.findViewById(R.id.app_name);
            mAppSwitch = (CheckBox) itemView.findViewById(R.id.app_switch);
        }
    }
}
