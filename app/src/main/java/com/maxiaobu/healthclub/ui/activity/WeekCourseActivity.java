package com.maxiaobu.healthclub.ui.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.utils.web.BaseJsToAndroid;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WeekCourseActivity extends BaseAty {

    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;
    @Bind(R.id.web_view)
    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_course);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        setCommonBackToolBar(mToolbarCommon, mTvTitleCommon, "团操课程");
        // 设置WebView支持JavaScript
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        // 在js中调用本地java方法
        mWebView.addJavascriptInterface(new WebAppInterface(this), "mobile");
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");

        mWebView.setWebViewClient(new MyWebViewClient());
//        clubid=C000174&coachid=M000447
        Log.d("ApplyBindClubActivity", "file:///android_asset/weekcourse.html?clubid=" + getIntent().getStringExtra("clubid") );
        mWebView.loadUrl("file:///android_asset/weekcourse.html?clubid=" +getIntent().getStringExtra("clubid"));
    }

    @Override
    public void initData() {

    }

    public class WebAppInterface extends BaseJsToAndroid {
        Context mContext;

        WebAppInterface(Context c) {
            super(c);
            mContext = c;
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
