package com.maxiaobu.healthclub.ui.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.dao.DataEntryDbHelper;

public class DataEntryActivity extends BaseAty {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_entry);
        DataEntryDbHelper mDbHelper=new DataEntryDbHelper(this,"biaoming");
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        mDbHelper.onCreate(db);
//        db.execSQL("CREATE TABLE IF NOT EXISTS " + "biaoming" +
//                " (_id INTEGER PRIMARY KEY AUTOINCREMENT,itemid TEXT,groups TEXT" +
//                ",strength TEXT,times TEXT)");
        ContentValues values=new ContentValues();
        values.put(DataEntryDbHelper.ITEMID,"itemid");
        values.put(DataEntryDbHelper.GROUPS,"groups");
        values.put(DataEntryDbHelper.STRENGTH,"strength");
        values.put(DataEntryDbHelper.TIMES,"times");
        long biaoming = db.insert("biaoming", null, values);
//        Log.d("DataEntryActivity", "biaoming:" + biaoming);

        Cursor biaoming1 = db.query("biaoming", new String[]{DataEntryDbHelper.ITEMID, DataEntryDbHelper.GROUPS},
               null, null, null, null, DataEntryDbHelper.STRENGTH);
        while (biaoming1.moveToNext()){
            Log.d("DataEntryActivity", "biaoming1.getColumnCount():" + biaoming1.getColumnCount());
            Log.d("DataEntryActivity", biaoming1.getString(0));
        }
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }
}
