package com.maxiaobu.healthclub.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.beangson.BeanMmyBespeak;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 马小布 on 2016/9/10.
 */
public class AdapterTeachingAppointment extends RecyclerView.Adapter {

    private Activity mActivity;
    private  List<BeanMmyBespeak.BespeaklistBean> mData;

    public AdapterTeachingAppointment(Activity activity, List<BeanMmyBespeak.BespeaklistBean> mData) {
        mActivity = activity;
        this.mData = mData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_teaching_appointment_aty, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder viewHolder = (MyViewHolder) holder;
        BeanMmyBespeak.BespeaklistBean bean = mData.get(position);
        //0 代付款；1 待收货；2已完成
        Glide.with(mActivity).load(bean.getImgsfile()).placeholder(R.mipmap.ic_place_holder).into(viewHolder.mIvPhoto);
        viewHolder.mTvName.setText(bean.getNickname());
        viewHolder.mTvOccupation.setText("教练");
        viewHolder.mTvCourse.setText(bean.getCoursename());
        viewHolder.mTvTime.setText(bean.getBegintime());
        viewHolder.mTvAddress.setText(bean.getClubname());
        if (bean.getCoursestatus().equals("0")){
            viewHolder.mTvEvaluate.setText("等待上课");
            viewHolder.mTvEvaluate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mActivity, "等待上课", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            viewHolder.mTvEvaluate.setText("评价");
            viewHolder.mTvEvaluate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mActivity, "评价", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_photo)
        ImageView mIvPhoto;
        @Bind(R.id.tv_name)
        TextView mTvName;
        @Bind(R.id.tv_occupation)
        TextView mTvOccupation;
        @Bind(R.id.tv_course)
        TextView mTvCourse;
        @Bind(R.id.tv_time)
        TextView mTvTime;
        @Bind(R.id.tv_address)
        TextView mTvAddress;
        @Bind(R.id.tv_evaluate)
        TextView mTvEvaluate;
        @Bind(R.id.ly_root)
        LinearLayout mLyRoot;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
