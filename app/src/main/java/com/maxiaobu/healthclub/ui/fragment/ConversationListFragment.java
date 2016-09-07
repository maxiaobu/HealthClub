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
public class ConversationListFragment extends BaseFrg {


    private View mRootView;

    public ConversationListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_conversation_list, container, false);
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
        ConversationListFragment fragment = new ConversationListFragment();
        return fragment;
    }
}
