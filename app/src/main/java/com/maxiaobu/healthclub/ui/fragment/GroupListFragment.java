package com.maxiaobu.healthclub.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maxiaobu.healthclub.BaseFrg;
import com.maxiaobu.healthclub.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupListFragment extends BaseFrg {


    private View mRootView;

    public GroupListFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_group_list, container, false);
        initView();
        initData();

        return mRootView;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    public static Fragment newInstance() {
        GroupListFragment fragment = new GroupListFragment();
        return fragment;
    }
}
