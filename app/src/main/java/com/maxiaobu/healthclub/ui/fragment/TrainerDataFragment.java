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
public class TrainerDataFragment extends BaseFrg {


    public TrainerDataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trainer_data, container, false);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }
}
