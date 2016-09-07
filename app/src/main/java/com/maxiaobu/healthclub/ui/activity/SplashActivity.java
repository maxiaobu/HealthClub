package com.maxiaobu.healthclub.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.VolleyError;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.MainActivity;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.service.UpdataService;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.volleykit.NodataFragment;
import com.maxiaobu.volleykit.RequestListener;

/**
 * 开屏页
 * 设置动画 --guide-登录--主页面--更新--初始化环信
 */
public class SplashActivity extends AppCompatActivity {


    private ImageView iv_start;

    private static final int sleepTime = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

    }

    @Override
    protected void onStart() {
        super.onStart();
        iv_start = (ImageView) findViewById(R.id.iv_start);
        initImage();
    }

    private void initImage() {
        final ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        scaleAnim.setFillAfter(true);
        scaleAnim.setDuration(2000);
        scaleAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                checkGuide();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        iv_start.startAnimation(scaleAnim);

    }

    private void checkGuide() {
        boolean enter_guide = SPUtils.getBoolean(this, "enter_guide", true);
        if (enter_guide) {
            this.finish();
            startActivity(new Intent(this, GuideActivity.class));
            overridePendingTransition(android.R.anim.fade_in,
                    android.R.anim.fade_out);
            finish();
        } else {
//            Log.d("SplashActivity", "enter_guide1:" + enter_guide1);

            checkVersion();
            startActivity();
        }

    }

    private void checkVersion() {
        App.getRequestInstance().get(this, "http://192.168.1.121:8080/efithealth/mbcoach.do?pageIndex=1&tarid=M000440", new RequestListener() {
            @Override
            public void requestSuccess(String s) {
                String lastVersion = "1.0.2";
                String apkUrl = "http:/\\/zhstatic.zhihu.com\\/pkg\\/store\\/daily\\/zhihu-daily-zhihu-2.6.0(744)-release.apk";
                String msg = "【更新】\\r\\n\\r\\n- 极大提升性能及稳定性\\r\\n- 部分用户无法使用新浪微博登录\\r\\n- 部分用户无图模式无法分享至微信及朋友圈";
                String version = "100";
                String versionName = getAppVersionName(SplashActivity.this);
                if (versionName.equals(version)) {
                    startActivity();
                } else {
                    //版本需要更新
                    showUpdateDialog(msg, apkUrl);
                }
            }

            @Override
            public void requestAgain(NodataFragment nodataFragment) {
                checkVersion();
            }
        });
    }


    public void showUpdateDialog(String msg, final String apkUrl) {
        new MaterialDialog.Builder(this)
                .title("有新版本")
                .content(msg)
                .positiveColor(getResources().getColor(R.color.colorTextPrimary))
                .positiveText("确认")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        // TODO: 2016/8/30 跟新
                        Intent intent = new Intent(SplashActivity.this, UpdataService.class);
                        intent.putExtra("url", apkUrl);
                        startService(intent);
                        dialog.dismiss();
                        startActivity();

                    }
                })
                .negativeColor(getResources().getColor(R.color.colorTextPrimary))
                .negativeText("取消")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        startActivity();
                    }
                })
                .show();
    }

    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
        } catch (Exception e) {
            Toast.makeText(context, "获取versionName失败", Toast.LENGTH_SHORT).show();
        }
        return versionName;
    }

    private void startActivity() {
        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
        finish();
    }
}
