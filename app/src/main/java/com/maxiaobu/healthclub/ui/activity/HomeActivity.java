package com.maxiaobu.healthclub.ui.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.hyphenate.util.EMLog;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.chat.Constant;
import com.maxiaobu.healthclub.chat.DemoHelper;
import com.maxiaobu.healthclub.chat.db.InviteMessgeDao;
import com.maxiaobu.healthclub.chat.runtimepermissions.PermissionsManager;
import com.maxiaobu.healthclub.chat.runtimepermissions.PermissionsResultAction;
import com.maxiaobu.healthclub.ui.fragment.ConversationListFragment;
import com.maxiaobu.healthclub.ui.fragment.DiscoveryFragment;
import com.maxiaobu.healthclub.ui.fragment.HomeFragment;
import com.maxiaobu.healthclub.ui.fragment.MineFragment;
import com.maxiaobu.healthclub.ui.fragment.TalkFragment;
import com.maxiaobu.healthclub.utils.HealthUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeActivity extends BaseAty {


    @Bind(R.id.bottom_navigation_bar)
    BottomNavigationBar mBottomNavigationBar;
    @Bind(R.id.fragment_container)
    FrameLayout mFragmentContainer;
    @Bind(R.id.view_point)
    View mViewPoint;

    private Runnable runnable;

    private Fragment nowFragment = new Fragment();
    private Fragment mHomeFragment;
    private Fragment mTalkFragment;
    private Fragment mDiscoveryFragment;
    private Fragment mMineFragment;

    /**
     * 用户在其他设备登录
     */
    public boolean isConflict = false;
    /**
     * 用户账户被移除
     */
    private boolean isCurrentAccountRemoved = false;
    /**
     * 在其他设备登录 dialog
     */
    private AlertDialog.Builder conflictBuilder;
    /**
     * 账户移除 dialog
     */
    private AlertDialog.Builder accountRemovedBuilder;
    private boolean isConflictDialogShow;//在其他设备登录 dialog显示中
    private boolean isAccountRemovedDialogShow;//如果账户移除 dialog显示中
    //false：当前为首页 true:当前非首页
    private boolean isHomePage = false;
    //是否可以退出应用
    private boolean isExit = true;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = true;
        }
    };
    /**
     * 消息数据库
     */
    private InviteMessgeDao inviteMessgeDao;
    /**
     * 会话列表frg
     */
    private ConversationListFragment conversationListFragment;

    /**
     * 应用内广播管理者
     */
    private LocalBroadcastManager broadcastManager;
    private BroadcastReceiver broadcastReceiver;
    private BroadcastReceiver internalDebugReceiver;//可忽略
    private BadgeItem mNumberBadgeItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.getBoolean(Constant.ACCOUNT_REMOVED, false)) {
            //账户移除
            DemoHelper.getInstance().logout(false, null);
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        } else if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false)) {
            //在其他设备登录
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        // 全部运行权限
        requestPermissions();
        initView();
        initData();
    }

    @Override
    public void initView() {
        if (getIntent().getBooleanExtra(Constant.ACCOUNT_CONFLICT, false) && !isConflictDialogShow) {
            //处理在其他设备登录
            showConflictDialog();
        } else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false) && !isAccountRemovedDialogShow) {
            //如果账户移除
            showAccountRemovedDialog();
        }
        mBottomNavigationBar
                .setMode(BottomNavigationBar.MODE_FIXED)
