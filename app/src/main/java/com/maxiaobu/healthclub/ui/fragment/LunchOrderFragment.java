package com.maxiaobu.healthclub.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseFrg;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.adapter.AdapterLunchOrderFrg;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanLunchOrderList;
import com.maxiaobu.healthclub.ui.activity.CateringDetailActivity;
import com.maxiaobu.healthclub.ui.weiget.refresh.LoadMoreFooterView;
import com.maxiaobu.healthclub.ui.weiget.refresh.RefreshHeaderView;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.healthclub.volleykit.JsonUtils;
import com.maxiaobu.healthclub.volleykit.NodataFragment;
import com.maxiaobu.healthclub.volleykit.RequestListener;
import com.maxiaobu.healthclub.volleykit.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class LunchOrderFragment extends BaseFrg implements OnRefreshListener, OnLoadMoreListener {


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
    private AdapterLunchOrderFrg mAdapter;
    private List<BeanLunchOrderList.ForderListBean> mData;

    public LunchOrderFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lunch_order, container, false);
        ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
    }

    @Override
    public void initView() {
        mLoadType = 0;
        mCurrentPage = 1;
        mData = new ArrayList<>();
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mSwipeTarget.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mSwipeTarget.setLayoutManager(layoutManager);
        mSwipeTarget.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new AdapterLunchOrderFrg(getActivity(), mData, this);
        mSwipeTarget.setAdapter(mAdapter);
        mAdapter.setOnCancelItemClickListener(new AdapterLunchOrderFrg.OnCancelItemClickListener() {
            @Override
            public void onItemClick(View view, final String what) {
                new MaterialDialog.Builder(getActivity())
                        .title("确定删除订单？")
                        .positiveColor(getResources().getColor(R.color.colorTextPrimary))
                        .positiveText("确认")

                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                                //http://192.168.1.121:8080/efithealth/mcancelForder.do?ordno=FO-20160726-170
                                // {"msgFlag":"1","msgContent":"取消订单成功"}
                                App.getRequestInstance().post(getActivity(),
                                        UrlPath.URL_CANCEL_ORDER, new RequestParams("ordno", what),
                                        new RequestListener() {
                                    @Override
                                    public void requestSuccess(String s) {
                                        try {
                                            JSONObject object = new JSONObject(s);
                                            Toast.makeText(getActivity(), object.get("msgContent").toString(), Toast.LENGTH_SHORT).show();
                                            mCurrentPage = 1;
                                            mLoadType = 0;
                                            initData();
                                        } catch (JSONException e) {
                                            Toast.makeText(getActivity(), "接口变了，我告诉我凹", Toast.LENGTH_SHORT).show();
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void requestAgain(NodataFragment nodataFragment) {
                                        nodataFragment.dismissAllowingStateLoss();
                                    }

                                });
                            }
                        })
                        .negativeColor(getResources().getColor(R.color.colorTextPrimary))
                        .negativeText("取消")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        mAdapter.setOnDelayItemClickListener(new AdapterLunchOrderFrg.OnDelayItemClickListener() {
            @Override
            public void onItemClick(View view, final String what) {
                new MaterialDialog.Builder(getActivity())
                        .title("确定延期？")
                        .positiveColor(getResources().getColor(R.color.colorTextPrimary))
                        .positiveText("确认")

                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                                //http://192.168.1.121:8080/efithealth/mextension.do?ordno=FO-20160726-170
                                //{"msgFlag":"1","msgContent":""}
                                //msgFlag：1成功了 0失败了
                                App.getRequestInstance().post(getActivity(), UrlPath.URL_DELAY_ORDER, new RequestParams("ordno", what), new RequestListener() {
                                    @Override
                                    public void requestSuccess(String s) {
                                        try {
                                            JSONObject object = new JSONObject(s);
                                            Toast.makeText(getActivity(), object.get("msgContent").toString(), Toast.LENGTH_SHORT).show();
                                            mCurrentPage = 1;
                                            mLoadType = 0;
                                            initData();
                                        } catch (JSONException e) {
                                            Toast.makeText(getActivity(), "接口变了，得告诉我凹", Toast.LENGTH_SHORT).show();
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void requestAgain(NodataFragment nodataFragment) {
                                        nodataFragment.dismissAllowingStateLoss();
                                    }

                                });
                            }
                        })
                        .negativeColor(getResources().getColor(R.color.colorTextPrimary))
                        .negativeText("取消")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        mAdapter.setOnAgainItemClickListener(new AdapterLunchOrderFrg.OnAgainItemClickListener() {
            @Override
            public void onItemClick(View view, String what) {
                Intent intent = new Intent(getActivity(),
                        CateringDetailActivity.class);
                intent.putExtra("merid", what);
                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {
        RequestParams params = new RequestParams();
        params.put("listtype", "forderlist");
        params.put("memid", SPUtils.getString( Constant.MEMID));
        App.getRequestInstance().post(getActivity(), UrlPath.URL_FOOD_ORDER_LIST, params, new RequestListener() {
            @Override
            public void requestSuccess(String s) {
                BeanLunchOrderList object = JsonUtils.object(s, BeanLunchOrderList.class);
                if (mLoadType == 0) {//刷新
                    if (object.getForderList().size()==0){
                        mRlNoData.setVisibility(View.VISIBLE);
                        mTvNodataContent.setText("暂无订单");
                    }
                    mData.clear();
                    mData.addAll(object.getForderList());
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2 && resultCode == 2)
            initData();
        super.onActivityResult(requestCode, resultCode, data);
    }
}
