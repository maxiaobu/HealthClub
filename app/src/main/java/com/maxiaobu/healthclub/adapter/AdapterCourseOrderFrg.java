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
import com.maxiaobu.healthclub.common.beangson.BeanCorderList;
import com.maxiaobu.healthclub.ui.activity.FoodOrderDetailActivity;
import com.maxiaobu.healthclub.ui.activity.PayActivity;
import com.maxiaobu.healthclub.utils.TimesUtil;

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
    private List<BeanCorderList.CorderListBean> mData;

    public AdapterCourseOrderFrg(Activity activity, List<BeanCorderList.CorderListBean> mData) {
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
        BeanCorderList.CorderListBean listBean = mData.get(position);
        //0 代付款；1 待收货；2已完成
        Glide.with(mActivity).load(listBean.getCa_imgsfilename()).placeholder(R.mipmap.ic_place_holder).into(viewHolder.mIvPhoto);
        viewHolder.mTvTitle.setText(listBean.getCa_nickname());

        viewHolder.mTvCourseName.setText(listBean.getCoursename());
        viewHolder.mTvClubName.setText(listBean.getClubname());
        viewHolder.mLyRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(mActivity,FoodOrderDetailActivity.class);
//                intent.putExtra("merid",String.valueOf( mData.get(position).getMbfordermerlist().get(0).getMerid()));
                intent.putExtra("ordno",mData.get(position).getOrdno());
                mActivity.startActivity(intent);
            }
        });


        if (listBean.getPaystatus() .equals("0") ) {
            viewHolder.mTvComplete.setText("待付款");
            viewHolder.mTvAppointment.setVisibility(View.GONE);
            viewHolder.mLyNopay.setVisibility(View.VISIBLE);
            viewHolder.mTvRight.setText("继续支付");
            viewHolder.mTvLeft.setText("删除");
            viewHolder.mTvRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(new Intent(mActivity, PayActivity.class));
                    intent.putExtra("totlePrice", String.valueOf(mData.get(position).getOrdamt()));
                    intent.putExtra("ordno", String.valueOf("["+mData.get(position).getOrdno()+"]"));
//                                Log.d("OrderConfirmActivity", object.getOrdno().toString());
                    mActivity.startActivity(intent);
                }
            });
            viewHolder.mTvLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener!=null){
                        mListener.onItemClick(view,String.valueOf(mData.get(position).getOrdno()));
                    }
                }
            });
        } else if (listBean.getOrdstatus() .equals("1") ) {
            viewHolder.mTvComplete.setText("已完成");
            viewHolder.mTvAppointment.setVisibility(View.GONE);
            viewHolder.mLyNopay.setVisibility(View.VISIBLE);
            viewHolder.mTvRight.setText("再来一单");
            viewHolder.mTvLeft.setText("删除");
            viewHolder.mTvRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAgainListener.onItemClick(view,String.valueOf( mData.get(position).getCoachid() ));
                }
            });
            viewHolder.mTvLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener!=null){
                        mListener.onItemClick(view,String.valueOf( mData.get(position).getOrdno() ));
                    }
                }
            });

        } else if (listBean.getBespeaknum() ==listBean.getCoursenum()) {
            viewHolder.mTvComplete.setText("待预约");
            viewHolder.mTvAppointment.setVisibility(View.VISIBLE);
            viewHolder.mLyNopay.setVisibility(View.GONE);
            viewHolder.mTvAppointment.setText("现在预约");
            // TODO: 2016/9/19 yuyue
//            corder.querySelector("#orderExe1").href = "javascript:commitExe('bespeak','" + corderData.ordno + "','" + corderData.coachid + "')";
        } else if (listBean.getBespeaknum() >listBean.getCoursenum()) {
            viewHolder.mTvComplete.setText("已预约");
            viewHolder.mTvAppointment.setVisibility(View.VISIBLE);
            viewHolder.mLyNopay.setVisibility(View.GONE);
            viewHolder.mTvAppointment.setText("查看预约");
            // TODO: 2016/9/19 yuyue
//            corder.querySelector("#orderExe1").href = "javascript:commitExe('查看预约','" + corderData.ordno + "')";
        }
        if (listBean.getPaystatus() .equals("0")) {
//            viewHolder.mTvResidueTimes.setText("剩余" + listBean.getOrdcoursetimes() + "次");
            viewHolder.mTvResidueTimes.setText(listBean.getCoursedays() + "天/"  + listBean.getOrdcoursetimes() + "次");
        } else if (listBean.getOrdstatus().equals("1") ) {
            viewHolder.mTvEndTime.setText("截至日期" +
                    TimesUtil.stringsToTimestamp(
                            String.valueOf(listBean.getOrdenddate().getTime()), "yyyy/MM/dd"));
            viewHolder.mTvResidueTimes.setText(listBean.getCoursedays() + "天/"  + listBean.getOrdcoursetimes() + "次");
        } else {
            viewHolder.mTvResidueTimes.setText("剩余" + listBean.getOrdcoursetimes() + "次");
            viewHolder.mTvEndTime.setText("截至日期" +
                    TimesUtil.stringsToTimestamp(
                            String.valueOf(listBean.getOrdenddate().getTime()), "yyyy/MM/dd"));
        }
        if (listBean.getPaystatus() .equals("2")) {
            viewHolder.mTvPrice.setText("凭会员卡免费预约");
        } else {
            viewHolder.mTvPrice.setText( "共计：" + listBean.getOrdamt() + "元");
        }


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
        @Bind(R.id.tv_right)
        TextView mTvRight;
        @Bind(R.id.tv_left)
        TextView mTvLeft;
        @Bind(R.id.ly_nopay)
        LinearLayout mLyNopay;
        @Bind(R.id.ly_root)
        LinearLayout mLyRoot;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
