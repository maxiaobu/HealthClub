package com.maxiaobu.healthclub.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.healthclub.utils.web.BaseJsToAndroid;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CourseBuyActivity extends BaseAty {

    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;
    @Bind(R.id.web_view)
    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_buy);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        String url = getIntent().getStringExtra("url");
        setCommonBackToolBar(mToolbarCommon, mTvTitleCommon, "确认订单");
        // 设置WebView支持JavaScript
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        // 在js中调用本地java方法
        mWebView.addJavascriptInterface(new WebAppInterface(this), "mobile");
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
        mWebView.loadUrl("file:///android_asset/" + url);
//        Log.d("Persional", "file:///android_asset/" + url);
//        mWebView.setWebViewClient(new MyWebViewClient());
    }

    @Override
    public void initData() {

    }

    public class WebAppInterface{
        Context mContext;
        WebAppInterface(Context c){
            mContext=c;
        }
        @JavascriptInterface
        public void gotoPay(String ordno,String ordamt){
            Intent intent = new Intent();
            intent.putExtra("ordno",ordno);
            Log.d("WebAppInterface", ordno);
            intent.putExtra("totlePrice",ordamt);
            intent.putExtra(Constant.PAY_TYPE,"course");
            intent.putExtra("coachid",getIntent().getStringExtra("coachid"));
            Log.d("WebAppInterface", getIntent().getStringExtra("coachid"));
            intent.setClass(CourseBuyActivity.this,PayActivity.class);
            startActivity(intent);
        }
        // 修改收货信息
        @JavascriptInterface
        public void personalInfo() {
            Intent intent = new Intent();
            intent.setClass(CourseBuyActivity.this, RevampAddress.class);
            startActivityForResult(intent, Constant.RESULT_REQUEST_ONE);
        }

        @JavascriptInterface
        public String getmemid() {
           return SPUtils.getString(Constant.MEMID);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Constant.RESULT_REQUEST_ONE&&resultCode==Constant.RESULT_OK){
            mWebView.reload();
        }
    }
}
