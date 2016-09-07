package com.maxiaobu.healthclub.ui.fragment;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.maxiaobu.healthclub.BaseFrg;
import com.maxiaobu.healthclub.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseOrderFragment extends BaseFrg {


    @Bind(R.id.web_view)
    WebView mWebView;

    public CourseOrderFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_course_order, container, false);
        ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
    }

    @Override
    public void initView() {
// 设置WebView支持JavaScript
        mWebView.getSettings().setJavaScriptEnabled(true);
        // 在js中调用本地java方法
//        mWebView.addJavascriptInterface(new BaseJsToAndroid(getActivity()), "mobile");
        // 在js中调用本地java方法
        // web_load.addJavascriptInterface(new
        // jsToAndroid_Find(),
        // "mobile");
        // 添加客户端支持
        // 设置进度条
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
               /* if (newProgress == 100) {
                    // 隐藏进度条
//                    swipeLayout.setRefreshing(false);
                } else {
                    if (!swipeLayout.isRefreshing())
                        swipeLayout.setRefreshing(true);
                }*/

                super.onProgressChanged(view, newProgress);
            }
        });
        // 设置WebView支持JavaScript
        mWebView.getSettings().setJavaScriptEnabled(true);
        // 在js中调用本地java方法
        // web_load.addJavascriptInterface(new
        // jsToAndroid_Find(),
        // "mobile");
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
        mWebView.loadUrl("file:///android_asset/corderList.html");
        // 设置WebView中的客户端的行为
        mWebView.setWebViewClient(new WebViewClient() {
            // 让WebView对点击网页中的URL做出响应
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
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