//                .setBarBackgroundColor(R.color.white);
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.ic_home_home, "首页"))
                .addItem(new BottomNavigationItem(R.mipmap.ic_home_message, "Talk"))
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
        isHomePage=true;




    }

    @Override
    public void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                HealthUtil.update_local_myinfo();
            }
        }).start();


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if ((Intent.FLAG_ACTIVITY_CLEAR_TOP & intent.getFlags()) != 0) {
            Intent orderIntent = new Intent();
            if (intent.getIntExtra(com.maxiaobu.healthclub.common.Constant.PAY_RESULT, -1) == 1) {

                orderIntent.putExtra("orderFlag", 2);
                orderIntent.setClass(HomeActivity.this, OrderListActivity.class);
                startActivity(orderIntent);
            } else if (intent.getIntExtra(com.maxiaobu.healthclub.common.Constant.PAY_RESULT, -1) == 0) {
                orderIntent.putExtra("orderFlag", 1);
                orderIntent.setClass(HomeActivity.this, OrderListActivity.class);
                startActivity(orderIntent);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onBackPressed() {
        if (isHomePage) {
            exit();
        } else {
            mBottomNavigationBar.selectTab(0);
        }
    }

    /**
     * 退出应用
     */
    private void exit() {
        if (isExit) {
            isExit = false;
            Toast.makeText(getApplicationContext(), "再按一次退出",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
        }
    }
    public void switchFragment(int position) {
        runnable = null;
        switch (position) {
            case 0:
                switchContent(nowFragment, mHomeFragment);
                isHomePage=true;
                break;
            case 1:
                switchContent(nowFragment, mTalkFragment);
                isHomePage=false;
                break;
            case 2:
                switchContent(nowFragment, mDiscoveryFragment);
                isHomePage=false;

                break;
            case 3:
                switchContent(nowFragment, mMineFragment);
                isHomePage=false;

                break;
            default:
                break;
        }

    }

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

    /**
     * 全部运行权限
     */
    @TargetApi(23)
    private void requestPermissions() {
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {
//				Toast.makeText(MainActivity.this, "All permissions have been granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(String permission) {
                //Toast.makeText(MainActivity.this, "Permission " + permission + " has been denied", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 当用户在其他设备登录显示dialog
     */
    private void showConflictDialog() {
        isConflictDialogShow = true;
        DemoHelper.getInstance().logout(false, null);
        String st = getResources().getString(R.string.Logoff_notification);
        if (!HomeActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (conflictBuilder == null)
                    conflictBuilder = new AlertDialog.Builder(HomeActivity.this);
                conflictBuilder.setTitle(st);
                conflictBuilder.setMessage("账户在其他设备登录");
                conflictBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        conflictBuilder = null;
                        finish();
                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                conflictBuilder.setCancelable(false);
                conflictBuilder.create().show();
                isConflict = true;
            } catch (Exception e) {
                EMLog.e("MainActivity", "---------用户在其他设备登录显示dialog创建失败" + e.getMessage());
            }
        }
    }

    /**
     * 当用户账户被移除 显示dialog
     */
    private void showAccountRemovedDialog() {
        isAccountRemovedDialogShow = true;
        DemoHelper.getInstance().logout(false, null);
        String st5 = getResources().getString(R.string.Remove_the_notification);
        if (!HomeActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (accountRemovedBuilder == null)
                    accountRemovedBuilder = new AlertDialog.Builder(HomeActivity.this);
                accountRemovedBuilder.setTitle(st5);
                accountRemovedBuilder.setMessage("账户被移除");
                accountRemovedBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        accountRemovedBuilder = null;
                        finish();
                        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    }
                });
                accountRemovedBuilder.setCancelable(false);
                accountRemovedBuilder.create().show();
                isCurrentAccountRemoved = true;
            } catch (Exception e) {
                EMLog.e("MainActivity", "---------当用户账户被移除 显示dialog创建失败" + e.getMessage());
            }
        }
    }

    /**
     * update the total unread count
     * 更新总未读数   联系人列表未读
     */
    public void updateUnreadAddressLable() {
        runOnUiThread(new Runnable() {
            public void run() {
                int count = getUnreadAddressCountTotal();
//                Log.d("联系人列表未读", String.valueOf(count));
                // TODO: 2016/9/6 设置未读消息数量  联系人列表未读
                if (count > 0) {
//                    unreadAddressLable.setVisibility(View.VISIBLE);
                    mViewPoint.setVisibility(View.VISIBLE);
                } else {
//                    unreadAddressLable.setVisibility(View.INVISIBLE);
                    mViewPoint.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    /**
     * get unread event notification count, including application, accepted, etc
     * 获取包括通知者的未读数    联系人列表未读
     *
     * @return
     */
    public int getUnreadAddressCountTotal() {
        int unreadAddressCountTotal = 0;
        unreadAddressCountTotal = inviteMessgeDao.getUnreadMessagesCount();
        return unreadAddressCountTotal;
    }

}
