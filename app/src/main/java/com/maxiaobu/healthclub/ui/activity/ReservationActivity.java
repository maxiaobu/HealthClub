package com.maxiaobu.healthclub.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.healthclub.utils.web.BaseJsToAndroid;
import com.maxiaobu.volleykit.RequestParams;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ReservationActivity extends BaseAty {

    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;
    @Bind(R.id.web_view)
    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        setCommonBackToolBar(mToolbarCommon, mTvTitleCommon, "预约");
        // 设置WebView支持JavaScript
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        // 在js中调用本地java方法
        mWebView.addJavascriptInterface(new WebAppInterface(this), "mobile");
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");

        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.loadUrl("file:///android_asset/reservation.html"+"?nickname=");
    }

    @Override
    public void initData() {
        RequestParams params = new RequestParams();

//        App.getRequestInstance().post(this, UrlPath.URL_EBI_PAY,params,);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClass(ReservationActivity.this, HomeActivity.class);
        intent.putExtra("foodFlag", 1);
        startActivity(intent);
    }

    public class WebAppInterface extends BaseJsToAndroid {
        Context mContext;

        WebAppInterface(Context c) {
            super(c);
            mContext = c;
        }

        @JavascriptInterface
        public void popNewWindow(String page) {
//        Intent intent = new Intent();
//        intent.putExtra("url",page);
//        intent.setClass(PersionalCourseActivity.this,CourseBuyActivity.class);
//            startActivity(intent);
        }


        @JavascriptInterface
        public String getmemid() {
            return SPUtils.getString( Constant.MEMID);
        }
    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            // 页面加载完成 通过js回调函数实现获取页面信息
            view.loadUrl("javascript:window.mobile.loadPageData("
                    + "document.title,$('title').attr('isback'),$('title').attr('btn'),$('title').attr('navbar'))");
        }
    }
}
