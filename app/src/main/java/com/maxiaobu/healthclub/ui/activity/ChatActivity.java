package com.maxiaobu.healthclub.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.FrameLayout;

import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.chat.runtimepermissions.PermissionsManager;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.ui.fragment.ChatFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import maxiaobu.easeui.EaseConstant;
import maxiaobu.easeui.ui.EaseChatFragment;

public class ChatActivity extends BaseAty {
    String toChatUsername;
    private EaseChatFragment chatFragment;

    @Bind(R.id.container)
    FrameLayout mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        Intent intent = getIntent();
        toChatUsername = getIntent().getExtras().getString(Constant.USER_ID);
        chatFragment = new ChatFragment();
        //pass parameters to chat fragment
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        // make sure only one chat activity is opened
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }
}
