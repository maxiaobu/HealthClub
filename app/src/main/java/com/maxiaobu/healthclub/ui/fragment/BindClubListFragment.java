package com.maxiaobu.healthclub.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseFrg;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.adapter.AdapterUnbindClubListfrg;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanClubList;
import com.maxiaobu.healthclub.common.beangson.BeanCoachesListAty;
import com.maxiaobu.healthclub.ui.activity.ClubDetailActivity;
import com.maxiaobu.healthclub.ui.weiget.refresh.LoadMoreFooterView;
import com.maxiaobu.healthclub.ui.weiget.refresh.RefreshHeaderView;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.healthclub.volleykit.NodataFragment;
import com.maxiaobu.healthclub.volleykit.RequestJsonListener;
import com.maxiaobu.healthclub.volleykit.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class BindClubListFragment extends BaseFrg
        implements OnRefreshListener, OnLoadMoreListener, View.OnClickListener{


    @Bind(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @Bind(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @Bind(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @Bind(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;

    /**
     * 0刷新  1加载
     */
    private int mDataType;
    private int mCurrentPage;

    List<BeanCoachesListAty.CoachListBean> mData;
    private AdapterUnbindClubListfrg mAdapter;
    public BindClubListFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bind_club_list, container, false);
        ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
    }

    @Override
    public void initView() {
        mCurrentPage = 1;
        mDataType = 0;
        mData = new ArrayList<>();
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mSwipeTarget.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mSwipeTarget.setLayoutManager(layoutManager);
        mSwipeTarget.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new AdapterUnbindClubListfrg(getActivity(), mData);
        mSwipeTarget.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new AdapterUnbindClubListfrg.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String tarid) {
                Intent intent = new Intent(getActivity(),
                        ClubDetailActivity.class);
                intent.putExtra("tarid", tarid);
                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {
        RequestParams params = new RequestParams();
        params.put("pageIndex", String.valueOf(mCurrentPage)); //当前页码
        params.put("memid", SPUtils.getString(Constant.MEMID));//当前用户id
        params.put("latitude", SPUtils.getString(Constant.LATITUDE));
        params.put("longitude", SPUtils.getString(Constant.LONGITUDE));
//        params.put("sorttype", mSortType);
        App.getRequestInstance().post(getActivity(), UrlPath.URL_MBCLUBLIST, BeanClubList.class, params, new RequestJsonListener<BeanClubList>() {
            @Override
            public void requestSuccess(BeanClubList beanClubList) {

            }

            @Override
            public void requestAgain(NodataFragment nodataFragment) {
                nodataFragment.dismissAllowingStateLoss();
            }
        });
    }

    @OnClick({})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            default:
                break;
        }
    }

    @Override
    public void onLoadMore() {
        mCurrentPage++;
        mDataType = 1;
        mSwipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        });
    }

    @Override
    public void onRefresh() {
        mCurrentPage = 1;
        mDataType = 0;
        if (mSwipeToLoadLayout != null) {
            mSwipeToLoadLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    initData();
                }
            }, 2);
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
