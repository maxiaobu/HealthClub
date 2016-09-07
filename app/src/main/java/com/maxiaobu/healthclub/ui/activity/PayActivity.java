package com.maxiaobu.healthclub.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.MainActivity;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanAccountInfo;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.volleykit.IRequest;
import com.maxiaobu.volleykit.JsonUtils;
import com.maxiaobu.volleykit.NodataFragment;
import com.maxiaobu.volleykit.RequestListener;
import com.maxiaobu.volleykit.RequestParams;

import org.json.JSONException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayActivity extends BaseAty implements View.OnClickListener{

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
        mTotlePrice = getIntent().getStringExtra("totlePrice");
        mOrdno = getIntent().getStringExtra("ordno");
        setCommonBackToolBar(mToolbarCommon, mTvTitleCommon, "支付页");
        mTvPrice.setText(mTotlePrice + "元");
    }

    @Override
    public void initData() {
        RequestParams params = new RequestParams("memid", SPUtils.getString(this, Constant.MEMID));
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
            if (mTotalEbi > Integer.parseInt(mTotlePrice)) {//仅 e币
                App.getRequestInstance().post(this, UrlPath.URL_EBI_PAY, new RequestParams("ordno", "{\"ordno\":" + mOrdno + "}"), new RequestListener() {
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
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClass(PayActivity.this, HomeActivity.class);
        intent.putExtra("foodFlag", 1);
        startActivity(intent);
    }
}
