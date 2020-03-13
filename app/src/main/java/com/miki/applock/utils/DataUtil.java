package com.miki.applock.utils;

import com.miki.applock.bean.AppInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author：cai_gp on 2020/3/10
 */
public class DataUtil {

    public static List<AppInfo> clearRepeatCommInfo(List<AppInfo> appInfos) {
        HashMap<String, AppInfo> hashMap = new HashMap<>();
        for (AppInfo lockInfo : appInfos) {
            if (!hashMap.containsKey(lockInfo.getPackageName())) {
                hashMap.put(lockInfo.getPackageName(), lockInfo);
            }
        }
        List<AppInfo> commonAppInfos = new ArrayList<>();
        for (HashMap.Entry<String, AppInfo> entry : hashMap.entrySet()) {
            commonAppInfos.add(entry.getValue());
        }
        return commonAppInfos;
    }

}