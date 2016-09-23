package com.maxiaobu.healthclub.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.beangson.BeanItems;
import com.maxiaobu.healthclub.ui.activity.DataEntryActivity;
import com.maxiaobu.healthclub.utils.recyeler.OnStartDragListener;
import com.maxiaobu.healthclub.utils.recyeler.SimpleItemTouchHelperCallback;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 马小布 on 2016/8/31.
 */
public class AdapterDataEntryRightListAty extends RecyclerView.Adapter implements OnStartDragListener {


    public interface OnItemClickListener {
        public void onItemClick(View view, String itemId, String title,int position, int status);//status 0无数据1 有数据
    }

    public OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    private DataEntryActivity mActivity;
    private List<BeanItems.ItemsBean> mData;
    SQLiteDatabase mDb;
    private int selectPosition = 0;
    private ItemTouchHelper mItemTouchHelper;

    public AdapterDataEntryRightListAty(Activity activity, List<BeanItems.ItemsBean> mData, SQLiteDatabase mDb) {
        mActivity = (DataEntryActivity) activity;
        this.mData = mData;
        this.mDb = mDb;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_data_entry_right, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder viewHolder = (MyViewHolder) holder;
        final BeanItems.ItemsBean bean = mData.get(position);
        viewHolder.setIsRecyclable(false);
        if (!mActivity.hasData(bean.getItemid())) {
            //无数据
            viewHolder.mIvRight.setImageResource(R.mipmap.ic_data_entry_add);
            viewHolder.mTvTitle.setText(bean.getItemname());
            viewHolder.mTvContent.setVisibility(View.GONE);
            viewHolder.mLyRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        selectPosition = position;
                        mListener.onItemClick(v, bean.getItemid(), bean.getItemname() + "第1组",position, 0);
                    }
                }
            });
        } else if (position != selectPosition) {
            //有数据未打开
            viewHolder.mIvRight.setImageResource(R.mipmap.ic_down_arrow_data_entry);
            viewHolder.mTvTitle.setText(bean.getItemname());
            Cursor c = mActivity.query(bean.getItemid());
            viewHolder.mTvContent.setText("已录入" + c.getCount() + "组");
            c.close();
            viewHolder.mLyRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        selectPosition = position;
                        mListener.onItemClick(v, bean.getItemid(), null ,position, 1);
                    }
                }
            });
        } else {
            viewHolder.mIvRight.setImageResource(R.mipmap.ic_down_up_data_entry);
            viewHolder.mTvTitle.setText(bean.getItemname());
            Cursor c = mActivity.query(bean.getItemid());
            viewHolder.mTvContent.setText("已录入" + c.getCount() + "组");
            c.close();
            viewHolder.mLyRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        selectPosition = 999999;
                        mListener.onItemClick(v, bean.getItemid(), null ,position, 3);
                    }
                }
            });
            LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
            viewHolder.mRvContent.setLayoutManager(layoutManager);
            viewHolder.mRvContent.setItemAnimator(new DefaultItemAnimator());
            AdapterDataEntryContentListAty mAdapter = new AdapterDataEntryContentListAty(mActivity, mData, AdapterDataEntryRightListAty.this);
            viewHolder.mRvContent.setAdapter(mAdapter);
            ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
            mItemTouchHelper = new ItemTouchHelper(callback);
            mItemTouchHelper.attachToRecyclerView(viewHolder.mRvContent);
            mAdapter.setOnItemClickListener(new AdapterDataEntryContentListAty.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Toast.makeText(mActivity, "sdjkfhsdkjhfsdkajhflksdjhfglkjsdhglkjsdfhglj", Toast.LENGTH_SHORT).show();
                }
            });



//        viewHolder.mLyRoot.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                Toast.makeText(mContext, "傻逼傻逼傻逼傻逼傻逼傻逼傻逼", Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });

            viewHolder.mLyRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        selectPosition = position;
                        mListener.onItemClick(view, bean.getItemid(),
                                bean.getItemname() + "第1组",position, 1);
                    }
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_title)
        TextView mTvTitle;
        @Bind(R.id.tv_content)
        TextView mTvContent;
        @Bind(R.id.iv_right)
        ImageView mIvRight;
        @Bind(R.id.rv_content)
        RecyclerView mRvContent;
        @Bind(R.id.ly_root)
        LinearLayout mLyRoot;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
