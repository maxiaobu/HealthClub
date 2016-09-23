package com.maxiaobu.healthclub.ui.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.adapter.AdapterDataEntryLeftListAty;
import com.maxiaobu.healthclub.adapter.AdapterDataEntryRightListAty;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanItems;
import com.maxiaobu.healthclub.common.beangson.BeanMgettrainingitem;
import com.maxiaobu.healthclub.dao.DataEntryDbHelper;
import com.maxiaobu.healthclub.volleykit.JsonUtils;
import com.maxiaobu.healthclub.volleykit.NodataFragment;
import com.maxiaobu.healthclub.volleykit.RequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DataEntryActivity extends BaseAty implements View.OnClickListener {

    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;
    @Bind(R.id.rv_left)
    RecyclerView mRvLeft;
    @Bind(R.id.rv_right)
    RecyclerView mRvRight;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.tv_times)
    TextView mTvTimes;
    @Bind(R.id.iv_times_add)
    ImageView mIvTimesAdd;
    @Bind(R.id.tv_times_num)
    TextView mTvTimesNum;
    @Bind(R.id.iv_times_reduce)
    ImageView mIvTimesReduce;
    @Bind(R.id.tv_strength)
    TextView mTvStrength;
    @Bind(R.id.iv_strength_add)
    ImageView mIvStrengthAdd;
    @Bind(R.id.tv_strength_num)
    TextView mTvStrengthNum;
    @Bind(R.id.iv_strength_reduce)
    ImageView mIvStrengthReduce;
    @Bind(R.id.tv_delete)
    TextView mTvDelete;
    @Bind(R.id.tv_save)
    TextView mTvSave;
    @Bind(R.id.ll_edit)
    LinearLayout mLlEdit;
    @Bind(R.id.fl_edit)
    FrameLayout mFlEdit;
    private List<BeanMgettrainingitem.ListBean.ItemsBean> mLeftData;
    List<BeanItems.ItemsBean> mRightData;
    AdapterDataEntryLeftListAty mLeftAdapter;
    AdapterDataEntryRightListAty mRightAdapter;
    private SQLiteDatabase mDb;
    private String mCorderlessonid;//表名
    private int mTimesNum = 1;
    private int mStrengthNum = 1;
    private int inputType;//0第一次录入数据  1有数据未打开
    private String currentItemID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_entry);
