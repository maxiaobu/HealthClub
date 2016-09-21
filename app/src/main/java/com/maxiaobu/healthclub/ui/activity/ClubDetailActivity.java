package com.maxiaobu.healthclub.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.chat.DemoHelper;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanMbclub;
import com.maxiaobu.healthclub.ui.fragment.ClubCourseFragment;
import com.maxiaobu.healthclub.ui.fragment.ClubDataFragment;
import com.maxiaobu.healthclub.ui.fragment.ClubDynamicFragment;
import com.maxiaobu.healthclub.ui.weiget.GlideCircleTransform;
import com.maxiaobu.healthclub.ui.weiget.toolsbar.MyNestedScrollView;
import com.maxiaobu.healthclub.ui.weiget.toolsbar.WrapContentHeightViewPager;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.healthclub.volleykit.JsonUtils;
import com.maxiaobu.healthclub.volleykit.NodataFragment;
import com.maxiaobu.healthclub.volleykit.RequestListener;
import com.maxiaobu.healthclub.volleykit.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClubDetailActivity extends BaseAty implements AppBarLayout.OnOffsetChangedListener, View.OnClickListener {

    @Bind(R.id.iv_header)
    ImageView mIvHeader;
    @Bind(R.id.tv_name)
    TextView mTvName;
    @Bind(R.id.tv_fans)
    TextView mTvFans;
    @Bind(R.id.tv_follow)
    TextView mTvFollow;
    @Bind(R.id.tv_order)
    TextView mTvOrder;
    @Bind(R.id.tv_signature)
    TextView mTvSignature;
    @Bind(R.id.ly_bar)
    LinearLayout mLyBar;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.ctl_name)
    CollapsingToolbarLayout mCtlName;
    @Bind(R.id.tabs)
    TabLayout mTabs;
    @Bind(R.id.app_bar)
    AppBarLayout mAppBar;
    @Bind(R.id.viewpager)
    WrapContentHeightViewPager mViewpager;
    @Bind(R.id.lin)
    LinearLayout mLin;
    @Bind(R.id.nested_scroll_view)
    MyNestedScrollView mNestedScrollView;
    @Bind(R.id.fab_pull_black)
    FloatingActionButton mFabPullBlack;
    @Bind(R.id.fab_subscribe)
    FloatingActionButton mFabSubscribe;
    @Bind(R.id.fab_talk)
    FloatingActionButton mFabTalk;
    @Bind(R.id.fab_menu)
    FloatingActionMenu mFabMenu;
    @Bind(R.id.fab_bind)
    FloatingActionButton mFabBind;

    private Handler mUiHandler = new Handler();
    private int mPreviousVisibleItem;
    private String mNickname = "";
    private String mClubid = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_detail);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAppBar.addOnOffsetChangedListener(this);
        mCtlName.setTitle("");
        mFabMenu.hideMenuButton(false);
        mUiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFabMenu.showMenuButton(true);
            }
        }, 400);
        mNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > mPreviousVisibleItem + 20) {
                    mFabMenu.hideMenuButton(false);
                    mPreviousVisibleItem = scrollY;
                } else if (scrollY < mPreviousVisibleItem - 20) {
                    mFabMenu.showMenuButton(true);
                    mPreviousVisibleItem = scrollY;
                }
            }

        });
        if (mViewpager != null) {
            setupViewPager(mViewpager);
            mViewpager.setOffscreenPageLimit(1);
        }
        mTabs.setupWithViewPager(mViewpager);
    }

    @Override
    public void initData() {
        RequestParams params = new RequestParams();
        params.put("pageIndex", "1");
        params.put("clubmemid", getIntent().getStringExtra("tarid"));
//        Log.d("ClubDetailActivity", getIntent().getStringExtra("tarid"));
        App.getRequestInstance().post(this, UrlPath.URL_CLUB_DETAIL,
                params, new RequestListener() {
                    @Override
                    public void requestSuccess(String s) {

//                        Log.d("ClubDetailActivity", s);
                        BeanMbclub object = JsonUtils.object(s, BeanMbclub.class);
                        BeanMbclub.BBMemberBean mData = object.getBBMember();
                        mClubid=object.getClubInfo().getClubid();
                        Glide.with(ClubDetailActivity.this).load(mData.getImgsfilename())//mData.getImgsfilename()
                                .transform(new GlideCircleTransform(mActivity)).placeholder(R.mipmap.ic_place_holder).into(mIvHeader);
                        mTvFans.setText("粉丝:" + mData.getFollownum());//mData.getFollownum()
                        mTvFollow.setText("关注：" + mData.getConcernnum());//mData.getConcernnum()
                        mNickname = mData.getNickname();//mData.getNickname()
                        mCtlName.setTitle(mData.getNickname());//mData.getNickname()
                        mTvName.setText(mData.getNickname());//mData.getNickname()
                        mTvSignature.setText(mData.getSignature());//mData.getSignature()
                    }

                    @Override
                    public void requestAgain(NodataFragment nodataFragment) {
                        initData();
                    }
                });
    }

    /**
     * toolbar滑动监听
     *
     * @param appBarLayout
     * @param verticalOffset
     */
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//        Log.i("sdfjkh", "onOffsetChanged: "+verticalOffset);
//        Log.i("321321", "onOffsetChanged: "+ (-mCtlName.getHeight() ));
        if (verticalOffset <= -mCtlName.getHeight() + mToolbar.getHeight() + 180) {
            mCtlName.setTitle(mNickname);
            mTabs.setBackgroundResource(R.color.colorPrimary);
            mTabs.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));
        } else {
            mCtlName.setTitle("");
            mTabs.setBackgroundResource(R.color.white);
            mTabs.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new ClubDataFragment(), "资料");
        adapter.addFragment(new ClubCourseFragment(), "课程");
        adapter.addFragment(new ClubDynamicFragment(), "动态");
        viewPager.setAdapter(adapter);
    }

    @OnClick({R.id.fab_talk,R.id.fab_bind})
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.fab_talk:
                String userId = getIntent().getStringExtra("tarid");
                if (userId == SPUtils.getString(Constant.MEMID)) {
                    Toast.makeText(this, "自己不能和自己聊天", Toast.LENGTH_SHORT).show();
                } else {
                    String nickname = DemoHelper.getInstance().getUserInfo(userId).getNickname();
                    intent.putExtra(Constant.USER_ID, userId);
                    intent.putExtra(Constant.NICK_NAME, nickname);
                    intent.setClass(this, ChatActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.fab_bind:
                intent.setClass(this,ApplyBindClubActivity.class);
                        intent.putExtra("coachid",getIntent().getStringExtra("tarid"));
                        intent.putExtra("clubid",mClubid);

                startActivity(intent);
                break;
            default:
                break;
        }
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
