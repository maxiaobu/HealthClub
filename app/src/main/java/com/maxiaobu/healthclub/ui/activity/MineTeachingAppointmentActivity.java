package com.maxiaobu.healthclub.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.adapter.AdapterMyBespeak;
import com.maxiaobu.healthclub.adapter.AdapterTeachingAppointment;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanMcoachBespeak;
import com.maxiaobu.healthclub.common.beangson.BeanMmyBespeak;
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

public class MineTeachingAppointmentActivity extends BaseAty implements OnRefreshListener, OnLoadMoreListener {

    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;
    @Bind(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @Bind(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @Bind(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @Bind(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    @Bind(R.id.ivNoDataLogo)
    ImageView mIvNoDataLogo;
    @Bind(R.id.tv_nodata_content)
    TextView mTvNodataContent;
    @Bind(R.id.rlNoData)
    RelativeLayout mRlNoData;

    /**
     * 0刷新  1加载
     */
    private int mLoadType;
    private int mCurrentPage;
    private AdapterTeachingAppointment mAdapter;
    private List<BeanMcoachBespeak.CoachBespeaklistBean> mData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_teaching_appointment);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        setCommonBackToolBar(mToolbarCommon,mTvTitleCommon,"我的教学预约");
        mLoadType = 0;
        mCurrentPage = 1;
        mData = new ArrayList<>();
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mSwipeTarget.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mSwipeTarget.setLayoutManager(layoutManager);
        mSwipeTarget.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new AdapterTeachingAppointment(this, mData);
        mSwipeTarget.setAdapter(mAdapter);

    }

    @Override
    public void initData() {
        RequestParams params = new RequestParams();
        params.put("memid", SPUtils.getString( Constant.MEMID));
        App.getRequestInstance().post(this, UrlPath.URL_MCOACHBESPEAK, BeanMcoachBespeak.class, params, new RequestJsonListener<BeanMcoachBespeak>() {
            @Override
            public void requestSuccess(BeanMcoachBespeak beanMcoachBespeak) {
                if (mLoadType == 0) {//刷新
                    if (beanMcoachBespeak.getCoachBespeaklist().size() == 0) {
                        mRlNoData.setVisibility(View.VISIBLE);
                        mTvNodataContent.setText("暂无订单");
                    }
                    mData.clear();
                    mData.addAll(beanMcoachBespeak.getCoachBespeaklist());
                    mAdapter.notifyDataSetChanged();
                    if (mSwipeToLoadLayout != null) {
                        mSwipeToLoadLayout.setRefreshing(false);
                    }
                } else if (mLoadType == 1) {//加载更多
//                    int position = mAdapter.getItemCount();
//                    mData.addAll(object.getForderList());
//                    mAdapter.notifyItemRangeInserted(position, object.getForderList().size());
                    mSwipeToLoadLayout.setLoadingMore(false);
                } else {
                    Toast.makeText(MineTeachingAppointmentActivity.this, "刷新什么情况", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void requestAgain(NodataFragment nodataFragment) {
                initData();
            }
        } );
    }

    @Override
    public void onRefresh() {
        mCurrentPage = 1;
        mLoadType = 0;
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
    public void onLoadMore() {
        mCurrentPage++;
        mLoadType = 1;
        mSwipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        initData();
    }
}
