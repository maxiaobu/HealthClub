package com.maxiaobu.healthclub.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseFrg;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.adapter.AdapterGcourseFrg;
import com.maxiaobu.healthclub.adapter.AdapterPcourseFrg;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanCoachesDetail;
import com.maxiaobu.healthclub.volleykit.JsonUtils;
import com.maxiaobu.healthclub.volleykit.NodataFragment;
import com.maxiaobu.healthclub.volleykit.RequestListener;
import com.maxiaobu.healthclub.volleykit.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CoachCourseFragment extends BaseFrg {
    @Bind(R.id.tv_personal)
    TextView mTvPersonal;
    @Bind(R.id.rv_personal)
    RecyclerView mRvPersonal;
    @Bind(R.id.tv_group)
    TextView mTvGroup;
    @Bind(R.id.rv_group)
    RecyclerView mRvGroup;

    private List<BeanCoachesDetail.PcourseListBean> mPcourseData;
    private List<BeanCoachesDetail.GcourseListBean> mGcourseData;
    private AdapterPcourseFrg mPersonalAdapter;
    private AdapterGcourseFrg mGroupAdapter;

    public CoachCourseFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coach_course, container, false);
        ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
    }

    @Override
    public void initView() {
        mPcourseData = new ArrayList<>();
        mGcourseData = new ArrayList<>();

        mRvPersonal.setHasFixedSize(true);
        LinearLayoutManager layoutManagerPersonal = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRvPersonal.setLayoutManager(layoutManagerPersonal);
        mRvPersonal.setItemAnimator(new DefaultItemAnimator());
        mPersonalAdapter = new AdapterPcourseFrg(getActivity(), mPcourseData);
        mRvPersonal.setAdapter(mPersonalAdapter);

        mRvGroup.setHasFixedSize(true);
        LinearLayoutManager layoutManagerGroup = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRvGroup.setLayoutManager(layoutManagerGroup);
        mRvGroup.setItemAnimator(new DefaultItemAnimator());
        mGroupAdapter = new AdapterGcourseFrg(getActivity(), mGcourseData);
        mRvGroup.setAdapter(mGroupAdapter);
    }

    @Override
    public void initData() {
        RequestParams params = new RequestParams();
//        params.put("pageIndex", "1");
        params.put("tarid", getActivity().getIntent().getStringExtra("tarid"));
        App.getRequestInstance().post(getActivity(), UrlPath.URL_COACHES_DETAIL,
                params, new RequestListener() {
            @Override
            public void requestSuccess(String s) {
                BeanCoachesDetail object = JsonUtils.object(s, BeanCoachesDetail.class);
                mPcourseData.addAll(object.getPcourseList());
                mGcourseData.addAll(object.getGcourseList());
                if (mPcourseData.size() == 0)
                    mTvPersonal.setVisibility(View.GONE);
                if (mGcourseData.size() == 0)
                    mTvGroup.setVisibility(View.GONE);
                mPersonalAdapter.notifyDataSetChanged();
                mGroupAdapter.notifyDataSetChanged();
            }

            @Override
            public void requestAgain(NodataFragment nodataFragment) {
                initData();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
