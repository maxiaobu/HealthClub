package com.maxiaobu.healthclub.ui.activity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.chat.DemoHelper;
import com.maxiaobu.healthclub.chat.db.DemoDBManager;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanMlogin;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.volleykit.JsonUtils;
import com.maxiaobu.volleykit.NodataFragment;
import com.maxiaobu.volleykit.RequestListener;
import com.maxiaobu.volleykit.RequestParams;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * 登录
     */
    public static final int REGISTER = 1;
    /**
     * 找回密码
     */
    public static final int FIND_PASSWORD = 2;

    private boolean progressShow;//加载中

    @Bind(R.id.et_username)
    EditText mEtUsername;
    @Bind(R.id.et_password)
    EditText mEtPassword;
    @Bind(R.id.bt_go)
    Button mBtGo;
    @Bind(R.id.tv_forget_password)
    TextView mTvForgetPassword;
    @Bind(R.id.cv)
    CardView mCv;
    @Bind(R.id.fab)
    FloatingActionButton mFab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initView();
        initData();
    }

    public void initView() {
        //用户名变删除已输入密码
        mEtUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mEtPassword.setText(null);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //如果有用户名放上
        String userId = SPUtils.getString(this, Constant.USER_ID);
        if (userId != null) {
            mEtUsername.setText(userId);
        }
    }

    public void initData() {

    }

    @OnClick({R.id.bt_go, R.id.fab, R.id.tv_forget_password})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setExitTransition(null);
                    getWindow().setEnterTransition(null);
                    ActivityOptions options =
                            ActivityOptions.makeSceneTransitionAnimation(this, mFab, mFab.getTransitionName());
                    startActivityForResult(new Intent(this, RegisterActivity.class),REGISTER, options.toBundle());
                } else {
                    startActivityForResult(new Intent(this, RegisterActivity.class),REGISTER);
                }
                break;
            case R.id.bt_go:
                login(mEtUsername.getText().toString().trim(), mEtPassword.getText().toString().trim());
                break;
            case R.id.tv_forget_password:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setExitTransition(null);
                    getWindow().setEnterTransition(null);
                    ActivityOptions options =
                            ActivityOptions.makeSceneTransitionAnimation(this, mFab, mFab.getTransitionName());
                    startActivityForResult(new Intent(this, FindPasswordActivity.class),FIND_PASSWORD, options.toBundle());
                } else {
                    startActivityForResult(new Intent(this, FindPasswordActivity.class),FIND_PASSWORD);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode==REGISTER||requestCode==FIND_PASSWORD)&&resultCode==1){
//            注册去主界面
            // TODO: 2016/9/7 登录
            String userName = data.getStringExtra("userName");
            String passWord=data.getStringExtra("passWord");
            login(userName,passWord);
        }
    }

    public void login(final String userName, final String password) {
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "手机号和密码不能为空！", Toast.LENGTH_SHORT).show();
        } else {
            progressShow = true;
            final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
            pd.setCanceledOnTouchOutside(false);
            pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    progressShow = false;
                }
            });
            pd.setMessage(getString(R.string.Is_landing));
            pd.show();
            DemoDBManager.getInstance().closeDB();

            RequestParams params = new RequestParams();
            params.put("mobphone", userName);
            params.put("mempass", password);
            params.put("phonedeviceno", "");
            App.getRequestInstance().post(this, UrlPath.URL_LOGIN, params, new RequestListener() {
                @Override
                public void requestSuccess(String s) {
                    String responseString = s.toString();
                    Log.d("LoginActivity", responseString);
                    BeanMlogin data = JsonUtils.object(responseString, BeanMlogin.class);
                    data.getMsgFlag();
                    if ("1".equals(data.getMsgFlag())) {
                        String nickname = data.getMember().getNickname();
                        String memid = data.getMember().getMemid();
                        String avatar=data.getMember().getImgsfilename();
                        SPUtils.putString(LoginActivity.this, Constant.MEMID, memid);
                        SPUtils.putString(LoginActivity.this, Constant.NICK_NAME, nickname);
                        SPUtils.putString(LoginActivity.this, Constant.AVATAR, avatar);
                        SPUtils.putString(LoginActivity.this,Constant.USER_ID,userName);
                        SPUtils.putString(LoginActivity.this,Constant.REC_ADDRESS,data.getMember().getRecaddress());
                        SPUtils.putString(LoginActivity.this,Constant.REC_NAME,data.getMember().getRecname());
                        SPUtils.putString(LoginActivity.this,Constant.REC_PHONE,data.getMember().getRecphone());


                        // TODO: 2016/9/7 登录环信
                        loginHx(memid,password,nickname,avatar,pd);
                    } else {
                        Toast.makeText(LoginActivity.this, data.getMsgContent().get(0), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void requestAgain(NodataFragment nodataFragment) {
                    login(mEtUsername.getText().toString().trim(), mEtPassword.getText().toString().trim());
                }
            });
        }
    }

    private void loginHx(final String userID, String passWord, final String nickName, final String avatar, final ProgressDialog pd) {
        EMClient.getInstance().login(userID, passWord, new EMCallBack() {
            @Override
            public void onSuccess() {
                // 将自己服务器返回的环信账号、昵称和头像URL设置到帮助类中。
                boolean updatenick = DemoHelper.getInstance().getUserProfileManager().updateCurrentUserNickName(nickName);// 更新当前用户的昵称
                DemoHelper.getInstance().getUserProfileManager().setCurrentUserAvatar(avatar);
                DemoHelper.getInstance().setCurrentUserName(userID); // 环信Id
                if (!updatenick) {
                    Log.e("LoginActivity", "更新用户昵称失败");
                }
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                //如果aty还在,并且加载条正在显示
                if (!LoginActivity.this.isFinishing() && pd.isShowing()) {
                    pd.dismiss();
                }
                // 获取用户信息 (this should be get from App's server or 3rd party service)
                DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();
                goHome();
            }

            @Override
            public void onError(int i, final String s) {

                if (!progressShow) {
                    return;
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + s,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onProgress(int i, String s) {
                Log.d("onProgress", "login: 环信正在登录");
            }
        });
    }

    // 获取点击事件
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View view = getCurrentFocus();
            if (isHideInput(view, ev)) {
                hideSoftInput(view);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
    private boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (ev.getX() > left && ev.getX() < right && ev.getY() > top
                    && ev.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    // 隐藏软键盘
    private void hideSoftInput(View view) {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager != null) {
            manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void goHome() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                            EMClient.getInstance().login();
                            /*Explode explode = new Explode();
                            explode.setDuration(500);
                            getWindow().setExitTransition(explode);
                            getWindow().setEnterTransition(explode);
                            ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(LoginActivity.this);
                            Intent i2 = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(i2, oc2.toBundle());
                            LoginActivity.this.finish();*/
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            LoginActivity.this.finish();
        } else {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            LoginActivity.this.finish();
        }
    }


}
