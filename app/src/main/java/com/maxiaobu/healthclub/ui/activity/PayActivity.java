package com.maxiaobu.healthclub.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanAccountInfo;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.healthclub.volleykit.JsonUtils;
import com.maxiaobu.healthclub.volleykit.NodataFragment;
import com.maxiaobu.healthclub.volleykit.RequestListener;
import com.maxiaobu.healthclub.volleykit.RequestParams;

import org.json.JSONException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayActivity extends BaseAty implements View.OnClickListener {

    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;
    @Bind(R.id.tv_price)
    TextView mTvPrice;
    @Bind(R.id.tv_ebi_content)
    TextView mTvEbiContent;
    @Bind(R.id.cb_e_pay)
    AppCompatCheckBox mCbEPay;
    @Bind(R.id.ly_epay)
    LinearLayout mLyEpay;
    @Bind(R.id.cb_wxin_pay)
    AppCompatCheckBox mCbWxinPay;
    @Bind(R.id.rl_wxin_pay)
    LinearLayout mRlWxinPay;
    @Bind(R.id.cb_ali_pay)
    AppCompatCheckBox mCbAliPay;
    @Bind(R.id.rl_ali_pay)
    LinearLayout mRlAliPay;
    @Bind(R.id.tv_now_order)
    TextView mTvNowOrder;


    private String mTotlePrice;
    private int mTotalEbi;
    private String mOrdno;
    private String mPayType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        mPayType = getIntent().getStringExtra(Constant.PAY_TYPE);
        mTotlePrice = getIntent().getStringExtra("totlePrice");
        mOrdno = getIntent().getStringExtra("ordno");
        setCommonBackToolBar(mToolbarCommon, mTvTitleCommon, "支付页");
        mTvPrice.setText(mTotlePrice + "元");
    }

    @Override
    public void initData() {
        RequestParams params = new RequestParams("memid", SPUtils.getString( Constant.MEMID));
        App.getRequestInstance().post(this, UrlPath.URL_ACCOUNT_INFO, params, new RequestListener() {
            @Override
            public void requestSuccess(String s) {
                BeanAccountInfo object = JsonUtils.object(s, BeanAccountInfo.class);
                mTotalEbi = (object.getYcoincashnum() + object.getYcoinnum()) / 100;
                if (mTotalEbi > Integer.parseInt(mTotlePrice)) {
                    mTvEbiContent.setText("本次可抵现" + mTotlePrice + "元，抵现后余额为0元");
                } else {
                    mTvEbiContent.setText("本次可抵现" + mTotlePrice + "元，抵现后余额为" + (Integer.parseInt(mTotlePrice) - mTotalEbi) + "元");
                }
            }

            @Override
            public void requestAgain(NodataFragment nodataFragment) {
                initData();
            }
        });
    }

    @OnClick({R.id.ly_epay, R.id.rl_wxin_pay, R.id.rl_ali_pay, R.id.tv_now_order})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ly_epay:
                mCbEPay.setChecked(!mCbEPay.isChecked());

                break;
            case R.id.rl_wxin_pay:
                Toast.makeText(this, "微信支付未开通", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_ali_pay:
                Toast.makeText(this, "支付宝未开通", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_now_order:
                pay();
                break;
            default:
                break;
        }
    }

    private void pay() {
        if (mCbEPay.isChecked()) {
//            Log.d("PayActivity", mOrdno);
            if (mTotalEbi > Integer.parseInt(mTotlePrice)) {
                //仅 e币
                RequestParams params;
                String url;
                if (mPayType != null && mPayType.equals("course")) {
                    params = new RequestParams("ordno", mOrdno);
                    params.put("memid", SPUtils.getString( Constant.MEMID));
                    url = UrlPath.URL_COURSE_EBI_PAY;
                }else {
                    params = new RequestParams("ordno", "{\"ordno\":" + mOrdno + "}");
                    url = UrlPath.URL_EBI_PAY;
                }
                //CO-20160905-994
                App.getRequestInstance().post(this, url, params, new RequestListener() {
                    @Override
                    public void requestSuccess(String s) {
                        try {
                            org.json.JSONObject a = new org.json.JSONObject(s);
                            if (a.get("msgFlag").equals("1")) {
                                startAty();
                            } else {
                                Toast.makeText(PayActivity.this, a.get("msgContent").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(mActivity, "接口是不是改了", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void requestAgain(NodataFragment nodataFragment) {
                        initData();
                    }


                });
            }
        } else {
            Toast.makeText(this, "请选择支付方式", Toast.LENGTH_SHORT).show();
        }
    }


    private void payRequest() {


    }

    private void startAty() {
        if (!TextUtils.isEmpty(mPayType) && mPayType.equals("course")) {
            new MaterialDialog.Builder(PayActivity.this)
                    .content("支付成功，是否下载预约")
                    .positiveText("现在预约")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Intent intent = new Intent();
                            intent.setClass(PayActivity.this, ReservationActivity.class);
                            intent.putExtra("coachid",getIntent().getStringExtra("coachid"));
                            intent.putExtra("orderid", getIntent().getStringExtra("ordno"));
                            intent.putExtra("reservation", getIntent().getStringExtra("reservation"));

                            intent.putExtra(Constant.PAY_RESULT, 0);
                            startActivity(intent);
                            PayActivity.this.finish();
                        }
                    })
                    .negativeText("稍后预约")
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Intent intent = new Intent();
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setClass(PayActivity.this, HomeActivity.class);
                            intent.putExtra(Constant.PAY_RESULT, 0);
                            startActivity(intent);
                        }
                    }).show();


        }else {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setClass(PayActivity.this, HomeActivity.class);
            intent.putExtra("PAY_RESULT", 1);
            startActivity(intent);
        }

    }
}