//        ContentValues values = new ContentValues();
//        values.put(DataEntryDbHelper.ITEMID, "itemid");
//        values.put(DataEntryDbHelper.GROUPS, "groups");
//        values.put(DataEntryDbHelper.STRENGTH, "strength");
//        values.put(DataEntryDbHelper.TIMES, "times");
//        long biaoming = mDb.insert("biaoming", null, values);
//        Log.d("DataEntryActivity", "biaoming:" + biaoming);
//
//        Cursor biaoming1 = db.query("biaoming", new String[]{DataEntryDbHelper.ITEMID, DataEntryDbHelper.GROUPS},
//                null, null, null, null, DataEntryDbHelper.STRENGTH);
//        while (biaoming1.moveToNext()) {
//            Log.d("DataEntryActivity", "biaoming1.getColumnCount():" + biaoming1.getColumnCount());
//            Log.d("DataEntryActivity", biaoming1.getString(0));
//        }
//
//        int biaming = db.delete("biaoming", DataEntryDbHelper.GROUPS+" LIKE ?", new String[]{"groups"});

        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        setCommonBackToolBar(mToolbarCommon, mTvTitleCommon, "训练数据录入");
        mLeftData = new ArrayList<>();
        mRightData = new ArrayList<>();
        mCorderlessonid = getIntent().getStringExtra("corderlessonid");
        DataEntryDbHelper mDbHelper = new DataEntryDbHelper(this, mCorderlessonid);
        mDb = mDbHelper.getWritableDatabase();
        mDbHelper.onCreate(mDb);

        LinearLayoutManager layoutManagerLeft = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRvLeft.setLayoutManager(layoutManagerLeft);
        mRvLeft.setItemAnimator(new DefaultItemAnimator());
        mLeftAdapter = new AdapterDataEntryLeftListAty(this, mLeftData);
        mRvLeft.setAdapter(mLeftAdapter);

        LinearLayoutManager layoutManagerRight = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRvRight.setHasFixedSize(true);
        mRvRight.setLayoutManager(layoutManagerRight);
        mRvRight.setItemAnimator(new DefaultItemAnimator());
        mRightAdapter = new AdapterDataEntryRightListAty(this, mRightData, mDb);
        mRvRight.setAdapter(mRightAdapter);
        mRightAdapter.setOnItemClickListener(new AdapterDataEntryRightListAty.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String itemId, String title,int position, int status) {
                if (status == 0) {
                    inputType = status;
                    currentItemID = itemId;
                    mTvTitle.setText(title);
                    showMenu();
                }else if (status==1){
                    inputType = status;
                    currentItemID = itemId;
                    mRightAdapter.notifyDataSetChanged();
                }else if (status==3){
                    inputType = status;
                    mRightAdapter.notifyItemChanged(position);
                }

            }
        });
    }

    @Override
    public void initData() {
        App.getRequestInstance().post(this, UrlPath.URL_MGETTRAININGITEM, null, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                try {
                    JSONObject object = new JSONObject(json);
                    JSONArray list = object.getJSONArray("list");
                    final JSONObject o = list.getJSONObject(0);
                    final JSONArray items = o.getJSONArray("items");

                    BeanMgettrainingitem result = JsonUtils.object(json, BeanMgettrainingitem.class);
                    mLeftData.clear();
                    mLeftData.addAll(result.getList().get(0).getItems());
                    mLeftAdapter.notifyDataSetChanged();
                    mLeftAdapter.setOnItemClickListener(new AdapterDataEntryLeftListAty.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            try {
                                mLeftAdapter.notifyDataSetChanged();
                                mRightData.clear();
                                JSONObject jsonObject = items.getJSONObject(position);
                                String s = "{ \"items\": " + jsonObject.getJSONArray("items").toString() + "}";
//                                Log.d("DataEntryActivity", s);
                                BeanItems m = JsonUtils.object(s, BeanItems.class);
                                mRightData.addAll(m.getItems());
                                mRightAdapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    mRightData.clear();
                    JSONObject jsonObject = items.getJSONObject(0);
                    String s = "{ \"items\": " + jsonObject.getJSONArray("items").toString() + "}";
//                    Log.d("DataEntryActivity", s);
                    BeanItems m = JsonUtils.object(s, BeanItems.class);
                    mRightData.addAll(m.getItems());
                    mRightAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mActivity, "DataEntryActivityjson解析失败", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void requestAgain(NodataFragment fragment) {

            }
        });

    }

    @OnClick({R.id.iv_times_add, R.id.iv_times_reduce, R.id.iv_strength_add,
            R.id.iv_strength_reduce, R.id.tv_save, R.id.tv_delete})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_times_add:
                mTimesNum++;
                mTvTimesNum.setText(String.valueOf(mTimesNum));
                break;
            case R.id.iv_times_reduce:
                if (mTimesNum > 1)
                    mTimesNum--;
                mTvTimesNum.setText(String.valueOf(mTimesNum));
                break;
            case R.id.iv_strength_add:
                mStrengthNum++;
                mTvStrengthNum.setText(String.valueOf(mStrengthNum));
                break;
            case R.id.iv_strength_reduce:
                if (mStrengthNum > 1)
                    mStrengthNum--;
                mTvStrengthNum.setText(String.valueOf(mStrengthNum));
                break;
            case R.id.tv_save:
                if (inputType == 0) {
                    add(currentItemID, "1", mTvStrength.getText().toString(), mTvTimes.getText().toString());
                    mRightAdapter.notifyDataSetChanged();
                    hideMenu();
                }

                break;
            case R.id.tv_delete:
                if (inputType == 0) {
                    mRightAdapter.notifyDataSetChanged();
                    hideMenu();
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onBackPressed() {
        if (mFlEdit.getVisibility() == View.VISIBLE) {
            hideMenu();
        } else {
            super.onBackPressed();
        }

    }

    public Cursor query(String itemid) {
        Cursor c = mDb.rawQuery("select * from " + mCorderlessonid + " where itemid=?", new String[]{itemid});
//                mDb.query(mCorderlessonid, null, DataEntryDbHelper.ITEMID,
//                new String[]{"?",itemid}, null, null, DataEntryDbHelper.GROUPS);
//        while (c.moveToNext()) {
//            Log.d("DataEntryActivity", "biaoming1.getColumnCount():" + c.getColumnCount());
//            Log.d("DataEntryActivity", c.getString(0));
//        }
return c;
//        int biaming = mDb.delete("biaoming", DataEntryDbHelper.GROUPS+" LIKE ?", new String[]{"groups"});
    }

    public boolean hasData(String itemid) {
        Cursor c = mDb.rawQuery("select * from " + mCorderlessonid + " where itemid=?", new String[]{itemid});
        int count = c.getCount();
        Log.d("DataEntryActivity", "biaoming1.getColumnCount():" + count);
        return count > 0;
//        int biaming = mDb.delete("biaoming", DataEntryDbHelper.GROUPS+" LIKE ?", new String[]{"groups"});
    }

    public void add(String itemId, String groups, String strength, String times) {
        ContentValues values = new ContentValues();
        values.put(DataEntryDbHelper.ITEMID, itemId);
        values.put(DataEntryDbHelper.GROUPS, groups);
        values.put(DataEntryDbHelper.STRENGTH, strength);
        values.put(DataEntryDbHelper.TIMES, times);
        long biaoming = mDb.insert(mCorderlessonid, null, values);
        Log.d("DataEntryActivity", "biaoming:" + biaoming);
    }


    public void hideMenu() {

        mFlEdit.setVisibility(View.GONE);
        mFlEdit.setAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.dd_mask_out));
        mLlEdit.setAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.bottom_menu_out));
        mTimesNum = 1;
        mStrengthNum = 1;
        mTvTimesNum.setText(String.valueOf(mTimesNum));
        mTvStrengthNum.setText(String.valueOf(mStrengthNum));

    }

    public void showMenu() {
        mFlEdit.setVisibility(View.VISIBLE);
        mFlEdit.setAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.dd_mask_in));
        mLlEdit.setAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.bottom_menu_in));
    }


}
