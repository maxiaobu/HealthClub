package com.maxiaobu.healthclub.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.beangson.BeanLunchOrderList;
import com.maxiaobu.healthclub.ui.activity.FoodOrderDetailActivity;
import com.maxiaobu.healthclub.ui.activity.PayActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 马小布 on 2016/8/25.
 */
public class AdapterCourseOrderFrg extends RecyclerView.Adapter {


    public interface OnCancelItemClickListener {
        public void onItemClick(View view, String what);
    }

    public OnCancelItemClickListener mListener;

    public void setOnCancelItemClickListener(OnCancelItemClickListener listener) {
        mListener = listener;
    }

    public interface OnDelayItemClickListener {
        public void onItemClick(View view, String what);
    }

    public OnDelayItemClickListener mDelayListener;

    public void setOnDelayItemClickListener(OnDelayItemClickListener listener) {
        mDelayListener = listener;
    }

    public interface OnAgainItemClickListener {
        public void onItemClick(View view, String what);
    }

    public OnAgainItemClickListener mAgainListener;

    public void setOnAgainItemClickListener(OnAgainItemClickListener listener) {
        mAgainListener = listener;
    }


    private Activity mActivity;
    private List<BeanLunchOrderList.ForderListBean> mData;

    public AdapterCourseOrderFrg(Activity activity, List<BeanLunchOrderList.ForderListBean> mData) {
        mActivity = activity;
        this.mData = mData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_course_order_list_frg, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder viewHolder = (MyViewHolder) holder;
        BeanLunchOrderList.ForderListBean listBean = mData.get(position);
        //0 代付款；1 待收货；2已完成
        Glide.with(mActivity).load(listBean.getImagesfilename()).placeholder(R.mipmap.ic_place_holder).into(viewHolder.mIvPhoto);

    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_photo)
        ImageView mIvPhoto;
        @Bind(R.id.tv_title)
        TextView mTvTitle;
        @Bind(R.id.tv_content)
        TextView mTvContent;
        @Bind(R.id.tv_complete)
        TextView mTvComplete;
        @Bind(R.id.tv_price)
        TextView mTvPrice;
        @Bind(R.id.tv_course_name)
        TextView mTvCourseName;
        @Bind(R.id.tv_club_name)
        TextView mTvClubName;
        @Bind(R.id.tv_end_time)
        TextView mTvEndTime;
        @Bind(R.id.tv_residue_times)
        TextView mTvResidueTimes;
        @Bind(R.id.tv_appointment)
        TextView mTvAppointment;
        @Bind(R.id.ly_root)
        LinearLayout mLyRoot;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
