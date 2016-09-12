package com.maxiaobu.healthclub.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.utils.HealthUtil;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.volleykit.NodataFragment;
import com.maxiaobu.volleykit.RequestListener;
import com.maxiaobu.volleykit.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RevampAddress extends BaseAty {

    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.btn_save)
    TextView mBtnSave;
    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;
    @Bind(R.id.ed_consignee)
    AppCompatEditText mEdConsignee;
    @Bind(R.id.ed_phone_num)
    AppCompatEditText mEdPhoneNum;
    @Bind(R.id.tv_area)
    TextView mTvArea;
    @Bind(R.id.ed_address)
    AppCompatEditText mEdAddress;

    private String mAddress;
    private String mArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revamp_address);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        setCommonBackToolBar(mToolbarCommon, mTvTitleCommon, "修改信息");
        String address = SPUtils.getString( Constant.REC_ADDRESS);
        String name = SPUtils.getString( Constant.REC_NAME);
        String phone = SPUtils.getString( Constant.REC_PHONE);

        if ("".equals(address)) {
            String[] split = address.split("区");
            if (split.length>1){
                mAddress = split[1];
                mArea = split[0] + "区";
            }
            mTvArea.setText(mArea);
            mEdAddress.setText(mAddress);

        }
        mEdConsignee.setText(name);
        mEdPhoneNum.setText(phone);
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfo();
            }
        });

    }

    private void saveInfo() {
        RequestParams params = new RequestParams();
        params.put("memid", SPUtils.getString( Constant.MEMID));
        params.put("nickname", SPUtils.getString( Constant.NICK_NAME));
        params.put("signature", SPUtils.getString(Constant.MY_SIGN));
        params.put("recaddress", mEdAddress.getText().toString());
        params.put("recname", mEdConsignee.getText().toString());
        params.put("recphone", mEdPhoneNum.getText().toString());
        params.put("birthday", SPUtils.getString( Constant.BRITHDAY));
        params.put("gender", SPUtils.getString( Constant.GENDER));
        params.put("dimg", "");
        App.getRequestInstance().post(RevampAddress.this, UrlPath.URL_MYINFO_UPDATE, params, new RequestListener() {
            @Override
            public void requestSuccess(String s) {
                try {
                    JSONObject data = new JSONObject(s);
                    String code = data.getString("msgFlag");// ["1","1"]
//                    Log.i("String code1", code);
                    if (code.equals("[\"1\",\"1\"]")) {
//                        Log.i("flag", code.equals("[\"1\",\"1\"]") + "");
                        Toast.makeText(RevampAddress.this, "修改成功", Toast.LENGTH_SHORT).show();
                        HealthUtil.update_local_myinfo();
                        Intent intent = new Intent();
                        intent.putExtra(Constant.REC_NAME, mEdConsignee.getText().toString());
                        intent.putExtra(Constant.REC_PHONE, mEdPhoneNum.getText().toString());
                        intent.putExtra(Constant.REC_ADDRESS, mEdAddress.getText().toString());
                        RevampAddress.this.setResult(Constant.RESULT_OK, intent);
                        finish();
                    } else {
                        Toast.makeText(RevampAddress.this, "服务器端错误:" + data.getString("msgContent"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(RevampAddress.this, "json解析失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void requestAgain(NodataFragment nodataFragment) {

            }
        });
    }

    @Override
    public void initData() {

    }
}
