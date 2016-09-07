package com.maxiaobu.healthclub.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.maxiaobu.healthclub.BaseAty;
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

public class RegisterTwoActivity extends BaseAty implements View.OnClickListener {


    @Bind(R.id.et_password)
    EditText mEtPassword;
    @Bind(R.id.et_config_password)
    EditText mEtConfigPassword;
    @Bind(R.id.et_nike_name)
    EditText mEtNikeName;
    @Bind(R.id.et_gender)
    EditText mEtGender;
    @Bind(R.id.bt_go)
    Button mBtGo;
    @Bind(R.id.cv_add)
    CardView mCvAdd;
    @Bind(R.id.fab)
    FloatingActionButton mFab;
    @Bind(R.id.til_gender)
    TextInputLayout mTilGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_two);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ShowEnterAnimation();
        }
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateRevealClose();
            }
        });
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.bt_go, R.id.til_gender})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_go:
//                RegisterActivity.this.finish();
//                startActivity(new Intent(RegisterActivity.this,RegisterTwoActivity.class));
                regist();
                break;
            case R.id.til_gender:
                final String[] genders = new String[]{"男", "女"};
                new MaterialDialog.Builder(this)
                        .items(genders)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                mEtGender.setText(genders[which]);
                                dialog.dismiss();
                            }
                        })
                        .show();

                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        animateRevealClose();
    }

    private void ShowEnterAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.fabtransition);
            getWindow().setSharedElementEnterTransition(transition);

            transition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {
                    mCvAdd.setVisibility(View.GONE);
                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        transition.removeListener(this);
                    }

                    animateRevealShow();
                }

                @Override
                public void onTransitionCancel(Transition transition) {

                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            });
        }

    }

    public void animateRevealShow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Animator mAnimator = ViewAnimationUtils.createCircularReveal(mCvAdd, mCvAdd.getWidth() / 2, 0, mFab.getWidth() / 2, mCvAdd.getHeight());
            mAnimator.setDuration(500);
            mAnimator.setInterpolator(new AccelerateInterpolator());
            mAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    mCvAdd.setVisibility(View.VISIBLE);
                    super.onAnimationStart(animation);
                }
            });
            mAnimator.start();
        }
    }

    public void animateRevealClose() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Animator mAnimator = ViewAnimationUtils.createCircularReveal(mCvAdd, mCvAdd.getWidth() / 2, 0, mCvAdd.getHeight(), mFab.getWidth() / 2);
            mAnimator.setDuration(500);
            mAnimator.setInterpolator(new AccelerateInterpolator());
            mAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mCvAdd.setVisibility(View.INVISIBLE);
                    super.onAnimationEnd(animation);
                    mFab.setImageResource(R.mipmap.ic_plus);
                    RegisterTwoActivity.super.onBackPressed();
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                }
            });
            mAnimator.start();
        }
    }

    private void regist() {
        if (TextUtils.isEmpty(mEtPassword.getText().toString().trim())) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(mEtConfigPassword.getText().toString().trim())) {
            Toast.makeText(this, "请输入确认密码", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(mEtNikeName.getText().toString().trim())) {
            Toast.makeText(this, "请输入昵称", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(mEtGender.getText().toString().trim())) {
            Toast.makeText(this, "请输入性别", Toast.LENGTH_SHORT).show();
        } else if (!mEtPassword.getText().toString().trim().equals(mEtConfigPassword.getText().toString().trim())) {
            Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
        } else {
            String gender;
            if (mEtGender.getText().toString().trim().equals("女")) {
                gender = "0";
            } else {
                gender = "1";
            }
            final String userPhone = getIntent().getStringExtra("mobphone");
            com.loopj.android.http.RequestParams params = new com.loopj.android.http.RequestParams();
            params.put("mempass", mEtPassword.getText().toString().trim());
            params.put("nickname", mEtNikeName.getText().toString().trim());
            params.put("gender", gender);
            params.put("mobphone", userPhone);
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(this, UrlPath.URL_REGISTER, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    String responseString = response.toString();
//                Log.d("LoginActivity", responseString);
                    // TODO: 2016/9/5 换bean
                    BeanMlogin data = JsonUtils.object(responseString, BeanMlogin.class);
                    data.getMsgFlag();
                    Toast.makeText(RegisterTwoActivity.this, data.getMsgContent(), Toast.LENGTH_SHORT).show();
                    if ("1".equals(data.getMsgFlag())) {

                        Intent intent = new Intent();
//                        intent.putExtra("mobphone", userPhone);
//                        intent.putExtra("mempass", mEtPassword.getText().toString().trim());
//                        intent.putExtra("where", 0);
                        intent.setClass(RegisterTwoActivity.this, LoginActivity.class);
                        startActivity(intent);
                       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            ActivityOptions options =
                                    ActivityOptions.makeSceneTransitionAnimation(RegisterTwoActivity.this, mFab, mFab.getTransitionName());
                            startActivity(intent, options.toBundle());
                        } else {

                        }*/
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
