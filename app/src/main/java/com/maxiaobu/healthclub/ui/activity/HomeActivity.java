package com.maxiaobu.healthclub.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.MainActivity;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.ui.fragment.DiscoveryFragment;
import com.maxiaobu.healthclub.ui.fragment.HomeFragment;
import com.maxiaobu.healthclub.ui.fragment.MineFragment;
import com.maxiaobu.healthclub.ui.fragment.TalkFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeActivity extends BaseAty {


    @Bind(R.id.bottom_navigation_bar)
    BottomNavigationBar mBottomNavigationBar;
    @Bind(R.id.fragment_container)
    FrameLayout mFragmentContainer;

    private Runnable runnable;

    private Fragment nowFragment = new Fragment();
    private Fragment mHomeFragment;
    private Fragment mTalkFragment;
    private Fragment mDiscoveryFragment;
    private Fragment mMineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {

        BadgeItem numberBadgeItem = new BadgeItem()
                .setBorderWidth(4)
                .setBackgroundColorResource(R.color.white)
                .setTextColorResource(R.color.red)
                .setText("5");
        mBottomNavigationBar
                .setMode(BottomNavigationBar.MODE_FIXED)
//                .setBarBackgroundColor(R.color.white);
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.ic_home_home, "首页"))
                .addItem(new BottomNavigationItem(R.mipmap.ic_home_message, "Talk").setBadgeItem(numberBadgeItem))
                .addItem(new BottomNavigationItem(R.mipmap.ic_home_discover, "发现"))
                .addItem(new BottomNavigationItem(R.mipmap.ic_home_mine, "我的"))
                .initialise();
        mBottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                switchFragment(position);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
//        bottomNavigationBar.setBarBackgroundColor(R.color.white);

        mHomeFragment = HomeFragment.newInstance();
        mTalkFragment = TalkFragment.newInstance();
        mDiscoveryFragment = DiscoveryFragment.newInstance();
        mMineFragment = MineFragment.newInstance();
        switchContent(nowFragment, mHomeFragment);

    }

    @Override
    public void initData() {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if ((Intent.FLAG_ACTIVITY_CLEAR_TOP & intent.getFlags()) != 0) {
            if (intent.getIntExtra("foodFlag",-1)==1){
                Intent foodOrderIntent=new Intent();
                foodOrderIntent.putExtra("orderFlag",2);
                foodOrderIntent.setClass(HomeActivity.this,MainActivity.class);
                startActivity(foodOrderIntent);
            }
        }

        /*if (getIntent().getBooleanExtra("conflict", false)
                && !isConflictDialogShow) {
            showConflictDialog();
        } else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false)
                && !isAccountRemovedDialogShow) {
            showAccountRemovedDialog();
        }*/
    }

    /**
     * 底部导航跳转
     *
     * @param position
     */
    public void switchFragment(int position) {
        runnable = null;
        switch (position) {
            case 0:
                switchContent(nowFragment, mHomeFragment);
                break;
            case 1:
                switchContent(nowFragment, mTalkFragment);
                break;
            case 2:
                switchContent(nowFragment, mDiscoveryFragment);
                break;
            case 3:
                switchContent(nowFragment, mMineFragment);
                break;
            default:
                break;
        }

    }

    /**
     * 导航切换
     *
     * @param from
     * @param to
     */
    private void switchContent(Fragment from, Fragment to) {
        if (nowFragment != to) {
            nowFragment = to;

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            if (!to.isAdded()) {
                transaction.hide(from).add(R.id.fragment_container, to).commit();
            } else {
                transaction.hide(from).show(to).commit();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
