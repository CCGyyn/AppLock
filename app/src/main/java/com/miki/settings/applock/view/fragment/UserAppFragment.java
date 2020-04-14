package com.miki.settings.applock.view.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import com.miki.settings.applock.R;
import com.miki.settings.applock.adapter.MainAdapter;
import com.miki.settings.applock.base.BaseFragment;
import com.miki.settings.applock.bean.AppInfo;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UserAppFragment extends BaseFragment {

    public static UserAppFragment newInstance(List<AppInfo> list) {
        UserAppFragment userAppFragment = new UserAppFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("data", (ArrayList<? extends Parcelable>) list);
        userAppFragment.setArguments(bundle);
        return userAppFragment;
    }

    private RecyclerView mRecyclerView;
    private List<AppInfo> data,list;
    private MainAdapter mMainAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_app_list;
    }

    @Override
    protected void init(View rootView) {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        data = getArguments().getParcelableArrayList("data");
        mMainAdapter = new MainAdapter(getContext());
        list = new ArrayList<>();
        for (AppInfo info : data) {
            if (!info.isSysApp()) {
                list.add(info);
            }
        }
        mMainAdapter.setAppInfos(list);
        mRecyclerView.setAdapter(mMainAdapter);
    }
}
