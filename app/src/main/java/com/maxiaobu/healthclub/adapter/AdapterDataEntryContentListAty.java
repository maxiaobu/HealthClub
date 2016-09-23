package com.maxiaobu.healthclub.adapter;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.beangson.BeanItems;
import com.maxiaobu.healthclub.utils.recyeler.ItemTouchHelperAdapter;
import com.maxiaobu.healthclub.utils.recyeler.OnStartDragListener;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 马小布 on 2016/8/31.
 */
public class AdapterDataEntryContentListAty extends RecyclerView.Adapter implements ItemTouchHelperAdapter {
    private final OnStartDragListener mDragStartListener;

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
//        if (toPosition==mData.size()+1)
        // TODO: 2016/9/23 拖走
        Collections.swap(mData, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {

    }


    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public interface OnItemLongClickListener {
        public void onItemClick(View view, int position);
    }

    public OnItemLongClickListener mLongListener;

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mLongListener = listener;
    }

    private Context mContext;
    private List<BeanItems.ItemsBean> mData;
    private int selectPosition = 0;

    public AdapterDataEntryContentListAty(Context context, List<BeanItems.ItemsBean> mData,OnStartDragListener dragStartListener) {
        mDragStartListener = dragStartListener;
        mContext = context;
        this.mData = mData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_data_entry_content, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder viewHolder = (MyViewHolder) holder;
//        if (selectPosition == position) {
//            viewHolder.mLyRoot.setBackgroundColor(mContext.getResources().getColor(R.color.gray262626));
//        } else {
//            viewHolder.mLyRoot.setBackgroundColor(mContext.getResources().getColor(R.color.gray444444));
//        }

//        BeanItems.ItemsBean bean = mData.get(position);
//        bean.getImgsfilename();
//        bean.getCreatetime();
//        bean.getPitemid();
//        Glide.with(mContext).load(bean.getImgsfilename()).placeholder(R.mipmap.ic_place_holder).into(viewHolder.mIvRight);
//        viewHolder.mTvTitle.setText(bean.getItemname());
//        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
//        viewHolder.mRvContent.setLayoutManager(layoutManager);
//        viewHolder.mRvContent.setItemAnimator(new DefaultItemAnimator());
////        mRightAdapter = new AdapterDataEntryContentListAty(this, mRightData);
////        viewHolder.mRvContent.setAdapter(mRightAdapter);
//
//
        viewHolder.mLyRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mListener != null) {
//                    selectPosition = position;
                    mListener.onItemClick(view, position);
                }
            }
        });

        viewHolder.mLyRoot.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(mContext, "傻逼傻逼傻逼傻逼傻逼傻逼傻逼", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        viewHolder.mIvRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_group)
        TextView mTvGroup;
        @Bind(R.id.tv_times)
        TextView mTvTimes;
        @Bind(R.id.tv_strength)
        TextView mTvStrength;
        @Bind(R.id.iv_right)
        ImageView mIvRight;
        @Bind(R.id.ly_root)
        RelativeLayout mLyRoot;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
