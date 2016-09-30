package com.maxiaobu.healthclub.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.ui.fragment.CourseOrderListFragment;
import com.maxiaobu.healthclub.ui.fragment.LunchOrderFragment;
import com.maxiaobu.healthclub.ui.fragment.MsjOrderFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OrderListActivity extends BaseAty {

    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;
    @Bind(R.id.tabs)
    TabLayout mTabs;
    @Bind(R.id.viewpager)
    ViewPager mViewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        int foodFlag = getIntent().getIntExtra("orderFlag", -1);
        setCommonBackToolBar(mToolbarCommon,mTvTitleCommon,"订单列表");
        if (mViewpager != null) {
            setupViewPager(mViewpager);
            mViewpager.setOffscreenPageLimit(1);
        }
        mTabs.setupWithViewPager(mViewpager);
        if (foodFlag!=-1){
            mViewpager.setCurrentItem(foodFlag,true);
        }
    }

    @Override
    public void initData() {

    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new CourseOrderListFragment(), "课程");
        adapter.addFragment(new LunchOrderFragment(), "配餐");
        adapter.addFragment(new MsjOrderFragment(), "按摩");
        viewPager.setAdapter(adapter);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
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
