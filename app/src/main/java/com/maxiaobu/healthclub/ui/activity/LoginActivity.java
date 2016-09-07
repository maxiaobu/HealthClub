package com.maxiaobu.healthclub.ui.activity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.transition.Explode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanMlogin;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.volleykit.JsonUtils;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

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

    }

    public void initData() {

    }

    @OnClick({R.id.bt_go,R.id.fab,R.id.tv_forget_password})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setExitTransition(null);
                    getWindow().setEnterTransition(null);
                    ActivityOptions options =
                            ActivityOptions.makeSceneTransitionAnimation(this, mFab, mFab.getTransitionName());
                    startActivity(new Intent(this, RegisterActivity.class), options.toBundle());
                } else {
                    startActivity(new Intent(this, RegisterActivity.class));
                }
                break;
            case R.id.bt_go:
                login(mEtUsername.getText().toString().trim(),mEtPassword.getText().toString().trim());
                break;
            case R.id.tv_forget_password:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setExitTransition(null);
                    getWindow().setEnterTransition(null);
                    ActivityOptions options =
                            ActivityOptions.makeSceneTransitionAnimation(this, mFab, mFab.getTransitionName());
                    startActivity(new Intent(this, FindPasswordActivity.class), options.toBundle());
                } else {
                    startActivity(new Intent(this, FindPasswordActivity.class));
                }
                break;
            default:
                break;
        }
    }

    public void login(String userName,String password){
        if (TextUtils.isEmpty(userName)||TextUtils.isEmpty(password)){
            Toast.makeText(this, "手机号和密码不能为空！", Toast.LENGTH_SHORT).show();
        }else {
            com.loopj.android.http.RequestParams params=new com.loopj.android.http.RequestParams();
            params.put("mobphone",userName);
            params.put("mempass",password);
            params.put("phonedeviceno","");
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(this,UrlPath.URL_LOGIN,params,new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    String responseString = response.toString();
                    Log.d("LoginActivity", responseString);
                    BeanMlogin data = JsonUtils.object(responseString, BeanMlogin.class);
                    data.getMsgFlag();
                    if ("1".equals(data.getMsgFlag())){
                        String nickname = data.getNickname();
                        String memid = data.getMemid();
                        SPUtils.putString(LoginActivity.this, Constant.MEMID,memid);
                        SPUtils.putString(LoginActivity.this, Constant.NICK_NAME,nickname);

                        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                            /*Explode explode = new Explode();
                            explode.setDuration(500);
                            getWindow().setExitTransition(explode);
                            getWindow().setEnterTransition(explode);
                            ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(LoginActivity.this);
                            Intent i2 = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(i2, oc2.toBundle());
                            LoginActivity.this.finish();*/
                            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                            LoginActivity.this.finish();
                        }else {
                            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                            LoginActivity.this.finish();
                        }
                    }else {
                        Toast.makeText(LoginActivity.this, data.getMsgContent(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.d("LoginActivity", responseString);
                }
            });
        }
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
}
