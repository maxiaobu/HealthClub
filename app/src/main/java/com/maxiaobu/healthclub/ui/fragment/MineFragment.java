package com.maxiaobu.healthclub.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.hyphenate.EMCallBack;
import com.maxiaobu.healthclub.BaseFrg;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.chat.DemoHelper;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.ui.activity.BindClubListActivity;
import com.maxiaobu.healthclub.ui.activity.CoachcertApplyActivity;
import com.maxiaobu.healthclub.ui.activity.CourseManageActivity;
import com.maxiaobu.healthclub.ui.activity.LoginActivity;
import com.maxiaobu.healthclub.ui.activity.MineTeachingAppointmentActivity;
import com.maxiaobu.healthclub.ui.activity.MyBespeakActivity;
import com.maxiaobu.healthclub.ui.activity.OrderListActivity;
import com.maxiaobu.healthclub.ui.activity.ScheduleManagementActivity;
import com.maxiaobu.healthclub.ui.weiget.GlideCircleTransform;
import com.maxiaobu.healthclub.ui.weiget.pulltozoom.PullToZoomScrollViewEx;
import com.maxiaobu.healthclub.utils.storage.SPUtils;

/**
 * maxiaobu 2016-9-5
 */
public class MineFragment extends BaseFrg implements View.OnClickListener {
//    @Bind(R.id.iv_header)
    ImageView mIvHeader;
//    @Bind(R.id.tv_header_edit)
    TextView mTvHeaderEdit;
//    @Bind(R.id.tv_cart_num)
    TextView mTvCartNum;
//    @Bind(R.id.ly_cart)
    LinearLayout mLyCart;
//    @Bind(R.id.tv_order_num)
    TextView mTvOrderNum;
//    @Bind(R.id.ly_order)
    LinearLayout mLyOrder;
//    @Bind(R.id.tv_appointment_num)
    TextView mTvAppointmentNum;
//    @Bind(R.id.ly_appointment)
    LinearLayout mLyAppointment;
//    @Bind(R.id.tv_balance_num)
    TextView mTvBalanceNum;
//    @Bind(R.id.ly_balance)
    LinearLayout mLyBalance;
//    @Bind(R.id.ly_my_friend)
    LinearLayout mLyMyFriend;
//    @Bind(R.id.ly_persional_info)
    LinearLayout mLyPersionalInfo;
//    @Bind(R.id.ly_my_cart)
    LinearLayout mLyMyCart;
//    @Bind(R.id.ly_qiandao)
    LinearLayout mLyQiandao;
//    @Bind(R.id.ly_authentication)
    LinearLayout mLyAuthentication;
//    @Bind(R.id.ly_course_manage)
    LinearLayout mLyCourseManage;
//    @Bind(R.id.ly_teaching_appointment)
    LinearLayout mLyTeachingAppointment;
//    @Bind(R.id.ly_club_list)
    LinearLayout mLyClubList;
//    @Bind(R.id.ly_my_student)
    LinearLayout mLyMyStudent;
//    @Bind(R.id.ly_file_management)
    LinearLayout mLyFileManagement;
//    @Bind(R.id.ly_trainer_root)
    LinearLayout mLyTrainerRoot;
//    @Bind(R.id.ly_blacklist)
    LinearLayout mLyBlacklist;
//    @Bind(R.id.ly_set_top)
    LinearLayout mLySetTop;
//    @Bind(R.id.ly_login_out)
    LinearLayout mLyLoginOut;
//    @Bind(R.id.scroll_view)
//    ObservableScrollView mScrollView;
//    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
//    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;
//    @Bind(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;

//    @Bind(R.id.fragment_mine_pulltozoom_scrollview)
    PullToZoomScrollViewEx scrollViewEx;

    private View mRootView;

    private LinearLayout[] linearLayouts = {mLyCart,mLyOrder,mLyAppointment,
            mLyBalance,mLyMyFriend,mLyPersionalInfo,mLyMyCart,
            mLyQiandao,mLyAuthentication,mLyCourseManage,mLyTeachingAppointment,
            mLyClubList,mLyMyStudent,mLyFileManagement,mLyTrainerRoot,mLyBlacklist,
            mLySetTop,mLyLoginOut};

    private Integer[] ids = {R.id.ly_cart, R.id.ly_order, R.id.ly_appointment, R.id.ly_balance,
            R.id.ly_my_friend, R.id.ly_persional_info, R.id.ly_my_cart, R.id.ly_sign, R.id.ly_authentication,
            R.id.ly_course_manage, R.id.ly_teaching_appointment, R.id.ly_club_list, R.id.ly_my_student,
            R.id.ly_file_management, R.id.ly_trainer_root, R.id.ly_blacklist, R.id.ly_set_top, R.id.ly_login_out};

    public MineFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_mine, container, false);
        scrollViewEx = (PullToZoomScrollViewEx) mRootView.findViewById(R.id.fragment_mine_pulltozoom_scrollview);
        mTvTitleCommon = (TextView) mRootView.findViewById(R.id.tv_title_common);
        mToolbarCommon = (Toolbar) mRootView.findViewById(R.id.toolbar_common);
        mAppBarLayout = (AppBarLayout) mRootView.findViewById(R.id.app_bar_layout);
