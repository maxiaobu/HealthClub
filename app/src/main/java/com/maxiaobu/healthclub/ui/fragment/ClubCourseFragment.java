package com.maxiaobu.healthclub.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseFrg;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.adapter.AdapterClubPcourseFrg;
import com.maxiaobu.healthclub.adapter.AdapterGcourseFrg;
import com.maxiaobu.healthclub.adapter.AdapterPcourseFrg;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanCoachesDetail;
import com.maxiaobu.healthclub.common.beangson.BeanMbclub;
import com.maxiaobu.healthclub.ui.activity.WeekCourseActivity;
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
public class ClubCourseFragment extends BaseFrg {


    @Bind(R.id.iv_top)
    ImageView mIvTop;
    @Bind(R.id.tv_personal)
    TextView mTvPersonal;
    @Bind(R.id.rv_personal)
    RecyclerView mRvPersonal;
    @Bind(R.id.tv_group)
    TextView mTvGroup;
    @Bind(R.id.rv_group)
    RecyclerView mRvGroup;


    private  List<BeanMbclub.PcourseListBean>  mPcourseData;
    private AdapterClubPcourseFrg mPersonalAdapter;

    public ClubCourseFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_club_course, container, false);
        ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
    }

    @Override
    public void initView() {
        mPcourseData = new ArrayList<>();

        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        mIvTop.getLayoutParams().height = (int) (width / 2.6);
        mRvPersonal.setHasFixedSize(true);
        LinearLayoutManager layoutManagerPersonal = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRvPersonal.setLayoutManager(layoutManagerPersonal);
        mRvPersonal.setItemAnimator(new DefaultItemAnimator());
        mPersonalAdapter = new AdapterClubPcourseFrg(getActivity(), mPcourseData);
        mRvPersonal.setAdapter(mPersonalAdapter);
        mIvTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), WeekCourseActivity.class));
            }
        });

    }

    @Override
    public void initData() {
        RequestParams params = new RequestParams();
        params.put("pageIndex", "1");
        params.put("clubmemid", getActivity().getIntent().getStringExtra("tarid"));
        App.getRequestInstance().post(getActivity(), UrlPath.URL_CLUB_DETAIL,
                params, new RequestListener() {
                    @Override
                    public void requestSuccess(String s) {
                        BeanMbclub object = JsonUtils.object(s, BeanMbclub.class);
                        mPcourseData.addAll(object.getPcourseList());
                        final String clubid = object.getClubInfo().getClubid();
                        mIvTop.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), WeekCourseActivity.class);
                                intent.putExtra("clubid",clubid);
                                startActivity(intent);
                            }
                        });
                        if (mPcourseData.size() == 0)
                            mTvPersonal.setVisibility(View.GONE);

                            mTvGroup.setVisibility(View.GONE);
                        mPersonalAdapter.notifyDataSetChanged();
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
