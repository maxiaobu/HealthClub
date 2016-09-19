package com.maxiaobu.healthclub.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseFrg;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.adapter.AdapterCourseOrderFrg;
import com.maxiaobu.healthclub.adapter.AdapterLunchOrderFrg;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanCorderList;
import com.maxiaobu.healthclub.common.beangson.BeanLunchOrderList;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseOrderFragment extends BaseFrg implements OnRefreshListener, OnLoadMoreListener {


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
    private AdapterCourseOrderFrg mAdapter;
    private List<BeanCorderList.CorderListBean> mData;

    public CourseOrderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_course_order, container, false);
        ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
    }

    @Override
    public void initView() {
// 设置WebView支持JavaScript
//        mWebView.getSettings().setJavaScriptEnabled(true);
        // 在js中调用本地java方法
//        mWebView.addJavascriptInterface(new BaseJsToAndroid(getActivity()), "mobile");
        // 在js中调用本地java方法
        // web_load.addJavascriptInterface(new
        // jsToAndroid_Find(),
        // "mobile");
        // 添加客户端支持
        // 设置进度条
        /*mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
               *//* if (newProgress == 100) {
                    // 隐藏进度条
//                    swipeLayout.setRefreshing(false);
                } else {
                    if (!swipeLayout.isRefreshing())
                        swipeLayout.setRefreshing(true);
                }*//*

                super.onProgressChanged(view, newProgress);
            }
        });
        // 设置WebView支持JavaScript
        mWebView.getSettings().setJavaScriptEnabled(true);
        // 在js中调用本地java方法
        // web_load.addJavascriptInterface(new
        // jsToAndroid_Find(),
        // "mobile");
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
        mWebView.loadUrl("file:///android_asset/corderList.html");
        // 设置WebView中的客户端的行为
        mWebView.setWebViewClient(new WebViewClient() {
            // 让WebView对点击网页中的URL做出响应
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // 页面加载完成 通过js回调函数实现获取页面信息
                view.loadUrl("javascript:window.mobile.loadPageData("
                        + "document.title,$('title').attr('isback'),$('title').attr('btn'),$('title').attr('navbar'))");
            }
        });*/
        mLoadType = 0;
        mCurrentPage = 1;
        mData = new ArrayList<>();
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mSwipeTarget.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mSwipeTarget.setLayoutManager(layoutManager);
        mSwipeTarget.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new AdapterCourseOrderFrg(getActivity(), mData);
        mSwipeTarget.setAdapter(mAdapter);
    }

    @Override
    public void initData() {
        RequestParams params = new RequestParams();
        params.put("pageIndex", String.valueOf(mCurrentPage));
        params.put("listtype", "corderlist");
        params.put("memid", SPUtils.getString(Constant.MEMID));
        App.getRequestInstance().post(getActivity(), UrlPath.URL_FOOD_ORDER_LIST, BeanCorderList.class, params, new RequestJsonListener<BeanCorderList>() {
            @Override
            public void requestSuccess(BeanCorderList result) {
                if (mLoadType == 0) {//刷新
                    if (result.getCorderList().size() == 0) {
                        mRlNoData.setVisibility(View.VISIBLE);
                        mTvNodataContent.setText("暂无订单");
                    }
                    mData.clear();
                    mData.addAll(result.getCorderList());
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
                    Toast.makeText(getActivity(), "刷新什么情况", Toast.LENGTH_SHORT).show();
                }
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

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        initData();
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

}