//        ButterKnife.bind(this, mRootView);
        initView();
        initData();
        return mRootView;
    }

    @Override
    public void initView() {

        loadViewForPullToZoomScrollView(scrollViewEx);

//        mScrollView.setScrollViewListener(this);

        mIvHeader = (ImageView) scrollViewEx.getHeaderView().findViewById(R.id.iv_header);
        mTvHeaderEdit = (TextView) scrollViewEx.getHeaderView().findViewById(R.id.tv_header_edit);
        mTvCartNum = (TextView) scrollViewEx.getPullRootView().findViewById(R.id.tv_cart_num);
        mTvOrderNum = (TextView) scrollViewEx.getPullRootView().findViewById(R.id.tv_order_num);
        mTvAppointmentNum  = (TextView) scrollViewEx.getPullRootView().findViewById(R.id.tv_appointment_num);
        mTvBalanceNum = (TextView) scrollViewEx.getPullRootView().findViewById(R.id.tv_balance_num);

        for (int i = 0; i < ids.length; i++) {
            linearLayouts[i] = (LinearLayout) scrollViewEx.getPullRootView().findViewById(ids[i]);
            linearLayouts[i].setOnClickListener(this);
        }

        mLyTrainerRoot = (LinearLayout) scrollViewEx.findViewById(R.id.ly_trainer_root);
        mLyAuthentication = (LinearLayout) scrollViewEx.findViewById(R.id.ly_authentication);

        // 判断身份的
        String memrole = SPUtils.getString(Constant.MEMROLE,"-1");
        if (memrole.equals("coach")) {
            mLyTrainerRoot.setVisibility(View.VISIBLE);
            mLyAuthentication.setVisibility(View.GONE);
        } else if (memrole.equals("mem")) {
            mLyTrainerRoot.setVisibility(View.GONE);
            mLyAuthentication.setVisibility(View.VISIBLE);
        }

        setPullToZoomViewLayoutParams(scrollViewEx);
    }

    private void loadViewForPullToZoomScrollView(PullToZoomScrollViewEx scrollView) {

        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mine_head_view, null);
        View zoomView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mine_head_zoom_view, null);
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mine_content_view, null);
        scrollView.setHeaderView(headView);
        scrollView.setZoomView(zoomView);
        scrollView.setScrollContentView(contentView);
    }


    // 设置头部的View的宽高。
    private void setPullToZoomViewLayoutParams(PullToZoomScrollViewEx scrollView) {
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
//        int mScreenHeight = localDisplayMetrics.heightPixels;
        int mScreenWidth = localDisplayMetrics.widthPixels;
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (9.8F * (mScreenWidth / 15.0F)));
        scrollView.setHeaderLayoutParams(localObject);
    }

    @Override
    public void initData() {
        Glide.with(getActivity()).load(SPUtils.getString(Constant.AVATAR))
                .transform(new GlideCircleTransform(getActivity()))
                .placeholder(R.mipmap.ic_place_holder).into(mIvHeader);
        mTvHeaderEdit.setText(SPUtils.getString(Constant.NICK_NAME));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ly_order:
                startActivity(new Intent(getActivity(), OrderListActivity.class));
                break;
            case R.id.ly_login_out:
                logout();

                break;
            //预约
            case R.id.ly_appointment:
                startActivity(new Intent(getActivity(), MyBespeakActivity.class));
                break;
            //申请认证
            case R.id.ly_authentication:
                startActivity(new Intent(getActivity(), CoachcertApplyActivity.class));
                break;
            //俱乐部列表
            case R.id.ly_club_list:
                startActivity(new Intent(getActivity(), BindClubListActivity.class));
                break;
            //课程管理
            case R.id.ly_course_manage:
                startActivity(new Intent(getActivity(), CourseManageActivity.class));
                break;
            //教学预约
            case R.id.ly_teaching_appointment:
                startActivity(new Intent(getActivity(), MineTeachingAppointmentActivity.class));
                break;
            case R.id.ly_file_management:
                startActivity(new Intent(getActivity(), ScheduleManagementActivity.class));
                break;
            default:
                break;
        }

    }

    public static Fragment newInstance() {
        MineFragment fragment = new MineFragment();
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        ButterKnife.unbind(this);
    }


//    @Override
//    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
////        float a = y;
////        float b = a / 1000;
////        float max = (float) Math.max(0, 1 - b);
//////        Log.d("MineFragment", String.valueOf(b));
////        mAppBarLayout.setAlpha(max);
//    }

    public void logout(){
        new MaterialDialog.Builder(getActivity())
                .title("退出登录")
                .positiveText("确定")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        DemoHelper.getInstance().logout(true, new EMCallBack() {
                            @Override
                            public void onSuccess() {
                                SPUtils.clearAllData(getActivity());
                                getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                                getActivity().finish();
                            }

                            @Override
                            public void onError(int i, String s) {
                                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onProgress(int i, String s) {
                                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                }).show();
    }

}
