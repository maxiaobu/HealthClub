package com.maxiaobu.healthclub.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.MainActivity;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanMbpcourse;
import com.maxiaobu.healthclub.utils.TimesUtil;
import com.maxiaobu.healthclub.utils.web.BaseJsToAndroid;
import com.maxiaobu.healthclub.volleykit.NodataFragment;
import com.maxiaobu.healthclub.volleykit.RequestJsonListener;
import com.maxiaobu.healthclub.volleykit.RequestParams;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class GcourseActivity extends BaseAty implements EasyPermissions.PermissionCallbacks {

    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;
    @Bind(R.id.web_view)
    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gcourse);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        setCommonBackToolBar(mToolbarCommon, mTvTitleCommon, "团操详情");
        // 设置WebView支持JavaScript
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        // 在js中调用本地java方法
        mWebView.addJavascriptInterface(new WebAppInterface(this), "mobile");
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");

        mWebView.setWebViewClient(new MyWebViewClient());
//        clubid=C000174
        Log.d("ApplyBindClubActivity", "file:///android_asset/" + getIntent().getStringExtra("page"));
        //gcourse.html?gcourseid=GC000043
        mWebView.loadUrl("file:///android_asset/" + getIntent().getStringExtra("page"));

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

        @JavascriptInterface
        public void callUp(final String phoneNumber) {
            ringUp(phoneNumber);
        }

        //教练团超2确认订单
        @JavascriptInterface
        public void popNewWindow(String page) {
//            Log.d("WebAppInterface", page);
            // courseBuy.html?mertype=gcourse&courseid=G000034&memid=M000510
            Intent intent = new Intent(GcourseActivity.this, CourseBuyActivity.class);
            intent.putExtra("url", page);
            startActivity(intent);
        }

        //俱乐部课表2预约
        @JavascriptInterface
        public void gotoBespeakList() {
//            Toast.makeText(context, "gotoBespeakList", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(GcourseActivity.this, HomeActivity.class);
            intent.putExtra(Constant.JUMP_KEY, Constant.GCOURSE_TO_BESPEAKLIST);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

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

    /**
     * 打电话确认
     */
    @AfterPermissionGranted(122)
    private void ringUp(final String phoneNum) {
        new MaterialDialog.Builder(this)
                .title("呼叫")
                .content(phoneNum)
                .positiveColor(getResources().getColor(R.color.colorTextPrimary))
                .positiveText("确认")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        if (EasyPermissions.hasPermissions(GcourseActivity.this, Manifest.permission.CALL_PHONE)) {
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            Uri data = Uri.parse("tel:" + phoneNum);
                            intent.setData(data);
                            startActivity(intent);
                        } else {
                            EasyPermissions.requestPermissions(GcourseActivity.this, "需要打电话的权限",
                                    122, Manifest.permission.CALL_PHONE);//让easyPermission去请求权限
                        }
                    }
                })
                .negativeColor(getResources().getColor(R.color.colorTextPrimary))
                .negativeText("取消")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    /**
     * 没有需要设置的地方，复制就行，但必须有
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions   将结果转发给EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * 用户给权限了
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
//        Log.i(TAG, "onPermissionsDenied: "+ requestCode + ":" + perms.size());
    }

    /**
     * 权限被拒绝胃
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        EasyPermissions.checkDeniedPermissionsNeverAskAgain(this, "你妈的，瞎恩个jb，点设置---》权限---》权限给我", R.string.setting, R.string.cancel, perms);
    }

}
