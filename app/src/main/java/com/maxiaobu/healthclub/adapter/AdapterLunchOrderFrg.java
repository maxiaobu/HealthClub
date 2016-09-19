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
import com.maxiaobu.healthclub.ui.activity.ModifyOrderAddressActivity;
import com.maxiaobu.healthclub.ui.activity.PayActivity;
import com.maxiaobu.healthclub.ui.fragment.LunchOrderFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 马小布 on 2016/8/25.
 */
public class AdapterLunchOrderFrg extends RecyclerView.Adapter {
    public interface OnCancelItemClickListener {
        public void onItemClick(View view, String what);
    }
    public OnCancelItemClickListener mListener;
    public void setOnCancelItemClickListener(OnCancelItemClickListener listener){
        mListener =listener;
    }

    public interface OnDelayItemClickListener {
        public void onItemClick(View view, String what);
    }
    public OnDelayItemClickListener mDelayListener;
    public void setOnDelayItemClickListener(OnDelayItemClickListener listener){
        mDelayListener =listener;
    }

    public interface OnAgainItemClickListener {
        public void onItemClick(View view, String what);
    }
    public OnAgainItemClickListener mAgainListener;
    public void setOnAgainItemClickListener(OnAgainItemClickListener listener){
        mAgainListener =listener;
    }
    private Activity mActivity;
    private List<BeanLunchOrderList.ForderListBean> mData;
    private LunchOrderFragment fragment;

    public AdapterLunchOrderFrg(Activity activity, List<BeanLunchOrderList.ForderListBean> mData, LunchOrderFragment fragment) {
        mActivity = activity;
        this.mData = mData;
        this.fragment=fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_food_order_list_frg, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder viewHolder = (MyViewHolder) holder;
        BeanLunchOrderList.ForderListBean listBean = mData.get(position);
        //0 代付款；1 待收货；2已完成
        Glide.with(mActivity).load(listBean.getImagesfilename()).placeholder(R.mipmap.ic_place_holder).into(viewHolder.mIvPhoto);
        viewHolder.mTvTitle.setText(listBean.getClubname());
        viewHolder.mTvContent.setText(listBean.getMername()+" X"+listBean.getMbfordermerlist().get(0).getBuynum());
        viewHolder.mTvPrice.setText(String.valueOf(listBean.getOrdamt()) + "元");
        viewHolder.mTvPayPrice.setText(String.valueOf(listBean.getPayamt()) + "元");
        viewHolder.mLyRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(mActivity,FoodOrderDetailActivity.class);
                intent.putExtra("merid",String.valueOf( mData.get(position).getMbfordermerlist().get(0).getMerid()));
                intent.putExtra("ordno",mData.get(position).getOrdno());
                mActivity.startActivity(intent);
            }
        });

        if (listBean.getOrdstatus().equals("0")) {
            //未完成
            if (listBean.getPaystatus().equals("0")) {
                //待付款
                viewHolder.mTvComplete.setText("待付款");
                viewHolder.mTvPay.setText("继续支付");
                viewHolder.mTvDelete.setText("取消");
                viewHolder.mTvPay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(new Intent(mActivity, PayActivity.class));
                        intent.putExtra("totlePrice", String.valueOf(mData.get(position).getOrdamt()));
                        intent.putExtra("ordno", String.valueOf("["+mData.get(position).getOrdno()+"]"));
//                                Log.d("OrderConfirmActivity", object.getOrdno().toString());
                        mActivity.startActivity(intent);


                    }
                });
                viewHolder.mTvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mListener!=null){
                            mListener.onItemClick(view,String.valueOf(mData.get(position).getOrdno() ));
                        }
                    }
                });

            } else {
                //已付款 为评价
                viewHolder.mTvComplete.setText("未完成");
                viewHolder.mTvPay.setText("延期");
                viewHolder.mTvDelete.setText("修改地址");
                viewHolder.mTvPay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDelayListener.onItemClick(view,String.valueOf( mData.get(position).getOrdno()));

                    }
                });
                viewHolder.mTvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //修改地址
                        Intent intent = new Intent();
                        intent.putExtra("ordno",String.valueOf( mData.get(position).getOrdno()));
                        intent.putExtra("recaddress",String.valueOf(mData.get(position).getRecaddress()));
                        intent.setClass(mActivity, ModifyOrderAddressActivity.class);
                        fragment.startActivityForResult(intent,2);
                    }
                });

            }
        } else {//2已评价
            viewHolder.mTvComplete.setText("已完成");
            viewHolder.mTvPay.setText("再来一单");
            viewHolder.mTvPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAgainListener.onItemClick(view,String.valueOf( mData.get(position).getMbfordermerlist().get(0).getMerid() ));
                }
            });
            viewHolder.mTvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener!=null){
                        mListener.onItemClick(view,String.valueOf( mData.get(position).getOrdno() ));
                    }
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
        @Bind(R.id.tv_title)
        TextView mTvTitle;
        @Bind(R.id.tv_content)
        TextView mTvContent;
        @Bind(R.id.tv_complete)
        TextView mTvComplete;
        @Bind(R.id.tv_price)
        TextView mTvPrice;
        @Bind(R.id.tv_pay_price)
        TextView mTvPayPrice;
        @Bind(R.id.tv_pay)
        TextView mTvPay;
        @Bind(R.id.tv_delete)
        TextView mTvDelete;
        @Bind(R.id.ly_nopay)
        LinearLayout mlyNopay;
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
